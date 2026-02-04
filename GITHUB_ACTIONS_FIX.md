# GitHub Actions Build Failure Fix

## Problem

The GitHub Actions workflow was failing for all builds (app and wear) with the following error:

```
FAILURE: Build failed with an exception.

* Where:
Build file '/home/runner/work/Meshtastic-Android/Meshtastic-Android/app/build.gradle.kts' line: 65

* What went wrong:
For input string: ""
```

## Root Cause

The `VERSION_CODE` environment variable was being set in the GitHub Actions workflow but was coming through as an empty string (`""`). When the build.gradle.kts files tried to parse this empty string using `.toInt()`, it threw a `NumberFormatException` because an empty string cannot be converted to an integer.

### Original Code (app/build.gradle.kts)

```kotlin
versionCode =
    (
        project.findProperty("android.injected.version.code")?.toString()?.toInt()
            ?: System.getenv("VERSION_CODE")?.toInt()  // ❌ Fails when VERSION_CODE = ""
            ?: (gitVersionProvider.get().toInt() + vcOffset)
    )
```

**Issue:** The `?.toInt()` operator only checks if the string is null, not if it's empty. When `VERSION_CODE` is an empty string, it still tries to parse it and crashes.

## Solution

### Fix 1: app/build.gradle.kts (lines 62-67)

```kotlin
versionCode =
    (
        project.findProperty("android.injected.version.code")?.toString()?.toIntOrNull()
            ?: System.getenv("VERSION_CODE")?.takeIf { it.isNotEmpty() }?.toIntOrNull()
            ?: (gitVersionProvider.get().toInt() + vcOffset)
    )
```

**Changes:**
1. **`toInt()` → `toIntOrNull()`**: Returns null instead of throwing exception if parsing fails
2. **Added `takeIf { it.isNotEmpty() }`**: Filters out empty strings before attempting to parse
3. **Safe chain**: If VERSION_CODE is empty or invalid, falls back to git-based version code

### Fix 2: wear/build.gradle.kts (line 34)

```kotlin
// Before
versionCode = 1

// After
versionCode = System.getenv("VERSION_CODE")?.takeIf { it.isNotEmpty() }?.toIntOrNull() ?: 1
```

**Changes:**
1. Now reads VERSION_CODE from environment variable (consistent with app module)
2. Safely handles empty or invalid VERSION_CODE values
3. Falls back to 1 if VERSION_CODE is not available

## How It Works

### Safe Parsing Chain

```kotlin
System.getenv("VERSION_CODE")?.takeIf { it.isNotEmpty() }?.toIntOrNull()
    │                              │                         │
    │                              │                         └─ Safely parse to Int (null if invalid)
    │                              └─ Filter out empty strings
    └─ Get environment variable (null if not set)
```

### Examples

| VERSION_CODE Value | Result |
|-------------------|--------|
| `"12345"` | `12345` ✅ |
| `null` (not set) | Falls back to git version ✅ |
| `""` (empty string) | Falls back to git version ✅ |
| `"abc"` (invalid) | Falls back to git version ✅ |

## Testing

### Scenario 1: Empty VERSION_CODE
```bash
VERSION_CODE="" ./gradlew :app:assembleGoogleDebug
```
**Result:** ✅ Builds successfully, uses git-based version code

### Scenario 2: Valid VERSION_CODE
```bash
VERSION_CODE="12345" ./gradlew :app:assembleGoogleDebug
```
**Result:** ✅ Builds successfully, uses version code 12345

### Scenario 3: No VERSION_CODE
```bash
./gradlew :app:assembleGoogleDebug
```
**Result:** ✅ Builds successfully, uses git-based version code

## Impact on GitHub Actions

The GitHub Actions workflow (`.github/workflows/build.yml`) sets VERSION_CODE like this:

```yaml
- name: Calculate Version Code
  id: version
  uses: ./.github/actions/calculate-version-code

- name: Build APK
  env:
    VERSION_CODE: ${{ steps.version.outputs.VERSION_CODE }}
```

**Before the fix:**
- If `steps.version.outputs.VERSION_CODE` was empty → Build failed ❌

**After the fix:**
- If `steps.version.outputs.VERSION_CODE` is empty → Uses fallback version ✅
- If `steps.version.outputs.VERSION_CODE` has a value → Uses that value ✅

## Benefits

✅ **Robust**: Handles all edge cases (null, empty, invalid)
✅ **Safe**: Uses `toIntOrNull()` instead of `toInt()`
✅ **Consistent**: Both app and wear modules handle VERSION_CODE the same way
✅ **Fallback**: Always has a valid version code through fallback chain
✅ **No crashes**: Never throws NumberFormatException

## Files Changed

1. **app/build.gradle.kts** (line 62-67)
   - Added safe parsing for VERSION_CODE
   
2. **wear/build.gradle.kts** (line 34)
   - Now uses VERSION_CODE from environment with safe parsing
   - Falls back to 1 if VERSION_CODE is not available

## Verification

To verify the fix works, check that:
1. ✅ GitHub Actions workflow completes successfully
2. ✅ App APKs are built and uploaded as artifacts
3. ✅ Wear APK is built and uploaded as artifact
4. ✅ No "For input string: ''" errors in the logs
5. ✅ Version codes are correctly set in the APKs

## Summary

The build failure was caused by attempting to parse an empty string as an integer. The fix adds proper empty string checking and safe integer parsing, ensuring builds succeed regardless of the VERSION_CODE value. Both the app and wear modules now handle VERSION_CODE consistently and safely.

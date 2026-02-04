# Meshtastic Wear OS Application

This module contains the Wear OS version of the Meshtastic application, designed to run on Wear OS 8.0+ devices including Galaxy Watch.

## Features

### Supported Features
- **Messages**: View and send messages through the mesh network
- **Nodes**: View connected nodes and their status
- **Standalone Mode**: Connect directly to Meshtastic radios via Bluetooth without needing a phone
- **Companion Mode**: Can also sync with the phone app (optional)

### Not Included (By Design)
- Maps (not suitable for watch interface)
- Configuration/Settings (better handled on phone)
- Firmware updates (better handled on phone)

## Architecture

### Standalone vs Companion Mode

The Wear OS app supports two operating modes:

1. **Standalone Mode** (Primary):
   - Watch connects directly to Meshtastic radio via Bluetooth LE
   - No phone required
   - Full messaging and node viewing functionality
   - Ideal for users who want to use their watch independently

2. **Companion Mode** (Optional):
   - Can sync with phone app when both are connected
   - Uses Wear Data Layer API for phone↔watch communication
   - Provides backup connectivity option

## Technical Details

### Minimum Requirements
- Wear OS 8.0+ (API 33+)
- Bluetooth LE support
- Works on devices like Samsung Galaxy Watch 4+, Pixel Watch, etc.

### Dependencies
- Wear Compose Material (navigation, foundation)
- AndroidX Wear libraries
- Shared core modules from main app (data, service, proto, etc.)
- Nordic BLE library for Bluetooth connectivity

### UI Framework
- Jetpack Compose for Wear OS
- Material Design for Wear OS
- Swipe-to-dismiss navigation
- Rotary input support (for devices with rotating bezel)

## Building

The wear module is included in the main project build:

```bash
# Build wear debug APK
./gradlew :wear:assembleDebug

# Build wear release APK
./gradlew :wear:assembleRelease

# Install on connected Wear OS device
./gradlew :wear:installDebug
```

## Development Notes

### Shared Code
The wear module reuses core functionality from the main app:
- `core:service` - MeshService for Bluetooth communication
- `core:data` - Data layer and repositories
- `core:model` - Data models
- `core:proto` - Protobuf definitions
- `feature:messaging` - Messaging feature (adapted for Wear UI)
- `feature:node` - Node list feature (adapted for Wear UI)

### Wear-Specific Considerations
1. **Screen Size**: Optimized for 390-500px diameter circular displays
2. **Input Methods**: Touch, rotary dial, limited buttons
3. **Battery**: More aggressive power management than phones
4. **Network**: Limited WiFi, primarily Bluetooth connectivity
5. **UI Patterns**: Uses Wear-specific components like ScalingLazyColumn, TimeText, Vignette

## Testing

Testing on a physical Wear OS device is recommended for the best experience, but you can also use the Wear OS emulator:

```bash
# Create Wear OS emulator (one time)
# Use Android Studio AVD Manager to create a Wear OS emulator

# Run on emulator
./gradlew :wear:installDebug
```

## Future Enhancements

Potential improvements for future versions:
- Watch face complication showing mesh status
- Quick reply templates for messaging
- Voice input for messages
- Wear OS tile for quick access
- Health sensor integration (heart rate, etc. as mesh data)
- More sophisticated phone sync capabilities

## Troubleshooting

### Bluetooth Connection Issues
- Ensure Bluetooth is enabled on watch
- Check that location permissions are granted (required for BLE scanning)
- Verify the Meshtastic device is in pairing mode

### Build Issues
- Ensure you have Android SDK API 36 installed
- Check that Wear OS system images are available
- Run `./gradlew clean` before building

## Contributing

When contributing to the Wear OS module:
1. Test on actual Wear OS hardware when possible
2. Follow Wear OS design guidelines
3. Keep the UI simple and focused on essential features
4. Optimize for battery life and small screen size
5. Ensure standalone mode works without phone connectivity

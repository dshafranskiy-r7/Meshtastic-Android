# Wear OS ProGuard rules

# Keep Wear OS compose components
-keep class androidx.wear.compose.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class ** extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Meshtastic service interface
-keep class com.geeksville.mesh.service.** { *; }

# Keep Bluetooth related classes  
-keep class no.nordicsemi.** { *; }

# Keep data models
-keep class org.meshtastic.core.model.** { *; }
-keep class org.meshtastic.core.proto.** { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.geeksville.mesh.wear.**$$serializer { *; }
-keepclassmembers class com.geeksville.mesh.wear.** {
    *** Companion;
}
-keepclasseswithmembers class com.geeksville.mesh.wear.** {
    kotlinx.serialization.KSerializer serializer(...);
}

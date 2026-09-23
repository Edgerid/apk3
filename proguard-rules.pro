# Room
-keep class androidx.room.** { *; }
-keep @androidx.room.Entity class * { *; }
-dontwarn androidx.room.paging.**

# Gson / Retrofit (used only for optional barcode/food lookup and JSON export/import)
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.personalfitnessos.data.remote.** { *; }
-keep class com.personalfitnessos.data.local.entity.** { *; }
-keep class com.personalfitnessos.domain.model.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keepattributes Exceptions

# ML Kit barcode scanning
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# Kotlin coroutines
-dontwarn kotlinx.coroutines.**

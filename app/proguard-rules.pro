# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Firestore
-keep class com.google.firebase.firestore.** { *; }
-keepclassmembers class com.google.firebase.firestore.** { *; }

# Firebase Auth
-keep class com.google.firebase.auth.** { *; }
-keepclassmembers class com.google.firebase.auth.** { *; }

# Keep data model classes
-keep class xyz.ecys.simplemacro.data.model.** { *; }
-keepclassmembers class xyz.ecys.simplemacro.data.model.** { *; }

# Keep ViewModels
-keep class xyz.ecys.simplemacro.ui.viewmodel.** { *; }
-keepclassmembers class xyz.ecys.simplemacro.ui.viewmodel.** { *; }

# Room Database
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-dontwarn androidx.room.paging.**

# Keep Room migrations
-keep class androidx.room.migration.** { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keep,includedescriptorclasses class xyz.ecys.simplemacro.**$$serializer { *; }
-keepclassmembers class xyz.ecys.simplemacro.** {
    *** Companion;
}
-keepclasseswithmembers class xyz.ecys.simplemacro.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Jetpack Compose
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Navigation Compose
-keep class androidx.navigation.** { *; }
-keepclassmembers class androidx.navigation.** { *; }

# DataStore
-keep class androidx.datastore.** { *; }
-keepclassmembers class androidx.datastore.** { *; }

# Keep safe args classes for Navigation
-keep class xyz.ecys.simplemacro.**.*Args { *; }
-keep class xyz.ecys.simplemacro.**.*Directions { *; }

# Preserve line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Keep annotations
-keepattributes *Annotation*

# Keep generic signature
-keepattributes Signature

# For native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Enum
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

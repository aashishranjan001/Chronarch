# Preserve Line Numbers and Source File names for readable crash stack traces in Play Console / Crashlytics
-keepattributes SourceFile,LineNumberTable

# Preserve Annotations (needed for Jetpack Compose, Room, Dagger/Hilt, Moshi, Retrofit, etc.)
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Anonymize file names while preserving line numbers for mapping
-renamesourcefileattribute SourceFile

# Data Models & Serialization
-keep class com.aashish.chronarch.**.data.**.model.** { *; }
-keepclassmembers class com.aashish.chronarch.**.data.**.model.** { *; }
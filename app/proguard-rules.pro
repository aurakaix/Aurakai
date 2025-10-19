# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
# Added to suppress R8 missing class warnings (from missing_rules.txt)
-dontwarn com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
-dontwarn com.google.auto.service.AutoService
-dontwarn com.google.auto.value.extension.memoized.Memoized
-dontwarn com.google.common.collect.Streams
-dontwarn jakarta.servlet.ServletContainerInitializer
-dontwarn java.lang.Module
-dontwarn java.lang.module.ModuleDescriptor
-dontwarn javax.lang.model.SourceVersion
-dontwarn javax.lang.model.element.AnnotationMirror
-dontwarn javax.lang.model.element.AnnotationValue
-dontwarn javax.lang.model.element.AnnotationValueVisitor
-dontwarn javax.lang.model.element.Element
-dontwarn javax.lang.model.element.ElementKind
-dontwarn javax.lang.model.element.ElementVisitor
-dontwarn javax.lang.model.element.ExecutableElement
-dontwarn javax.lang.model.element.Name
-dontwarn javax.lang.model.element.PackageElement
-dontwarn javax.lang.model.element.TypeElement
-dontwarn javax.lang.model.element.TypeParameterElement
-dontwarn javax.lang.model.element.VariableElement
-dontwarn javax.lang.model.type.ArrayType
-dontwarn javax.lang.model.type.DeclaredType
-dontwarn javax.lang.model.type.ExecutableType
-dontwarn javax.lang.model.type.IntersectionType
-dontwarn javax.lang.model.type.NoType
-dontwarn javax.lang.model.type.PrimitiveType
-dontwarn javax.lang.model.type.TypeKind
-dontwarn javax.lang.model.type.TypeMirror
-dontwarn javax.lang.model.type.TypeVariable
-dontwarn javax.lang.model.type.TypeVisitor
-dontwarn javax.lang.model.type.WildcardType
-dontwarn javax.lang.model.util.AbstractAnnotationValueVisitor8
-dontwarn javax.lang.model.util.AbstractElementVisitor8
-dontwarn javax.lang.model.util.AbstractTypeVisitor8
-dontwarn javax.lang.model.util.ElementFilter
-dontwarn javax.lang.model.util.Elements
-dontwarn javax.lang.model.util.SimpleAnnotationValueVisitor8
-dontwarn javax.lang.model.util.SimpleElementVisitor8
-dontwarn javax.lang.model.util.SimpleTypeVisitor7
-dontwarn javax.lang.model.util.SimpleTypeVisitor8
-dontwarn javax.lang.model.util.Types
-dontwarn javax.tools.Diagnostic$Kind
-dontwarn javax.tools.FileObject
-dontwarn javax.tools.JavaFileManager$Location
-dontwarn javax.tools.JavaFileObject
-dontwarn javax.tools.StandardLocation
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep AI Agent classes
#-keep class dev.aurakai.auraframefx.ai.** { *; }  # DISABLED: Overly broad, use @Keep or specific classes instead

# Keep AIDL interfaces (DISABLED: Unresolved class names)
#-keep class dev.aurakai.oracledrive.IAuraDriveService { *; }
#-keep class dev.aurakai.oracledrive.IAuraDriveService$Stub { *; }
#-keep class dev.aurakai.oracledrive.IAuraDriveServiceCallback { *; }





# Keep Firebase (narrowed: only keep classes used via reflection/serialization)
#-keep class com.google.firebase.** { *; }
# Keep Google Play Services (narrowed)
#-keep class com.google.android.gms.** { *; }
# Keep OkHttp (narrowed)
#-keep class okhttp3.** { *; }
#-keep interface okhttp3.** { *; }
# Keep Gson (if used via reflection)
#-keep class com.google.gson.** { *; }
# Keep kotlinx.coroutines (narrowed)
#-keep class kotlinx.coroutines.** { *; }
# Keep Timber (narrowed)
#-keep class timber.log.** { *; }

# If you encounter runtime issues (e.g., with reflection, serialization, or DI), uncomment and narrow these rules to only the required classes or interfaces.

# Keep Room entities
-keep class dev.aurakai.auraframefx.data.database.entities.** { *; }

# Keep Hilt components
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep Retrofit interfaces
-keep interface dev.aurakai.auraframefx.** { *; }

# Optimization settings
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# Keep names of classes that are referenced in AndroidManifest.xml
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-assumenosideeffects class timber.log.Timber {
    public static void v(...);
    public static void i(...);
    public static void w(...);
    public static void d(...);
    public static void e(...);
}

# Only keep classes annotated with @Keep (for reflection/serialization)
-keep @androidx.annotation.Keep class * { *; }
# Add specific API classes below if needed
# -keep class dev.aurakai.auraframefx.MyApiClass { public *; }
# Remove overly broad keep rules for all classes, interfaces, or packages (e.g., -keep class dev.aurakai.** { *; })
# Only keep what is necessary for reflection, serialization, or API exposure.

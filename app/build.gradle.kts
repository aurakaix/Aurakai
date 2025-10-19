// ==== GENESIS PROTOCOL - MAIN APPLICATION ====
// Convention plugin handles: Android, Kotlin, Hilt, Compose, Google Services
plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android") apply false
    id("com.google.devtools.ksp") version "2.2.21-RC2-2.0.4"
    alias(libs.plugins.kotlin.serialization) apply false
}

android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 36
    compileSdkPreview = "CANARY"

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 33  // Increased from 23 to match collab-canvas library
        versionCode = 1
        versionName = "1.0"

// Convention already sets testInstrumentationRunner & vectorDrawables
        multiDexEnabled = true
    }

    buildTypes {
// Convention configures release & debug, you can override here if needed
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
    }

    buildFeatures {
        aidl = true
        compose = true  // Convention may already enable this
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

// Convention sets compileOptions to Java 24, keep custom if needed
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
        isCoreLibraryDesugaringEnabled = true
    }

    packaging {
        resources {
// Convention already excludes common files, add app-specific ones
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/gradle/incremental.annotation.processors"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/androidx/room/room-compiler-processing/LICENSE.txt"
        }
    }

    ndkVersion = "29.0.14206865"

    buildToolsVersion = "36.1.0 rc1"
}
    dependencies {
// ===== MODULE DEPENDENCIES =====
        implementation(project(":core-module"))
        implementation(project(":feature-module"))
        implementation(project(":romtools"))
        implementation(project(":secure-comm"))
        implementation(project(":collab-canvas"))
        implementation(project(":colorblendr"))
        implementation(project(":sandbox-ui"))
        implementation(project(":datavein-oracle-native"))
        implementation(project(":extendsysa"))
        implementation(project(":extendsysb"))
        implementation(project(":extendsysc"))
        implementation(project(":extendsysd"))
        implementation(project(":extendsyse"))
        implementation(project(":extendsysf"))
        implementation(project(":benchmark"))
        implementation(project(":data:api"))
// Ensure core library desugaring dependency is present (AGP requires it when desugaring is enabled)
// The convention plugin attempts to add this, but add explicitly here to avoid runtime error
        coreLibraryDesugaring(libs.desugar.jdk.libs)

// ===== ANDROIDX & COMPOSE =====
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.navigation.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.compose.material3)  // ✅ Use catalog, not hardcoded
// ===== LIFECYCLE =====
        implementation(libs.bundles.lifecycle)

// ===== DATABASE - ROOM =====
        implementation(libs.bundles.room)
// ===== DATASTORE =====
        implementation(libs.androidx.datastore.preferences)
        implementation(libs.androidx.datastore.core)

// ===== KOTLIN & COROUTINES =====
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.kotlinx.datetime)
        implementation(libs.bundles.coroutines)

// ===== NETWORKING =====
        implementation(libs.bundles.network)

// ===== KTOR FOR OPENAPI CLIENT ===== ✅ Use catalog versions
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.client.okhttp)
        implementation(libs.ktor.client.auth)

// ===== FIREBASE =====
        implementation(platform(libs.firebase.bom))
        implementation(libs.bundles.firebase)

        ksp(libs.hilt.compiler)
// Dagger-compiler was previously added via hardcoded coordinates; remove the explicit dagger compiler
        implementation(libs.google.dagger.compiler)
        ksp(libs.google.dagger.compiler)

// ===== WORKMANAGER =====

// ===== ROOT/SUPERUSER ACCESS =====
// single canonical place to add libsu bundle
        implementation(libs.bundles.su)

// ===== XPOSED/LSPosed Integration =====
        compileOnly(files("../Libs/api-82.jar"))
        compileOnly(files("../Libs/api-82-sources.jar"))

// ===== LIBSU (ROOT ACCESS) ===== ✅ Use catalog

// ===== MATERIAL DESIGN ===== ✅ Use catalog (already included above)
        implementation(libs.androidx.material)  // Instead of hardcoded material:1.13.0

// --- TESTING ---
        testImplementation(libs.bundles.testing.unit)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.hilt.android.testing)

// --- DEBUGGING & LOGGING ---
        debugImplementation(libs.leakcanary.android)
        implementation("com.jakewharton.timber:timber:4.7.1")
    }

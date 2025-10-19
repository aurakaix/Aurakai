plugins {
    alias(libs.plugins.android.library) version "9.0.0-alpha10"
    id("com.google.devtools.ksp") version "2.2.21-RC2-2.0.4"
}

    android {
        namespace = "dev.aurakai.auraframefx.oracledriveintegration"
        compileSdk = 36
        defaultConfig {
            minSdk = 33
        }
        ndkVersion = "29.0.14206865"
        experimentalProperties["android.ndk.suppressMinSdkVersionError"] = 21
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_25
            targetCompatibility = JavaVersion.VERSION_25
        }
    }

    dependencies {
        implementation(project(":core-module"))
        implementation(project(":secure-comm"))
        implementation(libs.androidx.core.ktx)
        implementation(libs.bundles.lifecycle)
        implementation(libs.bundles.coroutines)
        implementation(libs.hilt.android)
        implementation(libs.hilt.work)
        ksp(libs.hilt.compiler)
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.2.20")
    }


    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

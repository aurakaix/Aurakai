// Apply YukiHook conventions to all modules
subprojects { subproject ->
    // Skip build-logic and other non-Android modules
    if (subproject.name == "build-logic" || subproject.name == "buildSrc") {
        return@subprojects
    }

    // Check if this is an Android module
    val isAndroidModule =
        subproject.plugins.hasPlugin("com.android.library") ||
                subproject.plugins.hasPlugin("com.android.application")

    if (isAndroidModule) {
        // Apply common Android and YukiHook configurations
        with(subproject) {
            // Apply common plugins if not already applied
            pluginManager.apply("com.android.library")
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("org.lsposed.lsparanoid")

            // Configure Android settings
            extensions.configure<com.android.build.gradle.LibraryExtension> {
                compileSdk = 36

                defaultConfig {
                    minSdk = 33
                    targetSdk = 36

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro",
                        )
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_24
                    targetCompatibility = JavaVersion.VERSION_24
                }

                kotlinOptions {
                    jvmTarget = "24"
                    freeCompilerArgs = freeCompilerArgs +
                            listOf(
                                "-Xjvm-default=all",
                                "-opt-in=kotlin.RequiresOptIn",
                            )
                }
            }

            // Add YukiHook dependencies
            dependencies {
                // Xposed Framework - YukiHookAPI (Standardized)
                implementation(libs.bundles.xposed)

                // Legacy Xposed API (compatibility)
                implementation(files("${project.rootDir}/Libs/api-82.jar"))
                implementation(files("${project.rootDir}/Libs/api-82-sources.jar"))

                // Core Android dependencies
                implementation(libs.bundles.androidx.core)

                // Testing
                testImplementation(libs.junit)
                androidTestImplementation(libs.androidx.test.ext.junit)
                androidTestImplementation(libs.androidx.test.espresso.core)
            }

            // Configure LSParanoid
            extensions.configure<org.lsposed.lsparanoid.gradle.ParanoidExtension> {
                seed = 0x2A // Consistent seed across all modules
                includeAsSharedUuid = true
            }

            // Configure KSP
            extensions.configure<com.google.devtools.ksp.gradle.KspExtension> {
                arg("YUKIHOOK_PACKAGE_NAME", project.group.toString())
            }
        }
    }
}

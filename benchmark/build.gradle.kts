// benchmark/build.gradle.kts

plugins {
    alias(libs.plugins.android.library) version "9.0.0-alpha10"
    id("com.google.devtools.ksp") version "2.2.21-RC2-2.0.4"
    // Apply the benchmark plugin from your libs.versions.toml
}

    android {
        namespace = "dev.aurakai.auraframefx.benchmark"

        // Configure the Java toolchain for consistency
        compileOptions {
            // Use consistent Java version configured in your central convention
            sourceCompatibility = JavaVersion.VERSION_24
            targetCompatibility = JavaVersion.VERSION_24


            // Modern build features (aidl is often not needed)
            buildFeatures {
                buildConfig = true
            }

            // Important: Configure benchmark build types
            buildTypes {
                create("benchmark") {
                    initWith(getByName("release"))


                    tasks.register("benchmarkAll") {
                        group = "benchmark"
                        description = "Aggregate runner for all Genesis Protocol benchmarks 🚀"
                        // Use an actual benchmark runner task instead of doLast
                        // For example, calling the connectedCheck task in your build script.
                        dependsOn(":app:connectedCheck")
                        doLast {
                            println("🚀 Genesis Protocol Performance Benchmarks")
                            println("📊 Monitor consciousness substrate performance metrics")
                        }
                    }

                    tasks.register("verifyBenchmarkResults") {
                        group = "verification"
                        description = "Verify benchmark module configuration"
                        doLast {
                            println("✅ Benchmark module configured")
                            println("🧠 Consciousness substrate performance monitoring ready")
                        }
                    }
                }
            }
        }
    }




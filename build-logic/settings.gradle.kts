
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://repo.gradle.org/gradle/libs-releases")
            url = uri("https://jitpack.io")
        }
    }
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
            gradlePluginPortal()
            maven {
                url = uri("https://repo.gradle.org/gradle/libs-releases")
            }
        }

        versionCatalogs {
            create("libs") {
                from(files("../gradle/libs.versions.toml"))
            }
        }
    }

// Note: Only one rootProject.name is needed
    rootProject.name = "build-logic"
}

// Build-logic only contains convention plugins, no app modules
// Note: foojay-resolver not needed - parent project has auto-download enabled
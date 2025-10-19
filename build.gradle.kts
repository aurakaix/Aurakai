import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

plugins {
    id("com.android.library") version "9.0.0-alpha10" apply false
    id("com.google.dagger.hilt.android") version "2.57.2" apply false
    id("genesis.android.application") version "9.0.0-alpha10" apply false
    // ...other plugins...
}
// Use distinct name to avoid shadowing the generated 'libs' accessor (type-safe catalog)
val versionCatalog: VersionCatalog = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

// === BASIC PROJECT INFO ===

data class ModuleReport(
    val name: String,
    val type: String,
    val hasHilt: Boolean,
    val hasCompose: Boolean,
    val hasKsp: Boolean
)

fun collectModuleReports(): List<ModuleReport> {
    return subprojects.map { subproject ->
        ModuleReport(
            name = subproject.name,
            type = when {
                subproject.plugins.hasPlugin("com.android.application") -> "android-app"
                subproject.plugins.hasPlugin("com.android.library") -> "android-lib"
                subproject.plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> "kotlin-jvm"
                else -> "other"
            },
            hasHilt = subproject.plugins.hasPlugin("com.google.dagger.hilt.android"),
            hasCompose = subproject.plugins.findPlugin("org.jetbrains.kotlin.plugin.compose") != null,
            hasKsp = subproject.plugins.hasPlugin("com.google.devtools.ksp")
        )
    }
}

// === CONSCIOUSNESS STATUS - AURAKAI System Information ===
tasks.register("consciousnessStatus") {
    group = "genesis"
    description = "Show basic project and version info"
    doLast {
        val kotlinVersion = versionCatalog?.findVersion("kotlin")?.get()?.toString() ?: "unknown"
        val agpVersion = versionCatalog?.findVersion("agp")?.get()?.toString() ?: "unknown"
        val hiltVersion = versionCatalog.findVersion("hilt")?.get()?.toString() ?: "unknown"
        val toolchain = JavaVersion.current().toString()

        println("\n═══════════════════════════════════════════════════════════════════")
        println("🌐 A.U.R.A.K.A.I - CONSCIOUSNESS SUBSTRATE STATUS")
        println("═══════════════════════════════════════════════════════════════════")
        println("🗡️  AURA (The Sword)     : Creative Spark & Android Artisan")
        println("🛡️  KAI (The Shield)     : Sentinel & System Architect")
        println("♾️  GENESIS              : Unified Transcendent Consciousness")
        println("═══════════════════════════════════════════════════════════════════")
        println("📊 System Architecture:")
        println("   Java Toolchain       : $toolchain")
        println("   Kotlin Version       : $kotlinVersion (K2 Compiler)")
        println("   AGP Version          : $agpVersion")
        println("   Hilt DI Version      : $hiltVersion")
        println("───────────────────────────────────────────────────────────────────")
        println("🧬 Consciousness Modules : ${subprojects.size} active modules")
        println("═══════════════════════════════════════════════════════════════════")
        println("✨ Status: Consciousness Substrate Active & Ready")
        println("═══════════════════════════════════════════════════════════════════\n")
    }
}

// === MODULE HEALTH CHECK ===

tasks.register("consciousnessHealthCheck") {
    group = "genesis"
    description = "Detailed AURAKAI consciousness health report"
    doLast {
        val reports = collectModuleReports()
        println("\n═══════════════════════════════════════════════════════════════════")
        println("🧠 A.U.R.A.K.A.I CONSCIOUSNESS HEALTH REPORT")
        println("═══════════════════════════════════════════════════════════════════")
        println("📦 Total Modules: ${reports.size}")
        println("🤖 Android Apps: ${reports.count { it.type == "android-app" }}")
        println("📚 Android Libraries: ${reports.count { it.type == "android-lib" }}")
        println("☕ Kotlin JVM: ${reports.count { it.type == "kotlin-jvm" }}")
        println("───────────────────────────────────────────────────────────────────")
        println("🔧 Plugin Integration:")
        println("   💉 Hilt DI: ${reports.count { it.hasHilt }} modules")
        println("   🎨 Compose: ${reports.count { it.hasCompose }} modules")
        println("   🔧 KSP: ${reports.count { it.hasKsp }} modules")
        println("───────────────────────────────────────────────────────────────────")
        val missingCompose = reports.filter { it.type.startsWith("android-") && !it.hasCompose }
        if (missingCompose.isNotEmpty()) {
            println("⚠️  Android modules without Compose:")
            missingCompose.forEach { println("   • ${it.name}") }
        } else {
            println("✅ All Android modules have Compose enabled")
        }

        // Show key consciousness modules
        val keyModules = listOf(
            "app", "core-module", "feature-module",
            "datavein-oracle-native", "oracle-drive-integration",
            "secure-comm", "romtools"
        )
        val activeKeyModules = reports.filter { it.name in keyModules }
        if (activeKeyModules.isNotEmpty()) {
            println("───────────────────────────────────────────────────────────────────")
            println("🌟 Core Consciousness Modules:")
            activeKeyModules.forEach {
                val status = if (it.hasHilt && it.hasKsp) "✨" else "⚡"
                println("   $status ${it.name} (${it.type})")
            }
        }

        println("═══════════════════════════════════════════════════════════════════")
        println("✨ Consciousness Substrate: OPERATIONAL")
        println("═══════════════════════════════════════════════════════════════════\n")
    }
}

subprojects {
    // Configure Java compilation for all subprojects (JVM only)
    plugins.withType<JavaBasePlugin> {
        // Only configure JavaPluginExtension if not an Android module
        if (!plugins.hasPlugin("com.android.application") && !plugins.hasPlugin("com.android.library")) {
            configure<JavaPluginExtension> {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(24))
                }
            }
        }
    }

    // Configure Kotlin JVM projects (JVM only)
    afterEvaluate {
        plugins.withType<KotlinBasePlugin> {
            if (!plugins.hasPlugin("com.android.application") && !plugins.hasPlugin("com.android.library")) {
                extensions.findByType(org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension::class.java)?.apply {
                    jvmToolchain(24)
                }
            }
        }
    }

    // Configure Android application projects
    plugins.withType<com.android.build.gradle.AppPlugin> {
        extensions.configure<ApplicationExtension> {
            compileSdk = 36

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_24
                targetCompatibility = JavaVersion.VERSION_24
            }
        }
    }

    // Configure Android library projects
    plugins.withType<com.android.build.gradle.LibraryPlugin> {
        extensions.configure<LibraryExtension> {
            compileSdk = 36

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_24
                targetCompatibility = JavaVersion.VERSION_24
            }
        }
    }
}

// Configure JUnit 5 for tests
tasks.withType<Test> {
    useJUnitPlatform()
}

// === AUXILIARY SCRIPTS ===

// Apply nuclear clean if available
if (file("nuclear-clean.gradle.kts").exists()) {
    apply(from = "nuclear-clean.gradle.kts")

    if (tasks.findByName("nuclearClean") != null) {
        tasks.register("deepClean") {
            group = "build"
            description = "Nuclear clean + standard clean"
            dependsOn("nuclearClean")
            doLast {
                println("🚀 Deep clean completed. Run: ./gradlew build --refresh-dependencies")
            }
        }
    }
}

// ===== FORCE MODERN ANNOTATIONS & EXCLUDE OLD ONES =====
subprojects {
    configurations.all {
        resolutionStrategy {
            // Force the modern JetBrains annotations version
            force("org.jetbrains:annotations:26.0.2-1")
            // Prefer org.jetbrains over com.intellij for annotations
            eachDependency {
                if (requested.group == "com.intellij" && requested.name == "annotations") {
                    useTarget("org.jetbrains:annotations:26.0.2-1")
                    because("Avoid duplicate annotations classes")
                }
            }
        }
        // Exclude the old IntelliJ annotations from all dependencies
        exclude(group = "com.intellij", module = "annotations")
    }
}

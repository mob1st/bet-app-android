@file:Suppress("DSL_SCOPE_VIOLATION")

buildscript {
    dependencies {
        classpath(libs.plugin.gradle)
        classpath(libs.plugin.google)
        classpath(libs.plugin.crashlytics)
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.jvm) apply false
}

subprojects {
    apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)

    dependencies {
        detektPlugins(rootProject.libs.plugin.detekt.libraries)
        detektPlugins(rootProject.libs.plugin.detekt.twitter.compose)
    }

    ktlint {
        android.set(true)
        enableExperimentalRules.set(true)
        ignoreFailures.set(false)
        version.set(rootProject.libs.versions.ktlint.get())
        filter {
            exclude("**/generated/**", "**/build/**")
        }
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
        }
    }

    detekt {
        config = files("$rootDir/config/detekt.yml")
        ignoreFailures = false
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "1.8"
        reports {
            txt.required.set(false)
            xml.required.set(false)
            html.required.set(true)
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        kotlinOptions.apply {
            freeCompilerArgs = listOf("-Xcontext-receivers")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

val deletePreviousGitHook by tasks.registering(Delete::class) {
    group = "utils"
    description = "Deleting previous githook"

    val preCommit = "${rootProject.rootDir}/.git/hooks/pre-commit.sh"
    val prePush = "${rootProject.rootDir}/.git/hooks/pre-push.sh"
    if (file(preCommit).exists() && file(prePush).exists()) {
        delete(preCommit, prePush)
    }
}

val installGitHook by tasks.registering(Copy::class) {
    group = "utils"
    description = "Adding githook to local working copy, this must be run manually"
    dependsOn(deletePreviousGitHook)
    from("${rootProject.rootDir}/hooks/pre-commit.sh", "${rootProject.rootDir}/hooks/pre-push.sh")
    into("${rootProject.rootDir}/.git/hooks")
    rename {
        it.replace(".sh", "")
    }

    eachFile {
        fileMode = 0b111101101
    }
}

tasks.getByPath("app:preBuild").dependsOn(installGitHook)

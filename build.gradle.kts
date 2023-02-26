@file:Suppress("DSL_SCOPE_VIOLATION")

buildscript {
    extra.apply {
        set("compose_compiler_version", "1.4.3")
        set("compose_version", "1.4.0-beta02")
        set("kotlin_version", "1.8.0")
        set("koin_version", "3.3.3")
        set("koin_ksp_version", "1.1.1")
        set("kotest_version", "5.5.5")
        set("lifecycle_version", "2.6.0-rc01")
        set("arrow_version", "1.1.5")
        set("ksp_version", "1.8.10-1.0.9")
        set("accompaninst_version", "0.27.1")
    }
    dependencies {
        classpath(libs.plugin.gradle)
        classpath(libs.plugin.google)
        classpath(libs.plugin.crashlytics)
    }

}// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.serialization)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
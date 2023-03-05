@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(projects.morpheus.annotation)
    implementation(libs.ksp.symbol.processing)
    implementation(libs.kotlin.reflection)
    implementation(libs.bundles.kotlin.poet)
    testImplementation(libs.kotest)
}
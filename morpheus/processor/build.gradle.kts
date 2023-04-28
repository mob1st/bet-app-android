@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("commonSetup")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(projects.morpheus.annotation)
    implementation(libs.ksp.symbol.processing)
    implementation(libs.kotlin.reflection)
    implementation(libs.bundles.kotlin.poet)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotlin.compiling.test)
    testImplementation(projects.morpheus.annotation)
}

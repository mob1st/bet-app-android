@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("org.jetbrains.kotlin.jvm")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(projects.morpheus.annotation)
    implementation(libs.ksp.symbol.processing)
    implementation(libs.kotlin.reflection)
    implementation(libs.bundles.kotlin.poet)
    testImplementation(libs.kotest)
    testImplementation(libs.kotlin.compiling.test)
    testImplementation(projects.morpheus.annotation)
}

@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("org.jetbrains.kotlin.jvm")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "17"
    }
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

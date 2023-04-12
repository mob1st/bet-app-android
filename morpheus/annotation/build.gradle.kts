@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
}

dependencies {
    testImplementation(libs.kotest.runner)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

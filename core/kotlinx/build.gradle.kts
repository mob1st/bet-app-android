@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id("commonSetup")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    api(libs.kotlin.collections)
    api(libs.kotlin.coroutines)
    api(libs.kotlin.serialization.json)
    testImplementation(libs.bundles.unittest.kotlin)
}
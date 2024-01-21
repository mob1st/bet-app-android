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
    api(libs.kotlin.coroutines.core)
    api(libs.kotlin.serialization.json)
    api(libs.kotlin.datetime)
    testImplementation(libs.bundles.unittest.kotlin)
}

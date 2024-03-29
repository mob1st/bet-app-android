@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("com.android.library")
    kotlin("android")
    id("commonSetup")
    alias(libs.plugins.junit5)
}

android {
    namespace = "br.com.mob1st.tests.unit"
}

dependencies {
    implementation(libs.kotest.runner)
    implementation(libs.kotlin.test)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.unittest.android)
    implementation(libs.bundles.unittest.kotlin)
    implementation(libs.bundles.junit5)
    implementation(libs.android.test.arch)
    implementation(libs.kotlin.coroutines.test)
}

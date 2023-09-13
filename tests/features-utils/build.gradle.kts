@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("com.android.library")
    kotlin("android")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.tests.featuresutils"
}

dependencies {
    implementation(libs.kotest.runner)
    implementation(libs.kotlin.test)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.unittest.android)

    implementation(projects.core.androidx)
    implementation(projects.core.design)
    implementation(projects.core.state)
    implementation(projects.features.utils)
    implementation(libs.kotlin.coroutines.test)
}

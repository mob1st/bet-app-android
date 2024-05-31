@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    kotlin("android")
    alias(libs.plugins.junit5)
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.core.design"

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.android.core)
    implementation(libs.android.window)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.accompanist)
    implementation(libs.coil)
    implementation(libs.kotlin.collections)
    implementation(libs.timber)

    // debug only
    debugImplementation(libs.compose.tooling)

    testImplementation(libs.bundles.unittest.android)
    testImplementation(libs.bundles.unittest.kotlin)
    testImplementation(projects.tests.unit)
    androidTestImplementation(libs.bundles.android.test)
}

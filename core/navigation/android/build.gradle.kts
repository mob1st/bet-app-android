@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    kotlin("android")
    alias(libs.plugins.junit5)
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.twocents.core.navigation.android"

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(projects.core.navigation.commons)
    implementation(libs.android.core)
    implementation(libs.bundles.compose)
    implementation(libs.timber)

    // debug only
    debugImplementation(libs.compose.tooling)

    testImplementation(libs.bundles.unittest.android)
    testImplementation(libs.bundles.unittest.kotlin)
}

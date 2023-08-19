@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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
    implementation(libs.timber)

    // debug only
    debugImplementation(libs.compose.tooling)

    testImplementation(libs.kotest.runner)
    androidTestImplementation(libs.bundles.android.test)
}

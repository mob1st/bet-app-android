@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.core.androidx"

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(libs.android.core)
    implementation(libs.bundles.lifecycle)
    implementation(libs.timber)
    implementation(libs.datastore.preferences)

    testImplementation(libs.bundles.unittest.android)
    testImplementation(libs.bundles.unittest.kotlin)
    testImplementation(projects.tests.unit)
    androidTestImplementation(libs.bundles.android.test)
}

@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.junit5)
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.core.state"
}

dependencies {
    implementation(libs.android.core)
    implementation(libs.timber)
    implementation(libs.bundles.lifecycle)

    implementation(projects.morpheus.annotation)
    implementation(libs.bundles.arrow)

    testImplementation(libs.bundles.unittest.android)
}

@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("commonSetup")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "br.com.mob1st.features.finances.publicapi"
}

dependencies {
    // bundles
    implementation(libs.bundles.android)

    // standalone
    implementation(libs.compose.navigation)
    implementation(libs.timber)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.timber)

    implementation(projects.core.androidx)
    implementation(projects.core.design)
    implementation(projects.core.kotlinx)
    implementation(projects.core.state)

    dokkaPlugin(libs.plugin.dokka.android)
}

@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.features.auth.publicapi"
}

dependencies {
    // bundles
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.android)
    implementation(libs.compose.navigation)

    // standalone
    implementation(libs.timber)
    implementation(libs.kotlin.coroutines.core)
    implementation(project(mapOf("path" to ":core:navigation")))

    dokkaPlugin(libs.plugin.dokka.android)
}

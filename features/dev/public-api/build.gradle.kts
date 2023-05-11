@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.features.dev.publicapi"
}

dependencies {
    // bundles
    implementation(libs.bundles.android)

    // standalone
    implementation(libs.timber)
    implementation(libs.kotlin.coroutines)

    dokkaPlugin(libs.plugin.dokka.android)
}

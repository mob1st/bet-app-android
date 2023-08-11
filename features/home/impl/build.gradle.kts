@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.google.ksp)
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.features.home.impl"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    api(projects.features.home.publicApi)

    // bundles
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.android)
    implementation(libs.bundles.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.lifecycle)

    // standalone
    implementation(libs.coil)
    implementation(libs.kotlin.collections)
    implementation(libs.timber)

    implementation(projects.morpheus.annotation)
    implementation(projects.core.navigation)
    debugImplementation(libs.compose.manifest)
    debugImplementation(libs.compose.tooling)

    ksp(libs.koin.compiler)
    ksp(projects.morpheus.processor)

    dokkaPlugin(libs.plugin.dokka.android)

    testImplementation(libs.kotest.runner)
    testImplementation(libs.koin.test)

    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.compose.test)
    androidTestImplementation(libs.leakcanary.test)
}

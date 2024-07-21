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
    namespace = "br.com.mob1st.features.onboarding.impl"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    // bundles
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.android)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.lifecycle)

    // standalone
    implementation(libs.kotlin.collections)
    implementation(libs.timber)

    implementation(projects.core.design)
    implementation(projects.core.kotlinx)
    implementation(projects.core.observability)
    implementation(projects.core.state)
    implementation(projects.features.auth.publicApi)
    implementation(projects.features.dev.publicApi)
    implementation(projects.features.home.publicApi)
    implementation(projects.morpheus.annotation)

    debugImplementation(libs.compose.manifest)
    debugImplementation(libs.compose.tooling)

    ksp(libs.koin.compiler)
    ksp(projects.morpheus.processor)

    dokkaPlugin(libs.plugin.dokka.android)

    testImplementation(libs.bundles.unittest.android)

    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.compose.test)
    androidTestImplementation(libs.leakcanary.test)
}

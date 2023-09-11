@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.features.utils"
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
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)

    // standalone
    implementation(libs.timber)

    implementation(projects.core.design)
    implementation(projects.core.kotlinx)
    implementation(projects.core.observability)
    implementation(projects.core.state)

    debugImplementation(libs.compose.manifest)
    debugImplementation(libs.compose.tooling)

    dokkaPlugin(libs.plugin.dokka.android)

    testImplementation(libs.bundles.unittest.android)
    testImplementation(projects.tests.unit)

    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.compose.test)
    androidTestImplementation(libs.leakcanary.test)
}

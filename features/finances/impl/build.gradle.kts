@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    kotlin("android")
    alias(libs.plugins.junit5)
    id("app.cash.sqldelight")
    id("commonSetup")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "br.com.mob1st.features.finances.impl"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    api(projects.features.finances.publicApi)

    // bundles
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.android)
    implementation(libs.bundles.arrow)
    ksp(libs.arrow.optics.compiler)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.koin)

    // standalone
    implementation(projects.features.utils)
    implementation(projects.core.androidx)
    implementation(projects.core.database)
    implementation(projects.core.design)
    implementation(projects.core.kotlinx)
    implementation(projects.core.observability)
    implementation(projects.core.state)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.timber)
    implementation(libs.lottie)
    implementation(project(":features:builder:impl"))

    debugImplementation(libs.compose.manifest)
    debugImplementation(libs.compose.tooling)

    debugImplementation(libs.datastore.preferences)

    dokkaPlugin(libs.plugin.dokka.android)

    testImplementation(libs.bundles.unittest.android)
    testImplementation(projects.tests.featuresUtils)

    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.compose.test)
    androidTestImplementation(libs.leakcanary.test)
}

sqldelight {
    databases {
        create("TwoCentsDb") {
            packageName = "br.com.mob1st.features.finances.impl"
            dependency(projects.core.database)
        }
    }
}

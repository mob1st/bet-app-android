@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.google.ksp)
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    namespace = "br.com.mob1st.bet.features.home.impl"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    // bundles
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.android)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.serialization)

    // standalone
    implementation(libs.coil)
    implementation(libs.kotlin.collections)
    implementation(libs.timber)

    implementation(projects.morpheus.annotation)
    debugImplementation(libs.compose.manifest)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.leakcanary.debug)

    ksp(libs.koin.compiler)
    ksp(projects.morpheus.processor)

    dokkaPlugin(libs.plugin.dokka.android)

    testImplementation(libs.kotest)
    testImplementation(libs.koin.test)

    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.compose.test)
    androidTestImplementation(libs.leakcanary.test)
}

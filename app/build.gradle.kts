@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.google.ksp)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {

    namespace = "br.com.mob1st.bet"
    compileSdk = 33

    defaultConfig {
        applicationId = "br.com.mob1st.bet"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            enableUnitTestCoverage = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            configure<CrashlyticsExtension> {
                // speeds up the build times
                mappingFileUploadEnabled = false
            }
        }
        create("benchmark") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles("benchmark-rules.pro")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    sourceSets.getByName("main") {
        java.srcDir("src/main/java")
        java.srcDir("src/main/kotlin")
    }
    sourceSets.getByName("test") {
        java.srcDir("src/test/java")
        java.srcDir("src/test/kotlin")
    }

    applicationVariants.configureEach {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
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
    implementation(libs.bundles.arrow)

    // standalone
    implementation(libs.coil)
    implementation(libs.kotlin.collections)
    implementation(libs.timber)

    // projects
    implementation(projects.morpheus.annotation)
    implementation(projects.features.home.impl)

    implementation("androidx.profileinstaller:profileinstaller:1.3.0-rc01")

    // debug only
    debugImplementation(libs.compose.manifest)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.leakcanary.debug)

    // ksp
    ksp(libs.koin.compiler)
    ksp(libs.arrow.optics.compiler)
    ksp(projects.morpheus.processor)

    dokkaPlugin(libs.plugin.dokka.android)

    testImplementation(libs.kotest)
    testImplementation(libs.koin.test)

    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.compose.test)
    androidTestImplementation(libs.leakcanary.test)
}

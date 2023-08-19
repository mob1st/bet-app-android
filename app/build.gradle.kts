@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

import br.com.mob1st.buildsrc.common.AndroidProjectConfig
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
    compileSdk = AndroidProjectConfig.COMPILE_SDK
    namespace = "br.com.mob1st.bet"

    defaultConfig {
        applicationId = "br.com.mob1st.bet"
        minSdk = 24
        targetSdk = AndroidProjectConfig.COMPILE_SDK
        versionCode = 1
        versionName = "1.0"

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

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    applicationVariants.configureEach {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // bundles
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.android)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.arrow)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // standalone
    implementation(libs.coil)
    implementation(libs.kotlin.collections)
    implementation(libs.timber)
    implementation(libs.android.startup)
    implementation(libs.android.profileinstaller)
    implementation(libs.kotlin.serialization.json)

    // projects
    implementation(projects.morpheus.annotation)
    implementation(projects.libs.firebase)
    implementation(projects.core.kotlinx)
    implementation(projects.core.navigation)
    implementation(projects.features.home.impl)
    implementation(projects.features.onboarding.impl)

    // debug only
    debugImplementation(libs.compose.manifest)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.leakcanary.debug)

    // ksp
    ksp(libs.koin.compiler)
    ksp(libs.arrow.optics.compiler)
    ksp(projects.morpheus.processor)

    dokkaPlugin(libs.plugin.dokka.android)

    testImplementation(libs.kotest.runner)
    testImplementation(libs.koin.test)

    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.compose.test)
    androidTestImplementation(libs.leakcanary.test)
}

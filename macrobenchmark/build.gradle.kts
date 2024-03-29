@file:Suppress("UnstableApiUsage", "DSL_SCOPE_VIOLATION")

import br.com.mob1st.buildsrc.common.AndroidProjectConfig
import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("com.android.test")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "br.com.mob1st.macrobenchmark"
    compileSdk = AndroidProjectConfig.COMPILE_SDK

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    defaultConfig {
        minSdk = 24
        targetSdk = AndroidProjectConfig.COMPILE_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
    }

    testOptions {
        managedDevices {
            devices {
                create<ManagedVirtualDevice>("pixel6Api30").apply {
                    device = "Pixel 6"
                    apiLevel = 30
                    systemImageSource = "google-atd"
                }
            }
        }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(libs.bundles.android.test)
    implementation(libs.android.test.uiautomator)
    implementation(libs.android.test.macrobenchmark)
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmark"
    }
}

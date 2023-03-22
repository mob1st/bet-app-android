package br.com.mob1st.buildsrc.common

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion

internal fun BaseExtension.defaultSetup() {
    compileSdkVersion(33)
    buildToolsVersion("33.0.1")
    defaultConfig {
        minSdk = AndroidProjectConfig.MIN_SDK
        targetSdk = AndroidProjectConfig.TARGET_SDK
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

}

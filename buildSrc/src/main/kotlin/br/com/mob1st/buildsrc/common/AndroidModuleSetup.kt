package br.com.mob1st.buildsrc.common

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun BaseExtension.defaultSetup() {
    compileSdkVersion(33)
    buildToolsVersion("33.0.1")

    defaultConfig {
        minSdk = AndroidProjectConfig.MIN_SDK
        targetSdk = AndroidProjectConfig.TARGET_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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

internal fun Project.librarySetup(extension: LibraryExtension) {
    extension.libraryVariants.configureEach {
        project.kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

private val Project.`kotlin`: KotlinAndroidProjectExtension get() =
    (this as ExtensionAware).extensions.getByName("kotlin") as KotlinAndroidProjectExtension

private fun KotlinAndroidProjectExtension.`sourceSets`(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>): Unit =
    (this as ExtensionAware).extensions.configure("sourceSets", configure)


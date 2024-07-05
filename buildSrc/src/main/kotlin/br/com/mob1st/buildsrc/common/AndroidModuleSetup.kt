package br.com.mob1st.buildsrc.common

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun BaseExtension.defaultSetup() {
    compileSdkVersion(AndroidProjectConfig.COMPILE_SDK)
    buildToolsVersion(AndroidProjectConfig.BUILD_TOOLS)

    defaultConfig {
        minSdk = AndroidProjectConfig.MIN_SDK
        targetSdk = AndroidProjectConfig.COMPILE_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = AndroidProjectConfig.JAVA_VERSION
        targetCompatibility = AndroidProjectConfig.JAVA_VERSION
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

internal fun Project.librarySetup(extension: LibraryExtension) = extension.run {
    libraryVariants.configureEach {
        project.kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

private val Project.`kotlin`: KotlinAndroidProjectExtension
    get() =
        (this as ExtensionAware).extensions.getByName("kotlin") as KotlinAndroidProjectExtension

private fun KotlinAndroidProjectExtension.`sourceSets`(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>): Unit =
    (this as ExtensionAware).extensions.configure("sourceSets", configure)


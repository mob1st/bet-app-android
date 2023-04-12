package br.com.mob1st.buildsrc.common

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.TestExtension
import com.android.build.gradle.TestPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.gradle.kotlin.dsl.getByType

class CommonSetupPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.forEach { plugin ->
            when (plugin) {
                is AppPlugin -> {
                    target.logger.info("Configuring ${target.path} as an Android App module")
                    target.configureApp()
                }
                is LibraryPlugin -> {
                    target.logger.info("Configuring ${target.path} as an Android Library module")
                    target.configureAndroidLibrary()
                }
                is TestPlugin -> {
                    target.logger.info("Configuring ${target.path} as an Android Test module")
                    target.configureAndroidTest()
                }
                is KotlinBasePluginWrapper -> {
                    target.logger.info("Configuring ${target.path} as a Kotlin module")
                    target.configureKotlin()
                }
            }
        }
    }

    private fun Project.configureApp() {
        extensions.getByType<AppExtension>().defaultSetup()
    }

    private fun Project.configureAndroidLibrary() {
        extensions.getByType<LibraryExtension>().defaultSetup()
    }

    private fun Project.configureAndroidTest() {
        extensions.getByType<TestExtension>().defaultSetup()
    }

    private fun Project.configureKotlin() {
        extensions.getByType<JavaPluginExtension>().apply {
            toolchain {
                JavaLanguageVersion.of(17)
            }
        }
    }
}

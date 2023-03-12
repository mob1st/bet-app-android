package br.com.mob1st.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension


class JacocoReportPlugin : Plugin<Project> {

    private val Project.jacoco: JacocoPluginExtension
        get() = extensions.findByName("jacoco") as? JacocoPluginExtension
            ?: error("Not a Jacoco module: $name")

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(JacocoPlugin::class.java)
            jacoco.toolVersion = "0.8.8"
            tasks.withType<Test>().configureEach {
                configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }
            extra.set("limits", JacocoConstants.limits.toMutableMap())

            subprojects {
                afterEvaluate {
                    pluginManager.apply(JacocoSetupPlugin::class.java)
                }
            }
        }
    }

}

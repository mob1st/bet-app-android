package br.com.mob1st.buildsrc.jacoco

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport


class JacocoReportPlugin : Plugin<Project> {

    private val Project.jacoco: JacocoPluginExtension
        get() = extensions.findByName("jacoco") as? JacocoPluginExtension
            ?: error("Not a Jacoco module: $name")

    override fun apply(target: Project) {
        with(target) {
            logger.info("Applying JacocoReportPlugin to $path")
            pluginManager.apply(JacocoPlugin::class.java)
            tasks.withType<Test>().configureEach {
                configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }
            jacoco.toolVersion = "0.8.8"
            extra.set("limits", JacocoConstants.limits.toMutableMap())
            subprojects {
                afterEvaluate {
                    pluginManager.apply(JacocoSetupPlugin::class.java)
                }
            }
            tasks.register("jacocoFullReport", JacocoReport::class.java) {
                fullReport()
            }
        }
    }

    private fun JacocoReport.fullReport() {
        group = "Reporting"
        description = "Generate Jacoco full report"

        val jacocoSetup = JacocoReportSetup.newInstance(project)
        logger.info("Jacoco setup: ${jacocoSetup.jacocoReports.map { it.reportProjectName }}")
        dependsOn(*jacocoSetup.jacocoReports.toTypedArray())

        val source = project.files(*jacocoSetup.sourceDirectories.toTypedArray())

        additionalSourceDirs.setFrom(source)
        sourceDirectories.setFrom(source)

        classDirectories.setFrom(project.files(*jacocoSetup.classDirectories.toTypedArray()))
        executionData.setFrom(project.files(*jacocoSetup.executionsData.toTypedArray()))

        reports {
            html.required.set(true)
            html.outputLocation.set(project.layout.buildDirectory.dir("reports/jacoco/html"))
            xml.required.set(true)
            xml.outputLocation.set(project.layout.buildDirectory.file("reports/jacoco/jacocoFullReport.xml"))
        }
    }

}

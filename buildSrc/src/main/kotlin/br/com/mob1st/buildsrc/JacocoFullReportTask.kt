package br.com.mob1st.buildsrc

import com.android.build.gradle.internal.coverage.JacocoReportTask
import groovy.lang.Closure
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoProjectSetup private constructor(
    val jacocoReports: MutableList<JacocoReport> = mutableListOf(),
    val sourceDirectories: MutableList<ConfigurableFileCollection> = mutableListOf(),
    val classDirectories: MutableList<ConfigurableFileCollection> = mutableListOf(),
    val executionsData: MutableList<ConfigurableFileCollection> = mutableListOf(),
) {

    private fun add(jacocoReport: JacocoReport) {
        jacocoReports.add(jacocoReport)
        sourceDirectories.add(jacocoReport.sourceDirectories)
        classDirectories.add(jacocoReport.classDirectories)
        executionsData.add(jacocoReport.executionData)
    }


    companion object {
        fun newInstance(root: Project): JacocoProjectSetup {
            val projects = root.subprojects.filter { project ->
                project.buildFile.exists() && !JacocoConstants.coverageIgnoredModules.contains(project.path)
            }
            println("ptest projects ${projects.size}: ${projects.map { it.path }}")
            val jacocoProjectSetup = JacocoProjectSetup()
            projects.forEach { project ->

                project.tasks.withType(JacocoReport::class.java).forEach { jacocoReport ->
                    println("ptest jacocoReport: ${jacocoReport.name} for project ${project.path}")
                    jacocoProjectSetup.add(jacocoReport)
                }
            }
            return jacocoProjectSetup
        }
    }
}

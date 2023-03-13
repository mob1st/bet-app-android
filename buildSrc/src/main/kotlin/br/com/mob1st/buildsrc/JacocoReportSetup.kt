package br.com.mob1st.buildsrc

import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.testing.jacoco.tasks.JacocoReport

internal class JacocoReportSetup private constructor(
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
        fun newInstance(root: Project): JacocoReportSetup {
            val projects = root.subprojects.filter { project ->
                project.buildFile.exists() && !JacocoConstants.coverageIgnoredModules.contains(project.path)
            }
            val jacocoReportSetup = JacocoReportSetup()
            projects.forEach { project ->
                project.tasks.withType(JacocoReport::class.java).forEach { jacocoReport ->
                    jacocoReportSetup.add(jacocoReport)
                }
            }
            return jacocoReportSetup
        }
    }
}

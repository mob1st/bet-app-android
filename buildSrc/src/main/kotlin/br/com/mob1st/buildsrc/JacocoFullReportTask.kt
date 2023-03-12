package br.com.mob1st.buildsrc

import com.android.build.gradle.internal.coverage.JacocoReportTask
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoFullReportTask : JacocoReport() {

    init {
        group = "Reporting"
        description = "Generate Jacoco full report"

        val jacocoSetup = JacocoProjectSetup.newInstance(project)

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

private class JacocoProjectSetup private constructor(
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
                project.buildFile.exists() && JacocoConstants.coverageIgnoredModules.contains(project.path)
            }
            val jacocoProjectSetup = JacocoProjectSetup()
            projects.forEach { project ->
                println("project $project")
                project.tasks.withType(JacocoReport::class.java).forEach { jacocoReport ->
                    jacocoProjectSetup.add(jacocoReport)
                }
            }
            return jacocoProjectSetup
        }
    }
}

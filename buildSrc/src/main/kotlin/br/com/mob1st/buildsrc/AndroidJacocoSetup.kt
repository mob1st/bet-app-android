package br.com.mob1st.buildsrc

import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Locale

internal class AndroidJacocoSetup(
    root: Project
) : Action<Project> {

    private val jacocoTestReport = root.tasks.register("jacocoTestReport")

    override fun execute(project: Project) {
        project.logger.info("Applying set up for Android module to ${project.path}")
        val androidJacocoTask = project.tasks.register("androidTestCoverage", JacocoReport::class.java) {
            config(project)
        }
        jacocoTestReport.dependsOn(androidJacocoTask)
    }

    private fun JacocoReport.config(project: Project) {
        dependsOn("testDebugUnitTest")
        reports {
            html.required.set(true)
            xml.required.set(true)
        }

        val javaTree = project.fileTree(
            "${project.buildDir}/intermediates/javac/debug",
        ) { exclude(JacocoConstants.excludedFiles) }

        val kotlinTree = project.fileTree(
            "${project.buildDir}/tmp/kotlin-classes/debug",
        ) { exclude(JacocoConstants.excludedFiles) }


        val coverageSrcDirectories = listOf(
            "${project.projectDir}/src/main/java",
            "${project.projectDir}/src/main/kotlin"
        )

        classDirectories.setFrom(project.files(javaTree, kotlinTree))
        additionalClassDirs.setFrom(project.files(coverageSrcDirectories))
        sourceDirectories.setFrom(project.files(coverageSrcDirectories))
        executionData.setFrom(
            project.files("${project.buildDir}/jacoco/testDebugUnitTest.exec")
        )
    }
}

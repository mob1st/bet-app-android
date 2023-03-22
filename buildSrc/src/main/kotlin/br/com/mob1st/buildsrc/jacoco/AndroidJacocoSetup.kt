package br.com.mob1st.buildsrc.jacoco

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.testing.jacoco.tasks.JacocoReport

internal object AndroidJacocoSetup : Action<Project> {



    override fun execute(project: Project) {
        project.logger.info("Applying set up for Android module to ${project.path}")
        project.tasks.register("jacocoTestReport", JacocoReport::class.java) {
            config(project)
        }
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

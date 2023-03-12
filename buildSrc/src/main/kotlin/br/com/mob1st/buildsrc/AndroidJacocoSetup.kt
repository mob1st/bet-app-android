package br.com.mob1st.buildsrc

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Locale

internal class AndroidJacocoSetup(private val root: Project) : Action<Project> {

    private val jacocoTestReport = root.tasks.register("jacocoTestReport")

    private val Project.android: BaseExtension
        get() = extensions.findByName("android") as? BaseExtension
            ?: error("Not an Android module: $name")

    override fun execute(project: Project) {
        val buildTypes = project.android.buildTypes.map { type -> type.name }
        val productFlavors = project.android.productFlavors.map { flavor -> flavor.name }.ifEmpty {
            listOf("")
        }
        productFlavors.forEach { flavorName ->
            buildTypes.forEach { buildTypeName ->
                jacocoTestReport.dependsOn(
                    project.forVariant(flavorName, buildTypeName)
                )
            }
        }
    }

    private fun Project.forVariant(flavorName: String, buildTypeName: String): TaskProvider<JacocoReport> {
        val (sourceName, sourcePath) = if(flavorName.isEmpty()) {
            buildTypeName to buildTypeName
        } else {
            "${flavorName}${buildTypeName.capitalize(Locale.ENGLISH)}" to "$flavorName/$buildTypeName"
        }
        val testTaskName = "test${sourceName.capitalize(Locale.ENGLISH)}UnitTest"
        return tasks.register("${testTaskName}Coverage", JacocoReport::class.java) {
            registerReporting(
                BuildVariantInfo(
                    testTaskName = testTaskName,
                    sourceName = sourceName,
                    sourcePath = sourcePath,
                    flavorName = flavorName,
                    buildTypeName = buildTypeName,
                )
            )
        }
    }

    private fun JacocoReport.registerReporting(
        info: BuildVariantInfo
    ) {
        dependsOn(info.testTaskName)

        group = JacocoConstants.taskGroup
        description = "Generate Jacoco coverage reports for the ${info.sourceName.capitalize(Locale.ENGLISH)} build."
        val javaDirectories = project.fileTree(
            "${project.buildDir}/intermediates/javac/${info.sourcePath}"
        ) { exclude(JacocoConstants.excludedFiles) }

        val kotlinDirectories = project.fileTree(
            "${project.buildDir}/tmp/kotlin-classes/${info.sourcePath}"
        ) { exclude(JacocoConstants.excludedFiles) }

        val coverageSrcDirectories = listOf(
            "src/main/java",
            "src/${info.flavorName}/java",
            "src/${info.buildTypeName}/java",
            "src/main/kotlin",
            "src/${info.flavorName}/kotlin",
            "src/${info.buildTypeName}/kotlin"
        )
        classDirectories.setFrom(project.files(javaDirectories, kotlinDirectories))
        additionalClassDirs.setFrom(project.files(coverageSrcDirectories))
        sourceDirectories.setFrom(project.files(coverageSrcDirectories))
        executionData.setFrom(
            project.files("${project.buildDir}/jacoco/${info.testTaskName}.exec")
        )

        reports {
            html.required.set(true)
            xml.required.set(true)
        }
    }
}

private data class BuildVariantInfo(
    val testTaskName: String,
    val sourceName: String,
    val sourcePath: String,
    val flavorName: String,
    val buildTypeName: String
)

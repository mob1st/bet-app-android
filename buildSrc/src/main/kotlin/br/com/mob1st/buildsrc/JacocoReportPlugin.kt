package br.com.mob1st.buildsrc

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension


class JacocoReportPlugin : Plugin<Project> {


    private val Project.android: BaseExtension
        get() = extensions.findByName("android") as? BaseExtension
            ?: error("Not an Android module: $name")

    private val Project.jacoco: JacocoPluginExtension
        get() = extensions.findByName("jacoco") as? JacocoPluginExtension
            ?: error("Not a Jacoco module: $name")

    override fun apply(target: Project) {
        with(target) {
            plugins.apply("jacoco")
            extra.set("limits", JacocoConstants.limits.toMutableMap())
            val setup = JacocoSetupFactory.create(this)
            afterEvaluate(setup)
        }
    }

}

fun Project.isAndroidModule(): Boolean {
    return plugins.hasPlugin("com.android.library") || plugins.hasPlugin("com.android.application")
}

fun Project.hasBuild() = buildFile.exists()

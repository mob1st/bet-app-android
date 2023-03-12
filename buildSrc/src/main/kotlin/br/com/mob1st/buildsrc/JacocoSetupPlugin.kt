package br.com.mob1st.buildsrc

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testing.jacoco.plugins.JacocoPlugin

class JacocoSetupPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(JacocoPlugin::class.java)
            val setup = when {
                isModuleExcluded() -> {
                    DummySetup
                }
                isAndroidModule() -> {
                    println("project $path is an android module")
                    AndroidJacocoSetup(this)
                }
                else -> {
                    println("project $path is a kotlin module")
                    KotlinJacocoSetup
                }
            }
            afterEvaluate(setup)
        }
    }

    private fun Project.isModuleExcluded(): Boolean {
        return !buildFile.exists() || JacocoConstants.coverageIgnoredModules.contains(path)
    }

    private fun Project.isAndroidModule(): Boolean {
        println(
            "project $path has plugins: ${plugins.map { it.javaClass.simpleName }}"
        )
        return plugins.hasPlugin("com.android.library") ||
                plugins.hasPlugin("com.android.application")
    }


    private object DummySetup : Action<Project> {
        override fun execute(project: Project)  {
            println("Module ${project.path} is excluded from coverage evaluation.")
        }
    }

}
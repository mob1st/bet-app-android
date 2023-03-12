package br.com.mob1st.buildsrc

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension

class JacocoSetupPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            logger.info("Applying JacocoSetupPlugin to $path")
            pluginManager.apply(JacocoPlugin::class.java)

            val setup = when {
                isModuleExcluded() -> {
                    DummySetup
                }
                isAndroidModule() -> {
                    logger.info("project $path is an android module")
                    AndroidJacocoSetup
                }
                else -> {
                    logger.info("project $path is a kotlin module")
                    KotlinJacocoSetup
                }
            }
            afterEvaluate(setup)
        }
    }


    private object DummySetup : Action<Project> {
        override fun execute(project: Project)  {
            println("Module ${project.path} is excluded from coverage evaluation.")
        }
    }

}

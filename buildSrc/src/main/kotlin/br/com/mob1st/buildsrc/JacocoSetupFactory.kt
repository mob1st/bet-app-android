package br.com.mob1st.buildsrc

import org.gradle.api.Action
import org.gradle.api.Project

internal object JacocoSetupFactory {

    fun create(project: Project): Action<Project> = project.run {
        when {
            isModuleExcluded() -> DummySetup
            isAndroidModule() -> AndroidJacocoSetup
            else -> KotlinJacocoSetup
        }
    }


    private fun Project.isModuleExcluded(): Boolean {
        return buildFile.exists() || JacocoConstants.coverageIgnoredModules.contains(path)
    }

    private fun Project.isAndroidModule(): Boolean {
        return plugins.hasPlugin("com.android.library") ||
                plugins.hasPlugin("com.android.application")
    }

    private object DummySetup : Action<Project> {
        override fun execute(project: Project)  {
            println("Module ${project.path} is excluded from coverage evaluation.")
        }
    }

}

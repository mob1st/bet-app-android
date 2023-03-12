package br.com.mob1st.buildsrc

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.tasks.JacocoReport

internal object KotlinJacocoSetup: Action<Project> {

    private const val TASK_NAME = "jacocoTestReport"

    override fun execute(project: Project) {
        project.tasks.withType(Test::class.java).configureEach {
            finalizedBy(TASK_NAME)
        }
        project.tasks.register(TASK_NAME, JacocoReport::class.java) {
            dependsOn("test")

            group = JacocoConstants.taskGroup
            description = "Generates code coverage report for the test task."

            reports {
                html.required.set(true)
                xml.required.set(true)
            }
        }
    }

}

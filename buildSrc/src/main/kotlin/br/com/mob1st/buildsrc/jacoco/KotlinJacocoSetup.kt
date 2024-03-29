package br.com.mob1st.buildsrc.jacoco

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.tasks.JacocoReport

internal object KotlinJacocoSetup : Action<Project> {

    private const val TASK_NAME = "jacocoTestReport"

    override fun execute(project: Project) {
        project.logger.info("Applying set for Kotlin module to ${project.path}")
        project.tasks.withType(Test::class.java).configureEach {
            finalizedBy(TASK_NAME)
        }
        project.tasks.withType(JacocoReport::class.java).configureEach {
            dependsOn("test")
            reports {
                html.required.set(true)
                xml.required.set(true)
            }
        }
    }

}

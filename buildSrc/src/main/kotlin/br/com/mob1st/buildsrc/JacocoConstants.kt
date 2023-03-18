package br.com.mob1st.buildsrc

import org.gradle.api.Project


object JacocoConstants {

    const val taskGroup = "Reporting"

    val excludedFiles = mutableSetOf(
        "**/R.class",
        "**/R$*.class",
        "**/*\$ViewInjector*.*",
        "**/*\$ViewBinder*.*",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*_MembersInjector*",
        "**android**",
        "**/BR.class",
        "**/*View.*",
        "**/*Application.*",
        "**/*App.*",
        "**/*Activity*.*",
        "**/*Fragment*.*",
        "**/*Initializer.*",
        "**/*Screen.*",
        "**/ui/ds/**",
    )

    val limits = mapOf<String, Double>(
        "instruction" to 0.0,
        "branch" to 0.0,
        "line" to 0.0,
        "complexity" to 0.0,
        "method" to 0.0,
        "class" to 0.0
    )

    val coverageIgnoredModules = mutableSetOf(
        ":testing:test-utils",
        ":morpheus:annotation"
    )
    fun excludedFiles(): List<String> {
        return excludedFiles.toList()
    }
}

fun excludedFiles(): List<String> {
    return JacocoConstants.excludedFiles.toList()
}

fun Project.isModuleExcluded(): Boolean {
    return !buildFile.exists() || JacocoConstants.coverageIgnoredModules.contains(path)
}

fun Project.isAndroidModule(): Boolean {
    return plugins.hasPlugin("com.android.library") ||
            plugins.hasPlugin("com.android.application")
}

package br.com.mob1st.buildsrc


internal object JacocoConstants {

    const val taskGroup = "Reporting"

    val excludedFiles = mutableSetOf(
        "**/R.class",
        "**/R$*.class",
        "**/*\$ViewInjector*.*",
        "**/*\$ViewBinder*.*",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Factory*",
        "**/*_MembersInjector*",
        "**/*Module*",
        "**/*Component*",
        "**android**",
        "**/BR.class",
        "**/*View.*",
        "**/*Application.*",
        "**/*App.*",
        "**/*Activity*.*",
        "**/*Fragment*.*",
        "**/*Initializer.*",
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

}

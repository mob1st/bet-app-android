package br.com.mob1st.features.dev.publicapi.domain

data class ProjectSettings(
    val backendEnvironment: BackendEnvironment,
    val buildInfo: BuildInfo,
)

enum class BackendEnvironment {
    STAGING,
    QA,
    PRODUCTION,
}

/**
 * Static information about the build config used to assemble the app.
 */
data class BuildInfo(
    /**
     * Static information about the app version.
     */
    val version: AppVersion,
    /**
     * Whether this build is a release build.
     */
    val isReleaseBuild: Boolean,
)

/**
 * Static information about the app version.
 */
data class AppVersion(
    /**
     * Gradle's versionName, used to allow users identify the app version. eg 1.0.0
     */
    val name: String,
    /**
     * Gradle's versionCode used to submit the app to Play store.
     */
    val code: Int,
)

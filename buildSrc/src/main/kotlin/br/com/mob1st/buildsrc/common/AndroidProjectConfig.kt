package br.com.mob1st.buildsrc.common

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

object AndroidProjectConfig {
    const val MIN_SDK = 24
    const val TARGET_SDK = 33
    val JAVA_VERSION = JavaVersion.VERSION_17
}

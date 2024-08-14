@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("com.android.library")
    kotlin("android")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.tests.featuresutils"
    packaging {
        resources.excludes.add("META-INF/**")
    }
    configurations.all {
        resolutionStrategy {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
        }
    }
}

dependencies {
    implementation(libs.kotest.runner)
    implementation(libs.kotlin.test)
    implementation(libs.timber)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.unittest.android)
    implementation(projects.core.androidx)
    implementation(projects.core.database)
    implementation(projects.core.design)
    implementation(projects.core.kotlinx)
    implementation(projects.core.observability)
    implementation(projects.core.state)
    implementation(projects.core.navigation.commons)
    implementation(projects.features.utils)
    implementation(libs.kotlin.coroutines.test)
    implementation(project(":features:finances:impl"))
}

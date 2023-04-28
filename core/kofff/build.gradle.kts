@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.core.kofff"
}

dependencies {
    implementation(libs.android.core)
    implementation(libs.workmanager.ktx)
    implementation(libs.timber)

    testImplementation(libs.kotest.runner)
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.workmanager.test)
}

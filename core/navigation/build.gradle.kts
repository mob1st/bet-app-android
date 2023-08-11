@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.core.navigation"
}

dependencies {
    implementation(libs.android.core)
    implementation(libs.timber)
    implementation(libs.compose.navigation)
}

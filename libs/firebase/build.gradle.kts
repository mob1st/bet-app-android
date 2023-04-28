@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.google.ksp)
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.libs.firebase"
}

dependencies {
    api(platform(libs.firebase.bom))
    api(libs.bundles.firebase)
    api(projects.core.observability)

    implementation(libs.android.core)
    implementation(libs.android.startup)
    implementation(libs.serialization.json)
    implementation(libs.timber)

    implementation(libs.bundles.lifecycle)

    ksp(libs.koin.compiler)
}

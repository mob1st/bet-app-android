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
    api(projects.core.observability)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.android.core)
    implementation(libs.android.startup)
    implementation(libs.koin.annotations)
    implementation(libs.koin.android)
    implementation(libs.timber)
    implementation(libs.kotlin.serialization.json)

    implementation(libs.bundles.lifecycle)

    ksp(libs.koin.compiler)
}

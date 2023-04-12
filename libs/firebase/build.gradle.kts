@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.libs.firebase"
}

dependencies {
    implementation(libs.android.core)
    implementation(libs.timber)
    implementation(libs.serialization.json)

    implementation(libs.bundles.lifecycle)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(projects.core.observability)

    testImplementation(libs.bundles.unittest.android)
    testImplementation(projects.tests.unit)
}

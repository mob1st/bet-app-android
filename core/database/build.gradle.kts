@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("com.android.library")
    kotlin("android")
    id("app.cash.sqldelight")
    id("commonSetup")
}

android {
    namespace = "br.com.mob1st.core.database"
}

dependencies {
    api(libs.bundles.sqldelight)
    implementation(libs.timber)
    testImplementation(libs.bundles.unittest.android)
    testImplementation(libs.bundles.unittest.kotlin)
}

sqldelight {
    databases {
        create("TwoCentsDb") {
            packageName = "br.com.mob1st.core.database"
        }
    }
}

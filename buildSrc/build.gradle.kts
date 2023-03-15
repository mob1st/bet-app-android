plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("jacocoReports") {
            id = "jacocoReports"
            implementationClass = "br.com.mob1st.buildsrc.JacocoReportPlugin"
        }
    }
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.plugin.gradle)
    implementation(libs.plugin.kotlin)
    compileOnly(gradleApi())
}

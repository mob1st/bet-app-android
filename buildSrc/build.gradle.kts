plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("jacocoReports") {
            id = "jacocoReports"
            implementationClass = "br.com.mob1st.buildsrc.jacoco.JacocoReportPlugin"
        }
        register("commonSetup") {
            id = "commonSetup"
            implementationClass = "br.com.mob1st.buildsrc.common.CommonSetupPlugin"
        }
    }
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    compileOnly(libs.plugin.gradle)
    compileOnly(libs.plugin.kotlin)
    compileOnly(gradleApi())
}

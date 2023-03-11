plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    testImplementation(libs.kotest)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Test> {
    useJUnitPlatform()
}

plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    testImplementation(libs.kotest)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

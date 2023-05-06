pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        mavenCentral()
        google()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Bet"
include(":app")
include(":morpheus:annotation")
include(":morpheus:processor")
include(":core:kofff")
include(":core:kotlinx")
include(":core:observability")
include(":core:state")
include(":features:auth:public-api")
include(":features:home:impl")
include(":features:onboarding:impl")
include(":libs:firebase")
include(":macrobenchmark")
include(":tests:unit")

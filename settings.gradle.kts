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
include(":core:androidx")
include(":core:design")
include(":core:kofff")
include(":core:kotlinx")
include(":core:navigation")
include(":core:observability")
include(":core:state")
include(":features:home:impl")
include(":features:home:public-api")
include(":features:onboarding:impl")
include(":features:auth:public-api")
include(":features:dev:impl")
include(":features:dev:public-api")
include(":features:utils")
include(":libs:firebase")
include(":macrobenchmark")
include(":tests:unit")

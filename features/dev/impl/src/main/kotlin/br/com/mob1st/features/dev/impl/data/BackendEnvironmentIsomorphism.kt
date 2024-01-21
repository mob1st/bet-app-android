package br.com.mob1st.features.dev.impl.data

import br.com.mob1st.core.kotlinx.monad.Morphism
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment

private const val QA = "qa"
private const val STAGING = "staging"
private const val PRODUCTION = "production"

internal fun backendEnvironmentIsomorphism(isReleaseBuild: Boolean) =
    domainToCacheMorphism() to cacheToDomainMorphism(isReleaseBuild)

private fun domainToCacheMorphism() = Morphism<BackendEnvironment, String?> {
    when (it) {
        BackendEnvironment.QA -> QA
        BackendEnvironment.PRODUCTION -> PRODUCTION
        BackendEnvironment.STAGING -> STAGING
    }
}

private fun cacheToDomainMorphism(isReleaseBuild: Boolean) = Morphism<String?, BackendEnvironment> {
    when (it) {
        QA -> BackendEnvironment.QA
        PRODUCTION -> BackendEnvironment.PRODUCTION
        STAGING -> BackendEnvironment.STAGING
        else -> if (isReleaseBuild) {
            BackendEnvironment.PRODUCTION
        } else {
            BackendEnvironment.STAGING
        }
    }
}

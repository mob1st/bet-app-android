package br.com.mob1st.features.dev.impl.data

import br.com.mob1st.core.kotlinx.monad.Isomorphism
import br.com.mob1st.core.kotlinx.monad.Morphism
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment

internal class BackendEnvironmentIsomorphism(
    private val isReleaseBuild: Boolean,
) : Isomorphism<BackendEnvironment, String?> {

    override fun invoke() = domainToCacheMorphism to cacheToDomainMorphism

    private val domainToCacheMorphism = Morphism<BackendEnvironment, String?> {
        when (it) {
            BackendEnvironment.QA -> QA
            BackendEnvironment.PRODUCTION -> PRODUCTION
            BackendEnvironment.STAGING -> STAGING
        }
    }

    private val cacheToDomainMorphism = Morphism<String?, BackendEnvironment> {
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

    companion object {
        private const val QA = "qa"
        private const val STAGING = "staging"
        private const val PRODUCTION = "production"
    }
}

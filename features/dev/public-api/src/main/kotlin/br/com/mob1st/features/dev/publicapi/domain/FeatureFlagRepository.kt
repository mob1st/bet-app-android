package br.com.mob1st.features.dev.publicapi.domain

import kotlinx.coroutines.flow.Flow

/**
 * Abstracts the access of data sources related to feature flags.
 */
interface FeatureFlagRepository {
    /**
     * Returns a [Flow] of [Boolean] that represents the state of the feature flag.
     * @param featureFlag the feature flag to be checked.
     */
    fun isEnabled(featureFlag: FeatureFlag): Flow<Boolean>

    /**
     * Syncs the different data sources of feature flags.
     */
    suspend fun sync()
}

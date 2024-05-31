package br.com.mob1st.features.dev.publicapi.domain

/**
 * Feature flag contract.
 *
 * Implement this interface to create a feature flag in the app.
 */
interface FeatureFlag {
    /**
     * Unique identifier of the feature flag.
     */
    val id: String

    /**
     * Name of the feature flag.
     */
    val name: String

    /**
     * Description of the feature flag.
     */
    val description: String

    /**
     * Indicates if the feature flag is enabled.
     */
    val isEnabled: Boolean
}

package br.com.mob1st.bet.features.ff

interface FeatureFlagRepository {
    /**
     * Sync the local cache with data provided by external sources
     */
    suspend fun sync()

    /**
     * Returns a boolean data class
     */
    fun getBoolean(featureFlag: String): Boolean
}

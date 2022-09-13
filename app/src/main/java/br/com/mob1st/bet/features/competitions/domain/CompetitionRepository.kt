package br.com.mob1st.bet.features.competitions.domain

/**
 * Manage and execute CRUD operations in the [Competition] entity and everything that depends on
 * it to exists
 */
interface CompetitionRepository {

    /**
     * Get the default [Competition] for new users
     */
    suspend fun getDefaultCompetition(): Competition

}
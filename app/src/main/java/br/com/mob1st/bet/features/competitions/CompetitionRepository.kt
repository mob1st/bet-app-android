package br.com.mob1st.bet.features.competitions

/**
 * Manage and execute CRUD operations in the [Competition] entity and everything that depends on
 * it to exists
 */
interface CompetitionRepository {

    /**
     * Get the default competition for new users
     */
    suspend fun getDefaultCompetition(): Competition

}
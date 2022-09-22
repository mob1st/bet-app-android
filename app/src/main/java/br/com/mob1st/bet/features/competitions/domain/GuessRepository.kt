package br.com.mob1st.bet.features.competitions.domain

/**
 * Manages CRUD operations under [Guess] aggregation and everything that depends on it
 */
interface GuessRepository {

    /**
     * Creates a new guess
     */
    suspend fun set(
        guess: Guess
    )

    suspend fun get(): List<Guess>

}
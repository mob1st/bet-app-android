package br.com.mob1st.features.auth.publicapi.domain

import kotlinx.coroutines.flow.Flow

/**
 * Abstract the access to the data sources related to the user's authentication.
 */
interface AuthRepository {
    /**
     * Returns the current status of the user's authentication.
     */
    val authStatus: Flow<AuthStatus>
}

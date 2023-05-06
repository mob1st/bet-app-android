package br.com.mob1st.features.onboarding.impl.ui

import br.com.mob1st.core.state.contracts.HelperClickManager
import br.com.mob1st.core.state.contracts.Navigable
import br.com.mob1st.core.state.contracts.SideEffectManager
import br.com.mob1st.core.state.contracts.StateOutputManager
import br.com.mob1st.features.onboarding.impl.domain.SplashDestination
import br.com.mob1st.morpheus.annotation.ConsumableEffect
import br.com.mob1st.morpheus.annotation.Morpheus
import javax.annotation.concurrent.Immutable

/**
 * Contract for communication between [LauncherPage] and the [LauncherViewModel].
 */
interface LauncherUiContract :
    StateOutputManager<LauncherUiState>,
    SideEffectManager<LauncherUiStateEffectKey>,
    HelperClickManager

/**
 * State of the [LauncherPage] provided by the [LauncherViewModel].
 * @param isLoading Indicates if the screen is loading.
 * @param navTarget Possible navigation targets from this screen.
 * @param errorMessage Possible error message to be shown.
 */
@Morpheus
@Immutable
data class LauncherUiState(
    val isLoading: Boolean = false,
    @ConsumableEffect
    override val navTarget: SplashDestination? = null,
    @ConsumableEffect
    val errorMessage: String? = null,
) : Navigable<SplashDestination>

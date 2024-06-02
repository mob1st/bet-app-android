package br.com.mob1st.features.onboarding.impl.ui

import br.com.mob1st.core.state.contracts.HelperClickManager
import br.com.mob1st.core.state.contracts.UiStateManager
import br.com.mob1st.features.onboarding.impl.domain.SplashDestination
import br.com.mob1st.morpheus.annotation.ConsumableEffect
import br.com.mob1st.morpheus.annotation.Morpheus
import javax.annotation.concurrent.Immutable

interface LauncherUiContract :
    UiStateManager<LauncherUiState>,
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
    val navTarget: LauncherNavTarget? = null,
    @ConsumableEffect
    val errorMessage: String? = null,
)

sealed class LauncherNavTarget : br.com.mob1st.core.design.atoms.properties.navigations.NavTarget() {
    object Home : LauncherNavTarget() {
        override val screenName: String = "home"
    }

    object Onboarding : LauncherNavTarget() {
        override val screenName: String = "init-onboarding"
    }

    companion object {
        fun of(splashDestination: SplashDestination) =
            when (splashDestination) {
                SplashDestination.Home -> Home
                SplashDestination.Onboarding -> Onboarding
            }
    }
}

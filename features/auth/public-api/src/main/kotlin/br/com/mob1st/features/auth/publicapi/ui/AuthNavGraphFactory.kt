package br.com.mob1st.features.auth.publicapi.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget

/**
 * Create the navigation graph for the authentication feature.
 */
interface AuthNavGraphFactory {
    /**
     * Create the navigation graph for the authentication feature.
     * @param navController The navigation controller.
     * @param initialTarget The initial target to be shown.
     * @param goToLoggedArea Callback to be called when successfully finishes the authentication process.
     */
    fun NavGraphBuilder.create(
        navController: NavController,
        initialTarget: AuthTarget = AuthTarget.Login,
        goToLoggedArea: () -> Unit,
    )
}

sealed class AuthTarget : NavTarget() {

    object PhoneNumberAccountCreation : AuthTarget() {
        override val screenName: String = "account-creation"
    }

    object Login : AuthTarget() {
        override val screenName: String = "login"
    }

    object ForgotPassword : AuthTarget() {
        override val screenName: String = "forgot-password"
    }
}

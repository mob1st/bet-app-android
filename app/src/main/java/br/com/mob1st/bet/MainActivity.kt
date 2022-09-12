package br.com.mob1st.bet

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import br.com.mob1st.bet.core.logs.Logger
import br.com.mob1st.bet.core.ui.compose.LocalActivity
import br.com.mob1st.bet.core.ui.ds.atoms.BetTheme
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.features.home.HomeScreen
import br.com.mob1st.bet.features.launch.LauncherViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val launcherViewModel by viewModel<LauncherViewModel>()
    private val logger by inject<Logger>()

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplash()
        super.onCreate(savedInstanceState)
        logger.d("The app has been started")
        setContent {
            CompositionLocalProvider(LocalActivity provides this) {
                BetTheme {
                    HomeScreen()
                }
            }
        }
        collectSplashState()
    }

    private fun initSplash() {
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition {
            launcherViewModel.currentState.loading
        }
        splash.setOnExitAnimationListener { splashScreenViewProvider ->
            animateEndOfSplash(splashScreenViewProvider)
        }
    }

    private fun animateEndOfSplash(provider: SplashScreenViewProvider) {
        val slideUp = ObjectAnimator.ofFloat(
            provider.iconView,
            View.TRANSLATION_Y,
            0f, -provider.iconView.height.toFloat()
        )
        slideUp.interpolator = AnticipateInterpolator()
        slideUp.duration = 1000L
        slideUp.doOnEnd {
            provider.remove()
        }
        slideUp.start()
    }

    private fun collectSplashState() {
        launcherViewModel
            .uiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                if (it.messages.isNotEmpty()) {
                    showErrorDialog(it.messages[0])
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun showErrorDialog(message: SimpleMessage): AlertDialog {
        return AlertDialog.Builder(this)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                finish()
            }
            .setMessage(message.descriptionResId)
            .setCancelable(false)
            .show()
    }


}


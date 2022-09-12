package br.com.mob1st.bet

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import br.com.mob1st.bet.core.ui.ds.atoms.BetTheme
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.features.home.HomeScreen
import br.com.mob1st.bet.features.launch.LauncherUiEvent
import br.com.mob1st.bet.features.launch.LauncherViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val launcherViewModel by viewModel<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplash()
        setContent {
            BetTheme {
                HomeScreen()
            }
        }
        collectSplashState()
    }

    private fun initSplash() {
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition {
            !launcherViewModel.uiState.value.data.finished
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
            .map { it.messages.firstOrNull() }
            .filterNotNull()
            .onEach {
                showErrorDialog(it)
            }
            .launchIn(lifecycleScope)
    }

    private fun showErrorDialog(message: SimpleMessage): AlertDialog {
        return AlertDialog.Builder(this)
            .setPositiveButton(R.string.try_again) { dialog, _ ->
                dialog.dismiss()
                launcherViewModel.fromUi(LauncherUiEvent.TryAgain(message))
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setMessage(message.descriptionResId)
            .setCancelable(false)
            .show()
    }


}
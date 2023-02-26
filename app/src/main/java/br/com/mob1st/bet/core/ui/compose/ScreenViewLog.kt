package br.com.mob1st.bet.core.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.analytics.ScreenDismissEvent
import br.com.mob1st.bet.core.analytics.ScreenViewEvent

/**
 * Helper function to call [ScreenViewLog]
 */
@Composable
fun ScreenViewLog(
    screenName: String,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    analyticsTool: AnalyticsTool = LocalAnalyticsTool.current
) {
    ScreenViewLog(
        screenView = ScreenViewEvent(screenName),
        lifecycleOwner = lifecycleOwner,
        analyticsTool = analyticsTool
    )
}

/**
 * Used to log the [ScreenViewEvent] and the [ScreenDismissEvent] on the [analyticsTool] when the
 * [lifecycleOwner] is started and stopped, respectively
 */
@Composable
fun ScreenViewLog(
    screenView: ScreenViewEvent,
    screenDismiss: ScreenDismissEvent = ScreenDismissEvent(screenView.screenName),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    analyticsTool: AnalyticsTool = LocalAnalyticsTool.current
) {
    val currentScreenView by rememberUpdatedState(screenView)
    val currentScreenDismiss by rememberUpdatedState(screenDismiss)

    DisposableEffect(lifecycleOwner, screenView, screenDismiss) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                analyticsTool.log(currentScreenView)
            } else if (event == Lifecycle.Event.ON_STOP) {
                analyticsTool.log(currentScreenDismiss)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

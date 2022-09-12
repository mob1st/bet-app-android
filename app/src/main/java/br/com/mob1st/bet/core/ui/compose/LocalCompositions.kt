package br.com.mob1st.bet.core.ui.compose

import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf
import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.logs.Logger

/**
 * Provides the instance of the current activity to the composable tree.
 *
 * It have to be initialized before call, because there is no default value. otherwise it will
 * trigger a IllegalStateException
 */
val LocalActivity = staticCompositionLocalOf<ComponentActivity> {
    error("CompositionLocal for LocalActivity not present")
}

/**
 * Provides the instance of analytics tool to the composable tree
 *
 * It have to be initialized before call, because there is no default value. otherwise it will
 * trigger a IllegalStateException
 */
val LocalAnalyticsTool = staticCompositionLocalOf<AnalyticsTool> {
    error("CompositionLocal for LocalAnalyticsTool not present")
}

/**
 * Provides the instance of the Logger to the composable tree
 *
 * It have to be initialized before call, because there is no default value. otherwise it will
 * trigger a IllegalStateException
 */
val LocalLogger = staticCompositionLocalOf<Logger> {
    error("CompositionLocal for LocalLogger not present")
}
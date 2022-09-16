package br.com.mob1st.bet.core.ui.compose

import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
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

/**
 * Provides the current theme used by the tree
 */
val LocalDarkTime = compositionLocalOf {
    false
}

/**
 * Provides the current snackbar host state of the composable tree
 */
val LocalSnackbarState = compositionLocalOf {
    SnackbarHostState()
}

val LocalLazyListState = compositionLocalOf {
    LazyListState()
}
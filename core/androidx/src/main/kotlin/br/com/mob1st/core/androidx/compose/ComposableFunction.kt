package br.com.mob1st.core.androidx.compose

import androidx.compose.runtime.Composable

/**
 * Helper type alias for a composable function.
 * It's useful for internal composable function declarations that can be nullable
 */
typealias ComposableFunction = @Composable () -> Unit

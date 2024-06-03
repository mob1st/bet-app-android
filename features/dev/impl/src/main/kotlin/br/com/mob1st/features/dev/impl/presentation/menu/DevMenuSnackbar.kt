package br.com.mob1st.features.dev.impl.presentation.menu

import androidx.compose.runtime.Immutable

@Immutable
sealed interface DevMenuSnackbar {
    @Immutable
    data object Todo : DevMenuSnackbar
}

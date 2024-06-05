package br.com.mob1st.features.home.impl.ui

import androidx.compose.runtime.Immutable

@Immutable
sealed interface HomeUiState {
    data object Empty : HomeUiState

    data class Loaded(
        val profile: Profile,
    ) : HomeUiState

    sealed interface Profile {
        @JvmInline
        value class User(val imageUrl: String) : Profile

        data object Guest : Profile
    }
}

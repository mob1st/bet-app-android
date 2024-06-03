package br.com.mob1st.features.home.impl.ui

import androidx.compose.runtime.Immutable

@Immutable
data class HomeUiState(
    val profile: Profile,
) {
    sealed interface Profile {
        @JvmInline
        value class User(val imageUrl: String) : Profile

        data object Guest : Profile
    }
}

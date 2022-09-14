package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.localization.LocalizedText

data class Team(
    val id: String,
    val name: LocalizedText,
    val logo: String
)

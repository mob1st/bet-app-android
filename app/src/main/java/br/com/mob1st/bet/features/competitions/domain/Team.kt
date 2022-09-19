package br.com.mob1st.bet.features.competitions.domain

import androidx.annotation.Keep
import br.com.mob1st.bet.core.localization.LocalizedText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class Team(
    @SerialName("ref")
    val id: String,
    val name: LocalizedText,
    val url: String
)

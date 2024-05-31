package br.com.mob1st.bet.core.ui.ds.organisms

import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.tooling.androidx.TextData
import br.com.mob1st.bet.core.ui.ds.molecule.ButtonState

data class HelperState(
    val title: TextData,
    val description: TextData,
    val cta: ButtonState.Primary?,
) {
    fun loading(loading: Boolean) =
        copy(
            cta = cta?.copy(loading = loading),
        )

    companion object {
        fun generalFailure(cta: ButtonState.Primary?) =
            HelperState(
                title = TextData(R.string.general_message_error_title),
                description = TextData(R.string.general_message_error_description),
                cta = cta,
            )
    }
}

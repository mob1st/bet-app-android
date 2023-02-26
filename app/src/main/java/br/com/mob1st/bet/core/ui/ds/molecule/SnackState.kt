package br.com.mob1st.bet.core.ui.ds.molecule

import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.tooling.androidx.TextData
import java.util.UUID

data class SnackState<T>(
    val id: T,
    val message: TextData,
    val action: TextData? = null
) {
    companion object {

        fun generalFailure(action: TextData? = null) = generalFailure(
            id = UUID.randomUUID(),
            action = action
        )

        fun <T> generalFailure(id: T, action: TextData?) = SnackState(
            id = id,
            message = TextData(R.string.general_message_error_snack),
            action = action
        )
    }
}

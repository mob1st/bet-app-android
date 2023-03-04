package br.com.mob1st.bet.core.ui.ds.molecule

import android.os.Parcelable
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.tooling.androidx.TextData
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnackState(
    val message: TextData,
    val action: TextData? = null
) : Parcelable {
    companion object {

        fun generalFailure(action: TextData? = null) = SnackState(
            message = TextData(R.string.general_message_error_snack),
            action = action
        )
    }
}

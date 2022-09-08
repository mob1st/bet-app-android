package br.com.mob1st.bet.core.ui

import androidx.annotation.StringRes
import br.com.mob1st.bet.R
import java.util.UUID

data class SimpleMessage(
    @StringRes val descriptionResId: Int,
    val id: Long = UUID.randomUUID().mostSignificantBits,
) {

    companion object {
        fun failure() = SimpleMessage(R.string.general_message_error_description)
    }
}

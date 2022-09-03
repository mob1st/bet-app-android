package br.com.mob1st.bet.core.ui

import androidx.annotation.StringRes
import br.com.mob1st.bet.R
import java.util.UUID

/**
 * A simple message to be displayed on UI, for dialogs, snackbars or helper screens.
 *
 * Use it for simple static texgts
 */
data class SimpleUiMessage(
    override val id: UUID = UUID.randomUUID(),
    @StringRes override val data: Int,
) : SingleShot<Int>

fun errorMessage() = SimpleUiMessage(
    data = R.string.general_message_error_description
)

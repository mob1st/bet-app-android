package br.com.mob1st.bet.core.ui.ds.organisms

import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.tooling.androidx.TextData
import br.com.mob1st.bet.core.ui.ds.molecule.ButtonState
import br.com.mob1st.bet.core.ui.ds.molecule.SnackState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

sealed interface PageState<T> {

    data class Empty<T>(val loading: Boolean = true) : PageState<T>

    data class Helper<T>(
        val title: TextData,
        val description: TextData,
        val cta: ButtonState?
    ) : PageState<T> {

        fun loading(loading: Boolean) = copy(
            cta = cta?.copy(loading = loading)
        )

        companion object {
            fun <T> generalFailure(cta: ButtonState? = null) = Helper<T>(
                title = TextData(R.string.general_message_error_title),
                description = TextData(R.string.general_message_error_description),
                cta = cta
            )
        }
    }

    data class Main<T>(
        val data: T,
        val snacks: PersistentList<SnackState<*>> = persistentListOf(),
        val refreshing: Boolean = false,
    ) : PageState<T> {

        fun offer(snack: SnackState<*>) = copy(snacks = snacks.add(snack))

        fun poll() = copy(snacks = snacks.removeAt(0))

        fun peek(): SnackState<*>? = snacks.getOrNull(0)

    }
}
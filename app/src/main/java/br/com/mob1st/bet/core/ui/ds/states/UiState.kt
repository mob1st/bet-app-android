package br.com.mob1st.bet.core.ui.ds.states

import androidx.lifecycle.ViewModel
import br.com.mob1st.bet.core.ui.ds.molecule.SnackState
import br.com.mob1st.bet.core.utils.extensions.putIf
import br.com.mob1st.bet.core.tooling.vm.update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow

data class UiState<T>(
    val mainContent: T? = null,
    val snacks: List<SnackState<*>> = emptyList(),
    val refreshing: Boolean = false
) {
    fun loading(value: Boolean) = copy(refreshing = value)

    fun push(snack: SnackState<*>) = copy(
        snacks = snacks.let {
            val list = snacks.toMutableList()
            list.putIf(snack) {
                it.id == snack.id
            }
            list.toList()
        }
    )

    fun success(value: T?) = copy(mainContent = value)

    fun dismiss(dismiss: SnackState<*>): UiState<T> {
        val mutableSnacks = snacks.toMutableList()
        dismiss.let {
            val index = mutableSnacks.indexOfFirst {
                it.id == dismiss.id
            }
            mutableSnacks.removeAt(index)
        }
        return copy(
            snacks = mutableSnacks.toList()
        )
    }
}

context(ViewModel)
fun <T> MutableStateFlow<UiState<T>>.updates(
    loading: Flow<Boolean> = emptyFlow(),
    push: Flow<SnackState<*>> = emptyFlow(),
    dismiss: Flow<SnackState<*>> = emptyFlow(),
    success: Flow<T>
) {
    update(loading) { currentState, newData ->
        currentState.loading(newData)
    }

    update(push) { currentState, newData ->
        currentState.push(newData)
    }

    update(dismiss) { currentState, newData ->
        currentState.dismiss(newData)
    }

    update(success) { currentState, newData ->
        currentState.success(newData)
    }
}
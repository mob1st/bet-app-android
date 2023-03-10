package br.com.mob1st.bet.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.bet.core.tooling.vm.actionFromFlow
import br.com.mob1st.bet.core.tooling.vm.trigger
import br.com.mob1st.bet.core.tooling.vm.update
import br.com.mob1st.morpheus.annotation.Consumable
import br.com.mob1st.morpheus.annotation.ConsumableEffect
import br.com.mob1st.morpheus.annotation.Morpheus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

@Morpheus
@kotlinx.serialization.Serializable
data class UiState(
    val content: String = "",
    val loading: Boolean = false,
    val errorMessage: List<String> = emptyList(),
    val loaded: Boolean = false,
    @ConsumableEffect
    val dialogContent: String? = null,
)
// CODE TO BE GENERATED

@Suppress("unused")
class MyViewModel : ViewModel(), Consumer<UiStateEffectKey> {

    private val consumableInput = MutableSharedFlow<Consumable<UiStateEffectKey, *>>()
    private val _uiState = MutableStateFlow(UiState())

    private val action = viewModelScope.actionFromFlow<String> {
        flowOf("")
    }

    private val clickAction = viewModelScope.actionFromFlow<String> {
        flowOf("")
    }

    init {
        // state
        _uiState.update(
            combine(
                action.success,
                action.loading,
                clickAction.loading
            ) { data, loading, clickLoading ->
                (loading || clickLoading) to data
            }
        ) { currentState, (loading, data) ->

            currentState.copy(content = data, loading = loading)
        }

        merge(
            merge(action.failure, clickAction.failure)
                .map { "error message" }
                .map {
                    UiState.serializer()
                },
            clickAction.success
                .map { true }
                .map {
                    UiState.serializer()
                }
        )

        // side effects
        _uiState.update(
            merge(action.failure, clickAction.failure).map { "error message" }
        ) { currentState, newData ->
            currentState.copy(dialogContent = newData)
        }
        _uiState.update(
            clickAction.success.map { true }
        ) { currentState, newData ->
            currentState.copy(loaded = newData)
        }

        // side effects clean
        _uiState.update(consumableInput) { currentState, newData ->
            currentState.clearEffect(newData)
        }

        action.trigger()
    }

    override suspend fun consume(consumable: Consumable<UiStateEffectKey, *>) {
        consumableInput.emit(consumable)
    }
}

interface Consumer<T> {
    suspend fun consume(consumable: Consumable<T, *>)
}

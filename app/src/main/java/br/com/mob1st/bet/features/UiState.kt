package br.com.mob1st.bet.features

<<<<<<< Updated upstream
data class UiState()
=======
import br.com.mob1st.morpheus.annotation.ConsumableEffect
import br.com.mob1st.morpheus.annotation.Morpheus
import br.com.mob1st.morpheus.annotation.strategy.QueueStrategy

@Morpheus
data class UiState(
    val content: String = "",
    @ConsumableEffect
    val errorMessage: List<String> = emptyList(),
    @ConsumableEffect
    val dialogContent: String? = null,
)

// CODE TO BE GENERATED
sealed class UiStateConsumableKey<T> {

    abstract val value: T

    data class ErrorMessage(
        override val value: List<String>,
    ) : UiStateConsumableKey<List<String>>()

    data class DialogContent(
        override val value: String?,
    ) : UiStateConsumableKey<String?>()
}

fun UiState.consume(
    consumableEffect: UiStateConsumableKey<*>
): UiState {
    return when (consumableEffect) {
        is UiStateConsumableKey.ErrorMessage -> copy(
            errorMessage = QueueStrategy<String>().consume(consumableEffect.value)
        )
        is UiStateConsumableKey.DialogContent -> copy(dialogContent = null)
    }
}

fun UiState.errorMessage(
    value: List<String>
) = UiStateConsumableKey.ErrorMessage(
    value = value,
)

fun UiState.dialogContent(
    value: String?
) = UiStateConsumableKey.DialogContent(
    value = value
)

>>>>>>> Stashed changes

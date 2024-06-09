package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.kotlinx.collections.update
import br.com.mob1st.features.twocents.builder.impl.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

/**
 * A debouncer that validates the amount field of a list of values.
 */
@OptIn(FlowPreview::class)
internal class ValidationDebouncer(
    private val validate: (Int, String) -> ValidationResult,
) {
    private val debouncers = hashMapOf<Int, MutableSharedFlow<ValidationRequest>>()

    /**
     * Creates a debouncer for a list of values.
     * @param count The number of values to be debounced.
     */
    fun create(count: Int) {
        for (position in 0 until count) {
            debouncers.putIfAbsent(position, MutableSharedFlow())
        }
    }

    /**
     * @return The results of the validation triggered by the [debounceValidation]
     */
    fun <T> results(list: PersistentList<BuilderUiState.ListItem<T>>) = mergeDebounce()
        .map { validationResult ->
            list.update(validationResult.position) {
                it.copy(
                    amount = it.amount,
                )
            }.toImmutableList()
        }

    private fun mergeDebounce() = debouncers.values.map { flow ->
        flow.debounce(DEBOUNCE_TIME)
            .map { validate(it.position, it.amount) }
            .distinctUntilChanged()
    }.merge()

    /**
     * Debounces the validation of a value.
     * @param validationRequest The value to be validated.
     */
    suspend fun debounceValidation(validationRequest: ValidationRequest) {
        debouncers
            .getOrPut(validationRequest.position) { MutableSharedFlow() }
            .emit(validationRequest)
    }

    companion object {
        private const val DEBOUNCE_TIME = 500L
    }
}

/**
 * A value change that will be validated after a proper debounce time.
 * @param position The position of the value in the list.
 * @param amount The value to be validated.
 */
internal data class ValidationRequest(
    val position: Int,
    val amount: String,
)

/**
 * The result of a validation.
 * @param position The position of the value in the list.
 * @param field The error found in the value.
 */
internal data class ValidationResult(
    val position: Int,
    val field: AmountError?,
) {
    /**
     * The type of error found in the amount field.
     */
    enum class AmountError {
        InvalidFormat,
    }
}

internal fun ValidationResult.AmountError.toResId(): Int {
    return when (this) {
        ValidationResult.AmountError.InvalidFormat -> R.string.builder_summary_button
    }
}

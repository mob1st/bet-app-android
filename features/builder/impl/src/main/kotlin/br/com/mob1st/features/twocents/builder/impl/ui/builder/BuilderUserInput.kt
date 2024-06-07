package br.com.mob1st.features.twocents.builder.impl.ui.builder

import android.os.Parcelable
import br.com.mob1st.core.kotlinx.structures.Id
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.parcelize.Parcelize

/**
 * The users input for the builder screen.
 * It's not part of the UI state, so no need to be immutable.
 * @param manuallyAdded The manually added categories.
 * @param suggested The suggested categories.
 */
@Parcelize
internal data class BuilderUserInput(
    val manuallyAdded: List<Entry> = emptyList(),
    val suggested: Map<Id, Entry> = emptyMap(),
) : Parcelable {
    /**
     * Category entry state. It can be a manually added category or a suggested category.
     * @param name The name of the category.
     * @param amount The value of the category.
     */
    @Parcelize
    internal data class Entry(
        val name: String = "",
        val amount: String = "",
    ) : Parcelable

    companion object {
        fun fromUiState(
            uiState: BuilderUiState,
            suggestions: List<CategorySuggestion>,
        ): BuilderUserInput {
            return BuilderUserInput(
                manuallyAdded = uiState.manuallyAdded.map { item ->
                    Entry(
                        name = item.name,
                        amount = item.amount.value,
                    )
                },
                suggested = uiState.manuallyAdded.mapIndexed { index, item ->
                    suggestions[index].id to Entry(
                        name = item.name,
                        amount = item.amount.value,
                    )
                }.toMap(),
            )
        }
    }
}

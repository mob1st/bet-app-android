package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.kotlinx.structures.Id
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryInput
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus

@Immutable
internal interface CategoryBuilderSection {
    val categories: ImmutableList<BuilderListItemState>

    fun applyUpdate(categorySheet: CategorySheetState): CategoryBuilderSection
}

@Immutable
internal data class ManualCategoryBuilderSection(
    override val categories: PersistentList<BuilderListItemState> = persistentListOf(),
) : CategoryBuilderSection {
    override fun applyUpdate(categorySheet: CategorySheetState): ManualCategoryBuilderSection {
        return copy(categories = categories.applyUpdate(categorySheet))
    }

    fun toManualEntryList(): List<BuilderUserInput.Entry> {
        return categories.map { item ->
            BuilderUserInput.Entry(
                name = item.input.name,
                amount = item.input.value.toString(),
            )
        }
    }
}

internal data class SuggestedCategoryBuilderSection(
    val suggestions: ImmutableList<CategorySuggestion> = persistentListOf(),
    override val categories: PersistentList<BuilderListItemState> = persistentListOf(),
) : CategoryBuilderSection {
    override fun applyUpdate(categorySheet: CategorySheetState): SuggestedCategoryBuilderSection {
        return copy(categories = categories.applyUpdate(categorySheet))
    }

    fun toSuggestedEntryMap(): Map<Id, BuilderUserInput.Entry> {
        return suggestions.zip(categories).associate { (suggestion, item) ->
            suggestion.id to BuilderUserInput.Entry(
                name = item.input.name,
                amount = item.input.value.toString(),
            )
        }
    }
}

@Immutable
internal data class BuilderListItemState(
    val input: CategoryInput,
) {
    val name: TextState = input.localizedName()
    val amount: String = input.value.toString()
    val shouldShowAmount: Boolean = input.value > Money.Zero
    val total: String? = input.calculateTotal()?.toString()
}

private fun PersistentList<BuilderListItemState>.applyUpdate(
    sheetState: CategorySheetState,
): PersistentList<BuilderListItemState> {
    return when (val operation = sheetState.operation) {
        CategorySheetState.Operation.Add -> this + BuilderListItemState(
            input = sheetState.input,
        )

        is CategorySheetState.Operation.Update -> set(
            operation.position,
            BuilderListItemState(
                input = sheetState.input,
            ),
        )
    }
}

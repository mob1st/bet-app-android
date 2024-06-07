package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryBatch
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryInput
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion

internal fun interface BuilderCategoryBatchFactory {
    fun create(
        suggestions: List<CategorySuggestion>,
        userInput: BuilderUserInput,
    ): CategoryBatch
}

internal fun BuilderCategoryBatchFactory(
    localizationProvider: CategoryNameLocalizationProvider,
): BuilderCategoryBatchFactory {
    return BuilderCategoryBatchFactoryImpl(localizationProvider)
}

private class BuilderCategoryBatchFactoryImpl(
    private val localizationProvider: CategoryNameLocalizationProvider,
) : BuilderCategoryBatchFactory {
    override fun create(
        suggestions: List<CategorySuggestion>,
        userInput: BuilderUserInput,
    ): CategoryBatch {
        val manuallyAdded = userInput.manuallyAdded.map { entry ->
            CategoryInput(
                name = entry.name,
                // TODO error handling
                value = Money(entry.amount.toInt()),
                linkedSuggestion = null,
            )
        }

        val suggested = suggestions.mapNotNull { suggestion ->
            userInput.suggested[suggestion.id]?.let { entry ->
                CategoryInput(
                    name = localizationProvider[suggestion.name],
                    // TODO error handling
                    value = Money(entry.amount.toInt()),
                    linkedSuggestion = suggestion,
                )
            }
        }
        return CategoryBatch(manuallyAdded + suggested)
    }
}

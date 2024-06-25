package br.com.mob1st.features.finances.impl.ui.builder

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategoryBuilder
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class CategoryBuilderUiState(
    val builder: CategoryBuilder? = null,
    val consumables: CategoryBuilderConsumables = CategoryBuilderConsumables(),
) {
    val manuallyAdded: ImmutableList<CategoryListItem> = builder?.manuallyAdded.orEmpty().map {
        ManualCategoryListItem(it)
    }.toImmutableList()

    val suggestions: ImmutableList<CategoryListItem> = builder?.suggestions.orEmpty().map {
        SuggestionListItem(it)
    }.toImmutableList()
}

@Immutable
sealed interface CategoryListItem {
    val leading: TextState
    val value: TextState?
    val supporting: TextState?
}

@Immutable
internal data class SuggestionListItem(
    val suggestion: CategorySuggestion,
) : CategoryListItem {
    override val leading: TextState
        get() = TODO("Not yet implemented")
    override val value: TextState?
        get() = TODO("Not yet implemented")
    override val supporting: TextState?
        get() = TODO("Not yet implemented")
}

@Immutable
internal data class ManualCategoryListItem(val category: Category) : CategoryListItem {
    override val leading: TextState
        get() = TODO("Not yet implemented")
    override val value: TextState?
        get() = TODO("Not yet implemented")
    override val supporting: TextState?
        get() = TODO("Not yet implemented")
}

@Immutable
internal data object AddCategoryListItem : CategoryListItem {
    override val leading: TextState
        get() = TODO("Not yet implemented")
    override val value: TextState? = null
    override val supporting: TextState? = null
}

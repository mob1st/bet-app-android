package br.com.mob1st.features.twocents.builder.impl.ui.builder

import android.content.Context
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.twocents.builder.impl.R
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryInput
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion

internal interface CategoryNameLocalizationProvider {
    operator fun get(name: CategorySuggestion.Name): String
}

internal fun CategoryNameLocalizationProvider(context: Context): CategoryNameLocalizationProvider {
    return CategoryNameLocalizationProviderImpl(context)
}

private class CategoryNameLocalizationProviderImpl(
    private val context: Context,
) : CategoryNameLocalizationProvider {
    override fun get(name: CategorySuggestion.Name): String {
        return context.getString(name.toResId())
    }
}

/**
 * Maps the [CategorySuggestion.Name] to the corresponding string resource id.
 */
internal fun CategorySuggestion.Name.toResId(): Int {
    return when (this) {
        CategorySuggestion.Name.RENT -> R.string.builder_fixed_expenses_suggestion_rent
        CategorySuggestion.Name.PARTY -> R.string.builder_summary_button
    }
}

internal fun CategoryInput.localizedName(): TextState {
    return if (linkedSuggestion != null) {
        TextState(linkedSuggestion.name.toResId())
    } else {
        TextState(name)
    }
}

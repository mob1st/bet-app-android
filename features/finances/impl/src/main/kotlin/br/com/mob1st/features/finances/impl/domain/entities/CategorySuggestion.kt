package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Uri

/**
 * Holds the static data of a category suggestion, used to provide pre-defined categories to the user.
 * @property name The localized name of the category.
 * @property image The image address of the category.
 */
data class CategorySuggestion(
    val name: String,
    val image: Uri,
)

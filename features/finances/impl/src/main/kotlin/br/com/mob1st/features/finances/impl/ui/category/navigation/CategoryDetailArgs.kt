package br.com.mob1st.features.finances.impl.ui.category.navigation

import android.os.Parcelable
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategoryDefaultValues
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
import br.com.mob1st.features.finances.impl.ui.category.navigation.CategoryDetailArgs.Intent
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Duplication of domain object [GetCategoryIntent].
 * It breaks that data structure into a [Parcelable] object in order to allow navigating between screens saving its
 * state through the process death.
 * @property intent The intent to be executed. It can be either [Intent.Edit] or [Intent.Create].
 * @property name The name of the category.
 * @property isExpense Whether the category is an expense or not.
 * @property recurrenceType The recurrence type of the category.
 */
@Parcelize
@Serializable
internal data class CategoryDetailArgs(
    val intent: Intent,
    val name: String,
    val isExpense: Boolean,
    val recurrenceType: RecurrenceType,
) : Parcelable {
    /**
     * Constructor that receives a [Category] object and maps it to the [CategoryDetailArgs] object.
     * It creates a edit intent.
     * @param category The category to be edited.
     */
    constructor(category: Category) : this(
        intent = Intent.Edit(category.id.value),
        name = category.name,
        isExpense = category.isExpense,
        recurrenceType = category.recurrences.asType(),
    )

    /**
     * Constructor that receives a [CategoryDefaultValues] object and maps it to the [CategoryDetailArgs] object.
     * It creates a create intent.
     * @param name The name of the category.
     * @param defaultValues The default values of the category.
     */
    constructor(
        name: String,
        defaultValues: CategoryDefaultValues,
    ) : this(
        intent = Intent.Create,
        name = name,
        isExpense = defaultValues.isExpense,
        recurrenceType = defaultValues.recurrenceType,
    )

    /**
     * Sealed interface that represents the possible intents of the [CategoryDetailArgs] object.
     * It can be either a edit intent or a create intent.
     */
    @Serializable
    sealed interface Intent : Parcelable {
        /**
         * Allows the edition of a existing category.
         * @property id The id of the category to be edited.
         */
        @Parcelize
        @Serializable
        data class Edit(val id: Long) : Intent

        /**
         * Allows the creation of a new category.
         */
        @Parcelize
        @Serializable
        data object Create : Intent
    }
}

/**
 * Maps the [CategoryDetailArgs] object to the [GetCategoryIntent] object.
 * @return The [GetCategoryIntent] object.
 */
internal fun CategoryDetailArgs.toIntent(): GetCategoryIntent {
    return when (intent) {
        is Intent.Edit -> GetCategoryIntent.Edit(
            id = Category.Id(intent.id),
            name = name,
        )

        is Intent.Create -> GetCategoryIntent.Create(
            name = name,
            defaultValues = CategoryDefaultValues(
                isExpense = isExpense,
                recurrenceType = recurrenceType,
            ),
        )
    }
}

package br.com.mob1st.features.finances.impl.ui.category.detail

import android.os.Parcelable
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.ui.utils.parcelers.MoneyParceler
import br.com.mob1st.features.finances.impl.ui.utils.parcelers.RecurrencesParceler
import br.com.mob1st.features.finances.impl.ui.utils.parcelers.UriParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

/**
 * Data edited by the user before the submission of the category detail screen.
 * It implements [Parcelable] to make the changes survive process death.
 * @property name The name of the category.
 * @property amount The amount of the category.
 * @property recurrences The recurrences of the category.
 * @property image The image of the category.
 */
@Parcelize
data class CategoryEntry(
    val name: String,
    val amount: @WriteWith<MoneyParceler> Money,
    val recurrences: @WriteWith<RecurrencesParceler> Recurrences,
    val image: @WriteWith<UriParceler> Uri,
) : Parcelable {
    /**
     * Creates a new [CategoryEntry] with the values from the given [category].
     * This is usually a way to create the initial values for the entry.
     * @param category The category to provide the initial values.
     */
    constructor(category: Category) : this(
        name = category.name,
        amount = category.amount,
        recurrences = category.recurrences,
        image = category.image,
    )
}

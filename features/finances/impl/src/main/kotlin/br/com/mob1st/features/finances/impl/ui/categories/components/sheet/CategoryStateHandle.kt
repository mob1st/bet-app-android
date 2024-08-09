package br.com.mob1st.features.finances.impl.ui.categories.components.sheet

import androidx.lifecycle.SavedStateHandle
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import kotlinx.coroutines.flow.combine

class CategoryStateHandle(
    private val savedStateHandle: SavedStateHandle,
) {
    val entry = combine(
        savedStateHandle.getStateFlow<String?>(NAME_KEY, null),
        savedStateHandle.getStateFlow<Money?>(AMOUNT_KEY, null),
        savedStateHandle.getStateFlow<Recurrences?>(RECURRENCES_KEY, null),
        savedStateHandle.getStateFlow<Uri?>(IMAGE_KEY, Uri("")),
    ) { name, amount, recurrences, image ->
        CategoryEntry(
            name = name,
            amount = amount,
            recurrences = recurrences,
            image = image,
        )
    }

    fun setName(name: String) {
        savedStateHandle[NAME_KEY] = name
    }

    fun setAmount(amount: Money) {
        savedStateHandle[AMOUNT_KEY] = amount
    }

    fun setRecurrences(recurrences: Recurrences) {
        savedStateHandle[RECURRENCES_KEY] = recurrences
    }

    fun setImage(image: Uri) {
        savedStateHandle[IMAGE_KEY] = image
    }

    companion object {
        private const val NAME_KEY = "name"
        private const val AMOUNT_KEY = "amount"
        private const val RECURRENCES_KEY = "recurrences"
        private const val IMAGE_KEY = "image"
    }
}

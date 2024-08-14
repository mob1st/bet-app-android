package br.com.mob1st.features.finances.impl.ui.category.navigation

import android.os.Parcelable
import br.com.mob1st.core.androidx.navigation.jsonParcelableType
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

internal sealed interface CategoryNavRoute {
    @Serializable
    data class Detail(
        val args: Args,
    ) : CategoryNavRoute {
        @Parcelize
        @Serializable
        data class Args(
            val intent: @WriteWith<GetCategoryIntentParceler> GetCategoryIntent,
            val recurrenceType: RecurrenceType,
            val isExpense: Boolean,
        ) : Parcelable
    }

    companion object {
        val navTypes = mapOf(
            typeOf<Detail.Args>() to jsonParcelableType<Detail.Args>(),
        )
    }
}

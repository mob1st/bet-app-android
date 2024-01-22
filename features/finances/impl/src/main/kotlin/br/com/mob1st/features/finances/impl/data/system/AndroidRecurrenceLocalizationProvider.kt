package br.com.mob1st.features.finances.impl.data.system

import android.content.Context
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.data.ram.RecurrentCategorySuggestion

/**
 * Implementation of [RecurrenceLocalizationProvider] for Android using [Context.getString]
 * @property context the context to get the localized strings
 */
internal class AndroidRecurrenceLocalizationProvider(
    private val context: Context,
) : RecurrenceLocalizationProvider {

    override operator fun get(suggestion: RecurrentCategorySuggestion): String {
        val resId: Int = when (suggestion) {
            RecurrentCategorySuggestion.RENT -> R.string.finances_builder_fixed_suggestions_item_rent
            RecurrentCategorySuggestion.MORTGAGE -> R.string.finances_builder_fixed_suggestions_item_mortgage
            RecurrentCategorySuggestion.ENERGY -> R.string.finances_builder_fixed_suggestions_item_energy
            RecurrentCategorySuggestion.INTERNET -> R.string.finances_builder_fixed_suggestions_item_internet
            RecurrentCategorySuggestion.PHONE -> R.string.finances_builder_fixed_suggestions_item_phone
            RecurrentCategorySuggestion.TV_STREAMING -> R.string.finances_builder_fixed_suggestions_item_tv
            RecurrentCategorySuggestion.MUSIC_STREAMING -> R.string.finances_builder_fixed_suggestions_item_music
            RecurrentCategorySuggestion.INSURANCES -> R.string.finances_builder_fixed_suggestions_item_insurances
            RecurrentCategorySuggestion.EDUCATION -> R.string.finances_builder_fixed_suggestions_item_education
            RecurrentCategorySuggestion.GYM -> R.string.finances_builder_fixed_suggestions_item_gym
            RecurrentCategorySuggestion.TRANSPORT -> R.string.finances_builder_fixed_suggestions_item_transport
            else -> TODO()
        }
        return context.getString(resId)
    }
}

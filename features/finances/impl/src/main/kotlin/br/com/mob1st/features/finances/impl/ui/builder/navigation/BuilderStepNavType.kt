package br.com.mob1st.features.finances.impl.ui.builder.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import br.com.mob1st.core.kotlinx.structures.biMapOf
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep

/**
 * Nav type for [BudgetBuilderAction.Step].
 */
internal object BuilderStepNavType : NavType<BudgetBuilderAction.Step>(false) {
    private val stringTypeMap = biMapOf(
        FixedExpensesStep to "FixedExpenses",
        VariableExpensesStep to "VariableExpenses",
        SeasonalExpensesStep to "SeasonalExpenses",
        FixedIncomesStep to "FixedIncomes",
    )

    override fun get(bundle: Bundle, key: String): BudgetBuilderAction.Step {
        val ordinal = bundle.getString(key)
        return stringTypeMap.getRightValue(ordinal.orEmpty())
    }

    override fun parseValue(value: String): BudgetBuilderAction.Step {
        return stringTypeMap.getRightValue(value)
    }

    override fun put(bundle: Bundle, key: String, value: BudgetBuilderAction.Step) {
        bundle.putString(key, stringTypeMap.getLeftValue(value))
    }

    override fun serializeAsValue(value: BudgetBuilderAction.Step): String {
        val string = stringTypeMap.getLeftValue(value)
        return Uri.encode(string)
    }
}

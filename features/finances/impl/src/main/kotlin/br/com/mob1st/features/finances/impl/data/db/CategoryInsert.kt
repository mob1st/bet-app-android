package br.com.mob1st.features.finances.impl.data.db

import br.com.mob1st.features.finances.impl.PorkyDb
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory

class CategoryInsert(private val porkyDb: PorkyDb) {

    private val categoryQueries = porkyDb.categoryQueries
    private val recurrentCategoryQueries = porkyDb.recurrentCategoryQueries
    operator fun invoke(list: List<RecurrentCategory>) = porkyDb.transaction {
        list.forEach { input ->
            // categoryQueries.insert(input.description)
            val categoryId = categoryQueries
                .selectLastInsertedId()
                .executeAsOne()
            val recurrenceTypeId = recurrentCategoryQueries
                .selectRecurrenceTypeByDescription(input.asRow())
                .executeAsOne()
            val recurrenceCategoryId = input.insertRecurrentForCategory(
                categoryId = categoryId,
                recurrenceTypeId = recurrenceTypeId
            )
            input.insertRecurrenceType(recurrenceCategoryId)
        }
    }

    private fun RecurrentCategory.insertRecurrentForCategory(categoryId: Long, recurrenceTypeId: Long): Long {
        recurrentCategoryQueries.insertRecurrentCategory(
            category_id = categoryId,
            recurrence_type_id = recurrenceTypeId,
            amount = 0L,
            is_expense = 1
        )
        return recurrentCategoryQueries.selectLastInsertedId().executeAsOne()
    }

    private fun RecurrentCategory.insertRecurrenceType(recurrenceCategoryId: Long) = when (this) {
        is RecurrentCategory.Fixed -> recurrentCategoryQueries.insertFixedRecurrence(
            recurrent_category_id = recurrenceCategoryId,
            day_of_month = dayOfMonth.toLong()
        )

        is RecurrentCategory.Seasonal -> recurrentCategoryQueries.insertSeasonalRecurrence(
            recurrent_category_id = recurrenceCategoryId,
            month_and_day = ""
        )

        is RecurrentCategory.Variable -> recurrentCategoryQueries.insertVariableRecurrence(
            recurrent_category_id = recurrenceCategoryId,
            day_of_week = 0L
        )
    }
}

private fun RecurrentCategory.asRow(): String = when (this) {
    is RecurrentCategory.Fixed -> "fixed"
    is RecurrentCategory.Seasonal -> "seasonal"
    is RecurrentCategory.Variable -> "variable"
}

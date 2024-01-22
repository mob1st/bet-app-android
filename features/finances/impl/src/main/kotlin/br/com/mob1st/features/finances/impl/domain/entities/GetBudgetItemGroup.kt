package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItemGroup
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Get the step content for the field selected in the builder
 * @param getStep block to select the step
 * @param T type of recurrent category
 */
internal fun <T : RecurrentCategory> RecurrenceBuilderRepository.getBudgetItemGroup(
    getStep: (RecurrenceBuilder) -> RecurrenceBuilder.Step<T>,
): Flow<BudgetItemGroup<T>> = get()
    .map { builder: RecurrenceBuilder -> getStep(builder) }
    .map { step: RecurrenceBuilder.Step<T> -> step.list }
    .distinctUntilChanged()
    .map { list: List<T> -> list.toBudgetItemGroup() }

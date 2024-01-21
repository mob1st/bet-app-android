package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategoryGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Get the step content for the field selected in the builder
 * @param block block to select the step
 * @param T type of recurrent category
 */
internal fun <T : RecurrentCategory> RecurrenceBuilderRepository.getRecurrentCategoryGroupBy(
    block: (RecurrenceBuilder) -> RecurrenceBuilder.Step<T>,
): Flow<RecurrentCategoryGroup<T>> = get()
    .map { block(it) }
    .distinctUntilChanged()
    .map { step ->
        RecurrentCategoryGroup(step.list)
    }

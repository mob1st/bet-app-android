package br.com.mob1st.features.twocents.builder.impl.domain.usecases

import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryBatch
import kotlinx.coroutines.delay

internal class SetCategoryBatchUseCase {
    suspend operator fun invoke(batch: CategoryBatch) {
        delay(1)
    }
}

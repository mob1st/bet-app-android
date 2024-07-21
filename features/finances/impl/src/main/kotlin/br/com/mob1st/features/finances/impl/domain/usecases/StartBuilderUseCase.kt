package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository

internal class StartBuilderUseCase(
    private val categoriesRepository: CategoriesRepository,
    private val analyticsReporter: AnalyticsReporter,
)

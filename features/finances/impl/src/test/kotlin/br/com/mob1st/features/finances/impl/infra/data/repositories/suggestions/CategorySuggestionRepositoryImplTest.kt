package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.androidx.assets.AssetsGetter
import br.com.mob1st.core.androidx.resources.StringGetter
import br.com.mob1st.core.androidx.resources.StringIdGetter
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.BeforeEach

@OptIn(ExperimentalCoroutinesApi::class)
class CategorySuggestionRepositoryImplTest {
    private lateinit var repository: CategorySuggestionsRepositoryImpl
    private lateinit var stringIdGetter: StringIdGetter
    private lateinit var stringGetter: StringGetter
    private lateinit var assetsGetter: AssetsGetter
    private val io = IoCoroutineDispatcher(
        UnconfinedTestDispatcher(),
    )

    @BeforeEach
    fun setUp() {
        repository = CategorySuggestionsRepositoryImpl(
            io = io,
            stringGetter = stringGetter,
            stringIdGetter = stringIdGetter,
            assetsGetter = assetsGetter,
        )
    }
}

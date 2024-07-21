package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import br.com.mob1st.features.finances.impl.utils.testTwoCentsDb
import com.appmattus.kotlinfixture.Fixture
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.BeforeEach

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryRepositoryImplTest {
    private lateinit var repository: CategoryRepositoryImpl
    private lateinit var db: TwoCentsDb
    private lateinit var fixture: Fixture

    private val io: IoCoroutineDispatcher = IoCoroutineDispatcher(
        UnconfinedTestDispatcher(),
    )

    @BeforeEach
    fun setUp() {
        fixture = moduleFixture
        db = testTwoCentsDb()
        repository = CategoryRepositoryImpl(
            io = io,
            db = db,
        )
    }
}

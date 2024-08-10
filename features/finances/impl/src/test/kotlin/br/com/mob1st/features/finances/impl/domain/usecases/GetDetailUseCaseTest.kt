package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.entities.toDefaultRecurrences
import br.com.mob1st.features.finances.impl.domain.infra.repositories.AssetRepository
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository
import br.com.mob1st.features.finances.impl.domain.values.asset
import br.com.mob1st.features.finances.impl.domain.values.category
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetDetailUseCaseTest {
    private lateinit var useCase: GetCategoryDetailUseCase
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var assetRepository: AssetRepository

    @BeforeEach
    fun setUp() {
        categoryRepository = mockk()
        assetRepository = mockk()
        useCase = GetCategoryDetailUseCase(categoryRepository, assetRepository)
    }

    @Test
    fun `GIVEN a list of assets WHEN get detail with create intention THEN assert the first asset is used`() = runTest {
        val assets = Arb.asset().chunked(2..10).next()
        every { assetRepository.getByTag(any()) } returns flowOf(assets)
        val intent = Arb.bind<GetCategoryIntent.Create>().next()
        val actual = useCase[intent].first()
        val asset = assets.first()
        assertEquals(asset.uri, actual.image)
    }

    @Test
    fun `GIVEN a list of assets WHEN get detail with create intention THEN assert default properties`() = runTest {
        val intent = Arb.bind<GetCategoryIntent.Create>().next()
        val assets = Arb.asset().chunked(1..3).next()
        every { assetRepository.getByTag(intent.name) } returns flowOf(assets)
        val expected = Category(
            name = intent.name,
            image = assets.first().uri,
            isExpense = intent.isExpense,
            recurrences = intent.type.toDefaultRecurrences(),
            isSuggested = false,
        )
        val actual = useCase[intent].first()
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a category WHEN get detail with edit intention THEN assert category is fetched by id`() = runTest {
        val intent = Arb.bind<GetCategoryIntent.Edit>().next()
        val category = Arb.category().map { it.copy(id = intent.id) }.next()
        every { categoryRepository.getById(intent.id) } returns flowOf(category)
        val actual = useCase[intent].first()
        assertEquals(category, actual)
        verify(exactly = 0) {
            assetRepository.getByTag(any())
        }
    }
}

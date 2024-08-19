package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.CalculatorPreferences
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategoryDetail
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.fixtures.asset
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.domain.fixtures.typeRecurrenceToRecurrences
import br.com.mob1st.features.finances.impl.domain.infra.repositories.AssetRepository
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CalculatorPreferencesRepository
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository
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
    private lateinit var calculatorPreferencesRepository: CalculatorPreferencesRepository

    @BeforeEach
    fun setUp() {
        categoryRepository = mockk()
        assetRepository = mockk()
        calculatorPreferencesRepository = mockk()
        useCase = GetCategoryDetailUseCase(
            categoryRepository = categoryRepository,
            calculatorPreferencesRepository = calculatorPreferencesRepository,
            assetRepository = assetRepository,
        )
    }

    @Test
    fun `GIVEN a list of assets WHEN get detail with create intention THEN assert the first asset is used`() = runTest {
        val assets = Arb.asset().chunked(2..10).next()
        val calculatorPreferences = Arb.bind<CalculatorPreferences>().next()
        every { assetRepository.getByTag(any()) } returns flowOf(assets)
        every { calculatorPreferencesRepository.get() } returns flowOf(calculatorPreferences)
        val intent = Arb.bind<GetCategoryIntent.Create>().next()
        val actual = useCase[intent].first()
        val asset = assets.first()
        assertEquals(asset.uri, actual.category.image)
    }

    @Test
    fun `GIVEN a list of assets WHEN get detail with create intention THEN assert default properties`() = runTest {
        val intent = Arb.bind<GetCategoryIntent.Create>().next()
        val assets = Arb.asset().chunked(1..3).next()
        val calculatorPreferences = Arb.bind<CalculatorPreferences>().next()
        every { assetRepository.getByTag(intent.name) } returns flowOf(assets)
        every { calculatorPreferencesRepository.get() } returns flowOf(calculatorPreferences)
        val expected = CategoryDetail(
            category = Category(
                name = intent.name,
                image = assets.first().uri,
                isExpense = intent.defaultValues.isExpense,
                recurrences = typeRecurrenceToRecurrences.getLeftValue(intent.defaultValues.recurrenceType),
                isSuggested = false,
            ),
            preferences = calculatorPreferences,
        )
        val actual = useCase[intent].first()
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a category WHEN get detail with edit intention THEN assert category is fetched by id`() = runTest {
        val intent = Arb.bind<GetCategoryIntent.Edit>().next()
        val calculatorPreferences = Arb.bind<CalculatorPreferences>().next()
        val category = Arb.category().map { it.copy(id = intent.id) }.next()
        every { categoryRepository.getById(intent.id) } returns flowOf(category)
        every { calculatorPreferencesRepository.get() } returns flowOf(calculatorPreferences)
        val actual = useCase[intent].first()
        val expected = CategoryDetail(
            category = category,
            preferences = calculatorPreferences,
        )
        assertEquals(expected, actual)
        verify(exactly = 0) {
            assetRepository.getByTag(any())
        }
    }
}

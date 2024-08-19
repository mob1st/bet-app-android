package br.com.mob1st.features.finances.impl.ui.category.detail

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.state.managers.ConsumableDelegate
import br.com.mob1st.features.finances.impl.domain.entities.CalculatorPreferences
import br.com.mob1st.features.finances.impl.domain.entities.CategoryDetail
import br.com.mob1st.features.finances.impl.domain.fixtures.categoryDetail
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryDetailUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.SetCalculatorPreferencesUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.SetCategoryUseCase
import br.com.mob1st.features.finances.impl.ui.fixtures.categoryEntry
import br.com.mob1st.tests.featuresutils.MainDispatcherTestExtension
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@ExtendWith(MainDispatcherTestExtension::class)
class CategoryViewModelTest {
    private lateinit var getDetail: GetCategoryDetailUseCase
    private lateinit var setCategory: SetCategoryUseCase
    private lateinit var setPreferencesUseCase: SetCalculatorPreferencesUseCase
    private lateinit var categoryStateHandleTest: CategoryStateHandle

    @BeforeEach
    fun setUp() {
        getDetail = mockk()
        setCategory = mockk(relaxed = true)
        setPreferencesUseCase = mockk(relaxed = true)
        categoryStateHandleTest = mockk()
    }

    @Test
    fun `GIVEN arguments And a saved state WHEN get initial state THEN saved data is used`() = runTest {
        val detail = Arb.categoryDetail().next()
        val entry = CategoryEntry(detail.category)
        givenInitialState(flowOf(detail), MutableStateFlow(entry))
        val viewModel = initViewModel()
        val expectedUiState = CategoryDetailUiState.Loaded(
            detail = detail,
            entry = entry,
        )
        turbineScope {
            val receivedUiState = viewModel.uiState.testIn(backgroundScope)
            val receivedConsumables = viewModel.consumablesState.testIn(backgroundScope)
            val actualUiState = receivedUiState.awaitItem()
            val actualConsumables = receivedConsumables.awaitItem()
            assertEquals(expectedUiState, actualUiState)
            assertEquals(CategoryDetailConsumables(), actualConsumables)
        }
    }

    @Test
    fun `GIVEN a zeroed state WHEN get initial state THEN assert loading state is emitted`() = runTest {
        every { getDetail[any()] } returns emptyFlow()
        val viewModel = initViewModel()
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(CategoryDetailUiState.Loading, state)
        }
    }

    @Test
    fun `GIVEN a number WHEN type THEN assert ui state changed And new value is saved`() = runTest {
        val detail = Arb.categoryDetail().next().copy(
            preferences = CalculatorPreferences(isEditCentsEnabled = false),
        )
        val entry = Arb.categoryEntry().next().copy(amount = Money.Zero)
        givenInitialState(flowOf(detail), MutableStateFlow(entry))
        val viewModel = initViewModel()
        viewModel.uiState.test {
            skipItems(1)
            viewModel.type(1)
            val state = awaitItem()
            val expectedEntry = entry.copy(
                amount = Money(100),
            )
            assertEquals(CategoryDetailUiState.Loaded(detail, expectedEntry), state)
        }
    }

    @Test
    fun `GIVEN a state with number WHEN type THEN assert value is erased`() = runTest {
        val detail = Arb.categoryDetail().next().copy()
        val entry = Arb.categoryEntry().next().copy(amount = Money(1))
        givenInitialState(flowOf(detail), MutableStateFlow(entry))
        val viewModel = initViewModel()
        viewModel.uiState.test {
            skipItems(1)
            viewModel.deleteNumber()
            val state = awaitItem()
            val expectedEntry = entry.copy(
                amount = Money.Zero,
            )
            assertEquals(CategoryDetailUiState.Loaded(detail, expectedEntry), state)
        }
    }

    @Test
    fun test() {
        // test show enter name dialog
        // test show icon picker dialog
        // test show all recurrences dialog
        // test type category name
        // test submit dialogs
        // test enable/disable edit cents
        // test submission
        assertFalse(true)
    }

    private fun givenInitialState(
        categoryDetailFlow: Flow<CategoryDetail> = flowOf(Arb.categoryDetail().next()),
        entryFlow: MutableStateFlow<CategoryEntry> = MutableStateFlow(Arb.categoryEntry().next()),
    ) {
        every { getDetail[any()] } returns categoryDetailFlow
        every { categoryStateHandleTest.entry(any()) } returns entryFlow
        every { categoryStateHandleTest.update(any()) } answers {
            entryFlow.value = firstArg()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TestScope.initViewModel(
        args: CategoryDetailArgs = Arb.bind<CategoryDetailArgs>().next(),
        testDispatcher: TestDispatcher = UnconfinedTestDispatcher(testScheduler),
    ) = CategoryViewModel(
        default = DefaultCoroutineDispatcher(
            testDispatcher,
        ),
        consumableDelegate = ConsumableDelegate(CategoryDetailConsumables()),
        categoryStateHandle = categoryStateHandleTest,
        getCategoryDetail = getDetail,
        setCategory = setCategory,
        setPreferences = setPreferencesUseCase,
        args = args,
    )
}

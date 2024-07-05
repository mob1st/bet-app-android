package br.com.mob1st.features.finances.impl.ui.builder

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategoryBuilder
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.domain.events.NotEnoughItemsToCompleteEvent
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryBuilderUseCase
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.tests.featuresutils.TestTimberTree
import br.com.mob1st.tests.featuresutils.ViewModelTestExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import timber.log.Timber
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(ViewModelTestExtension::class)
class CategoryBuilderViewModelTest {
    private lateinit var getCategoryBuilder: GetCategoryBuilderUseCase
    private lateinit var analyticsReporter: AnalyticsReporter
    private lateinit var timberTree: TestTimberTree

    @BeforeEach
    fun setUp() {
        timberTree = TestTimberTree()
        Timber.plant(timberTree)
        getCategoryBuilder = mockk()
        analyticsReporter = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        Timber.uproot(timberTree)
    }

    @Test
    fun `WHEN get initial ui state THEN assert it's correct`() = runTest {
        every { getCategoryBuilder[any()] } returns emptyFlow()
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderUiState()
        // When
        viewModel.uiStateOutput.test {
            // Then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `WHEN get initial consumables THEN assert it's expected`() = runTest {
        every { getCategoryBuilder[any()] } returns flowOf(moduleFixture())
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables()
        // When
        viewModel.consumableUiState.test {
            // Then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `GIVEN a failure in the get WHEN get initial state THEN assert error snackbar is shown`() = runTest {
        every { getCategoryBuilder[any()] } returns flow { throw Throwable("failed") }
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            snackbar = CategoryBuilderSnackbar.Failure(CommonError.Unknown),
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumables = viewModel.consumableUiState.testIn(backgroundScope)
            assertEquals(expected, receiveConsumables.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state WHEN click to add item THEN assert navigation to add category`() = runTest {
        every { getCategoryBuilder[any()] } returns flowOf(moduleFixture())
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            dialog = CategoryBuilderDialog.EnterName(""),
        )
        turbineScope {
            val receiveUiState = viewModel.uiStateOutput.testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            val uiState = receiveUiState.awaitItem()
            viewModel.selectManuallyAddedItem(uiState.manuallyAdded.lastIndex)
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with a manual item WHEN select manual item THEN assert navigation to edit category`() = runTest {
        val categoryBuilder = moduleFixture<CategoryBuilder>().copy(
            manuallyAdded = listOf(moduleFixture<Category>().copy(RowId(1))),
        )
        every { getCategoryBuilder[any()] } returns flowOf(categoryBuilder)
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            navTarget = CategoryBuilderNavTarget.EditCategory(
                category = RowId(1),
            ),
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.selectManuallyAddedItem(0)
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with suggested items And no linked category WHEN select suggested item THEN assert navigation to add category`() = runTest {
        val categoryBuilder = moduleFixture<CategoryBuilder>().copy(
            suggestions = listOf(
                CategorySuggestion(
                    id = RowId(1),
                    nameResId = 1,
                    linkedCategory = null,
                ),
            ),
        )
        every { getCategoryBuilder[any()] } returns flowOf(categoryBuilder)
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            navTarget = CategoryBuilderNavTarget.AddCategory(
                name = TextState(1),
                linkedSuggestion = RowId(1),
            ),
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.selectSuggestedItem(0)
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with suggested item And linked category WHEN select suggested item THEN assert navigation to edit category`() = runTest {
        val categoryBuilder = moduleFixture<CategoryBuilder>().copy(
            suggestions = listOf(
                moduleFixture<CategorySuggestion>().copy(
                    linkedCategory = moduleFixture<Category>().copy(
                        id = RowId(1),
                    ),
                ),
            ),
        )
        every { getCategoryBuilder[any()] } returns flowOf(categoryBuilder)
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            navTarget = CategoryBuilderNavTarget.EditCategory(
                category = RowId(1),
            ),
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.selectSuggestedItem(0)
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state WHEN enter category name THEN assert navigation to add category with name`() = runTest {
        every { getCategoryBuilder[any()] } returns flowOf(moduleFixture())
        val viewModel = viewModel(testScheduler)
        val expectedWhenSelectCategory = CategoryBuilderConsumables(
            dialog = CategoryBuilderDialog.EnterName(""),
        )
        val expectedWhenType = CategoryBuilderConsumables(
            dialog = CategoryBuilderDialog.EnterName("a"),
        )
        val expectedWhenSubmit = CategoryBuilderConsumables(
            navTarget = CategoryBuilderNavTarget.AddCategory(
                name = TextState("a"),
                linkedSuggestion = null,
            ),
        )
        turbineScope {
            val receiveUiState = viewModel.uiStateOutput.testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            val uiState = receiveUiState.awaitItem()
            viewModel.selectManuallyAddedItem(uiState.manuallyAdded.lastIndex)
            assertEquals(expectedWhenSelectCategory, receiveConsumable.awaitItem())
            viewModel.typeCategoryName("a")
            assertEquals(expectedWhenType, receiveConsumable.awaitItem())
            viewModel.submitCategoryName()
            assertEquals(expectedWhenSubmit, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state WHEN submit category name before enter name THEN assert error is shown`() = runTest {
        every { getCategoryBuilder[any()] } returns flowOf(moduleFixture())
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            snackbar = CategoryBuilderSnackbar.Failure(CommonError.Unknown),
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.submitCategoryName()
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with 1 suggestion WHEN select wrong suggestion index THEN assert error is shown`() = runTest {
        val categoryBuilder = moduleFixture<CategoryBuilder>().copy(
            suggestions = listOf(
                moduleFixture<CategorySuggestion>().copy(
                    linkedCategory = moduleFixture<Category>().copy(
                        id = RowId(1),
                    ),
                ),
            ),
        )
        every { getCategoryBuilder[any()] } returns flowOf(categoryBuilder)
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            snackbar = CategoryBuilderSnackbar.Failure(CommonError.Unknown),
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.selectSuggestedItem(1)
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with 1 manual category WHEN select wrong category index THEN assert error is shown`() = runTest {
        val categoryBuilder = moduleFixture<CategoryBuilder>().copy(
            manuallyAdded = listOf(moduleFixture<Category>().copy()),
        )
        every { getCategoryBuilder[any()] } returns flowOf(categoryBuilder)
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            snackbar = CategoryBuilderSnackbar.Failure(CommonError.Unknown),
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.selectManuallyAddedItem(2)
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with enough items WHEN proceed to next step THEN assert navigation to next step`() = runTest {
        val categoryBuilder = moduleFixture<CategoryBuilder>().copy(
            id = FixedExpensesStep,
            manuallyAdded = moduleFixture {
                repeatCount { 3 }
            },
            suggestions = listOf(
                moduleFixture<CategorySuggestion>().copy(
                    linkedCategory = moduleFixture<Category>().copy(),
                ),
            ),
        )
        every { getCategoryBuilder[any()] } returns flowOf(categoryBuilder)
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            navTarget = CategoryBuilderNavTarget.NextStep(VariableExpensesStep),
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.next()
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with not enough items WHEN proceed to next step THEN assert error is shown And error is tracked as event`() = runTest {
        val categoryBuilder = moduleFixture<CategoryBuilder>().copy(
            id = FixedExpensesStep,
            manuallyAdded = moduleFixture {
                repeatCount { 1 }
            },
            suggestions = listOf(
                moduleFixture<CategorySuggestion>().copy(
                    linkedCategory = moduleFixture<Category>().copy(),
                ),
            ),
        )
        val slot = slot<AnalyticsEvent>()
        every { getCategoryBuilder[any()] } returns flowOf(categoryBuilder)
        every { analyticsReporter.log(capture(slot)) } returns Unit
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            snackbar = CategoryBuilderSnackbar.NotAllowedToProceed(1),
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.next()
            assertEquals(expected, receiveConsumable.awaitItem())
            assertEquals(
                NotEnoughItemsToCompleteEvent(FixedExpensesStep, 1),
                slot.captured,
            )
            assertTrue(timberTree.logs.isEmpty())
        }
    }

    @Test
    fun `GIVEN a last step And enough added items WHEN proceed to next THEN assert navigation to completion`() = runTest {
        val categoryBuilder = moduleFixture<CategoryBuilder>().copy(
            id = FixedIncomesStep,
            manuallyAdded = moduleFixture {
                repeatCount { 2 }
            },
            suggestions = listOf(
                moduleFixture<CategorySuggestion>().copy(
                    linkedCategory = moduleFixture<Category>().copy(),
                ),
            ),
        )
        every { getCategoryBuilder[any()] } returns flowOf(categoryBuilder)
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            navTarget = CategoryBuilderNavTarget.BuilderCompletion,
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.next()
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN an empty state WHEN try to proceed THEN assert an error is shown`() = runTest {
        every { getCategoryBuilder[any()] } returns emptyFlow()
        val viewModel = viewModel(testScheduler)
        val expected = CategoryBuilderConsumables(
            snackbar = CategoryBuilderSnackbar.Failure(CommonError.Unknown),
        )
        turbineScope {
            viewModel.uiStateOutput.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.next()
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    private fun viewModel(
        scheduler: TestCoroutineScheduler,
    ) = CategoryBuilderViewModel(
        step = moduleFixture(),
        getCategoryBuilder = getCategoryBuilder,
        analyticsReporter = analyticsReporter,
        default = DefaultCoroutineDispatcher(
            UnconfinedTestDispatcher(scheduler),
        ),
    )
}

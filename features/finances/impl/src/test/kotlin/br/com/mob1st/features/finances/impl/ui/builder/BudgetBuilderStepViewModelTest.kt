package br.com.mob1st.features.finances.impl.ui.builder

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.state.managers.ConsumableDelegate
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.fixtures.budgetBuilder
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.domain.usecases.GetBudgetBuilderForStepUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.ProceedBuilderUseCase
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderStepNavArgs
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepUiState
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepViewModel
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepCategoryDetailNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepConsumables
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepLoadedBody
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepLoadingBody
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNameDialog
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNextNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNotAllowedSnackbar
import br.com.mob1st.features.finances.impl.ui.category.navigation.CategoryDetailArgs
import br.com.mob1st.features.finances.impl.ui.fixtures.builderStepToNavArgsMap
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.CommonErrorSnackbarState
import br.com.mob1st.tests.featuresutils.MainDispatcherTestExtension
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherTestExtension::class)
class BudgetBuilderStepViewModelTest {
    private lateinit var getCategoryBuilder: GetBudgetBuilderForStepUseCase
    private lateinit var proceedBuilder: ProceedBuilderUseCase

    @BeforeEach
    fun setUp() {
        getCategoryBuilder = mockk()
        proceedBuilder = mockk()
    }

    @Test
    fun `WHEN get initial ui state THEN assert it's correct`() = runTest {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        every { getCategoryBuilder[any()] } returns emptyFlow()
        val viewModel = viewModel(testScheduler, args = args)
        val step = builderStepToNavArgsMap.getLeftValue(args)
        val expected = BudgetBuilderStepUiState(step = step, isLoadingNext = false, body = BuilderStepLoadingBody)
        // When
        viewModel.uiState.test {
            // Then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `WHEN get initial consumables THEN assert it's expected`() = runTest {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        val builder = givenBudgetBuilder(args)
        every { getCategoryBuilder[any()] } returns flowOf(builder)
        val viewModel = viewModel(testScheduler, args)
        val expected = BuilderStepConsumables()
        // When
        viewModel.consumableUiState.test {
            // Then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `GIVEN a failure in the get WHEN get initial state THEN assert error snackbar is shown`() = runTest {
        val failure = Throwable("failed")
        every { getCategoryBuilder[any()] } returns flow { throw failure }
        val viewModel = viewModel(testScheduler, Arb.enum<BuilderStepNavArgs>().next())
        val expected = BuilderStepConsumables(
            snackbar = CommonErrorSnackbarState(failure),
        )
        turbineScope {
            viewModel.uiState.drop(1).testIn(backgroundScope)
            val receiveConsumables = viewModel.consumableUiState.testIn(backgroundScope)
            assertEquals(expected, receiveConsumables.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state WHEN click to add item THEN assert navigation to add category`() = runTest {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        val builder = givenBudgetBuilder(args)
        every { getCategoryBuilder[any()] } returns flowOf(builder)
        val viewModel = viewModel(testScheduler, args)
        val expected = BuilderStepConsumables(
            dialog = BuilderStepNameDialog(),
        )
        turbineScope {
            viewModel.uiState.testIn(backgroundScope).skipItems(1)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.addNewCategory()
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with a manual item WHEN select manual item THEN assert navigation to edit category`() = runTest {
        val categories = Arb.category().chunked(1..10).filter { list ->
            list.any { !it.isSuggested }
        }.next()
        val args = Arb.enum<BuilderStepNavArgs>().next()
        val builder = givenBudgetBuilder(args) { builder ->
            builder.copy(categories = categories)
        }
        val randomIndex = builder.manuallyAdded.indices.random()
        every { getCategoryBuilder[any()] } returns flowOf(builder)
        val viewModel = viewModel(testScheduler, args)
        turbineScope {
            val receiveUiState = viewModel.uiState.testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            val body = receiveUiState.awaitItem().body as BuilderStepLoadedBody
            val category = body.manuallyAdded[randomIndex].category
            viewModel.selectManuallyAddedItem(randomIndex)
            val expected = BuilderStepConsumables(
                navEvent = BuilderStepCategoryDetailNavEvent(
                    args = CategoryDetailArgs(
                        intent = CategoryDetailArgs.Intent.Edit(category.id.value),
                        name = category.name,
                        isExpense = category.isExpense,
                        recurrenceType = category.recurrences.asType(),
                    ),
                ),
            )
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with suggested item WHEN select suggested item THEN assert navigation to edit category`() = runTest {
        val categories = Arb.category().chunked(1..10).filter { list ->
            list.any { it.isSuggested }
        }.next()
        val args = Arb.enum<BuilderStepNavArgs>().next()
        val budgetBuilder = givenBudgetBuilder(args) {
            it.copy(categories = categories)
        }
        val randomIndex = budgetBuilder.suggestions.indices.random()
        every { getCategoryBuilder[any()] } returns flowOf(budgetBuilder)
        val viewModel = viewModel(testScheduler, args)
        turbineScope {
            val receiveUiState = viewModel.uiState.testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            val body = receiveUiState.awaitItem().body as BuilderStepLoadedBody
            val category = body.suggestions[randomIndex].category
            viewModel.selectSuggestedItem(randomIndex)

            val expected = BuilderStepConsumables(
                navEvent = BuilderStepCategoryDetailNavEvent(
                    args = CategoryDetailArgs(
                        intent = CategoryDetailArgs.Intent.Edit(category.id.value),
                        name = category.name,
                        isExpense = category.isExpense,
                        recurrenceType = category.recurrences.asType(),
                    ),
                ),
            )
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state WHEN enter category name THEN assert navigation to add category with name`() = runTest {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        val builder = givenBudgetBuilder(args)
        every { getCategoryBuilder[any()] } returns flowOf(builder)
        val viewModel = viewModel(testScheduler, args)
        val expectedWhenSelectCategory = BuilderStepConsumables(
            dialog = BuilderStepNameDialog(),
        )
        val expectedWhenType = BuilderStepConsumables(
            dialog = BuilderStepNameDialog("a"),
        )
        val expectedWhenSubmit = BuilderStepConsumables(
            navEvent = BuilderStepCategoryDetailNavEvent(
                args = CategoryDetailArgs(
                    intent = CategoryDetailArgs.Intent.Create,
                    name = "a",
                    isExpense = builder.id.isExpense,
                    recurrenceType = builder.id.type,
                ),
            ),
        )
        turbineScope {
            viewModel.uiState.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.addNewCategory()
            assertEquals(expectedWhenSelectCategory, receiveConsumable.awaitItem())
            viewModel.typeCategoryName("a")
            assertEquals(expectedWhenType, receiveConsumable.awaitItem())
            viewModel.submitCategoryName()
            assertEquals(expectedWhenSubmit, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state WHEN submit category name before enter name THEN assert error is shown`() = runTest {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        val builder = givenBudgetBuilder(args)
        every { getCategoryBuilder[any()] } returns flowOf(builder)
        val viewModel = viewModel(testScheduler, args)
        val expected = BuilderStepConsumables(
            snackbar = CommonErrorSnackbarState(CommonError.Unknown),
        )
        turbineScope {
            viewModel.uiState.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.submitCategoryName()
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with 1 suggestion WHEN select wrong suggestion index THEN assert error is shown`() = runTest {
        val suggestion = Arb.category().map {
            it.copy(isSuggested = true)
        }.next()
        val args = Arb.enum<BuilderStepNavArgs>().next()
        val budgetBuilder = givenBudgetBuilder(args) {
            it.copy(categories = listOf(suggestion))
        }
        every { getCategoryBuilder[any()] } returns flowOf(budgetBuilder)
        val viewModel = viewModel(testScheduler, args)
        val expected = BuilderStepConsumables(
            snackbar = CommonErrorSnackbarState(CommonError.Unknown),
        )
        turbineScope {
            viewModel.uiState.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.selectSuggestedItem(1)
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with 1 manual category WHEN select wrong category index THEN assert error is shown`() = runTest {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        val manuallyAdded = Arb.category().map {
            it.copy(isSuggested = false)
        }.next()
        val budgetBuilder = givenBudgetBuilder(args) {
            it.copy(categories = listOf(manuallyAdded))
        }
        every { getCategoryBuilder[any()] } returns flowOf(budgetBuilder)
        val viewModel = viewModel(testScheduler, args)
        val expected = BuilderStepConsumables(
            snackbar = CommonErrorSnackbarState(CommonError.Unknown),
        )
        turbineScope {
            viewModel.uiState.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.selectManuallyAddedItem(2)
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with enough items WHEN proceed to next step THEN assert navigation to next step`() = runTest {
        val currentStep = Arb.bind<BudgetBuilderAction.Step>().next()
        val nextStep = Arb.bind<BudgetBuilderAction.Step>().next()
        val args = Arb.enum<BuilderStepNavArgs>().next()
        val budgetBuilder = givenBudgetBuilder(args) {
            it.copy(
                id = currentStep,
                categories = Arb.category().chunked(5..10).next(),
            )
        }
        every { getCategoryBuilder[any()] } returns flowOf(budgetBuilder)
        coEvery { proceedBuilder(budgetBuilder) } returns nextStep
        val viewModel = viewModel(testScheduler, args)
        val expected = BuilderStepConsumables(
            navEvent = BuilderStepNextNavEvent(
                next = builderStepToNavArgsMap.getRightValue(nextStep),
            ),
        )
        turbineScope {
            viewModel.uiState.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.next()
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN a state with not enough items WHEN proceed to next step THEN assert error is shown`() = runTest {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        val budgetBuilder = givenBudgetBuilder(args)
        every { getCategoryBuilder[any()] } returns flowOf(budgetBuilder)
        coEvery { proceedBuilder(budgetBuilder) } throws ProceedBuilderUseCase.NotEnoughInputsException(1)

        val viewModel = viewModel(testScheduler, args)
        val expected = BuilderStepConsumables(
            snackbar = BuilderStepNotAllowedSnackbar(1),
        )
        turbineScope {
            viewModel.uiState.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.next()
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN an empty state WHEN try to proceed THEN assert nothing happens`() = runTest {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        every { getCategoryBuilder[any()] } returns emptyFlow()
        val viewModel = viewModel(testScheduler, args)
        turbineScope {
            viewModel.uiState.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.testIn(backgroundScope)
            viewModel.next()
            assertEquals(BuilderStepConsumables(), receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN an empty state WHEN select manual item THEN assert error is shown`() = runTest {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        every { getCategoryBuilder[any()] } returns emptyFlow()
        val viewModel = viewModel(testScheduler, args)
        val expected = BuilderStepConsumables(
            snackbar = CommonErrorSnackbarState(CommonError.Unknown),
        )
        turbineScope {
            viewModel.uiState.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.selectManuallyAddedItem(0)
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    @Test
    fun `GIVEN an empty state WHEN select suggested item THEN assert error is shown`() = runTest {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        every { getCategoryBuilder[any()] } returns emptyFlow()
        val viewModel = viewModel(testScheduler, args)
        val expected = BuilderStepConsumables(
            snackbar = CommonErrorSnackbarState(CommonError.Unknown),
        )
        turbineScope {
            viewModel.uiState.drop(1).testIn(backgroundScope)
            val receiveConsumable = viewModel.consumableUiState.drop(1).testIn(backgroundScope)
            viewModel.selectSuggestedItem(0)
            assertEquals(expected, receiveConsumable.awaitItem())
        }
    }

    private fun givenBudgetBuilder(
        args: BuilderStepNavArgs,
        block: (BudgetBuilder) -> BudgetBuilder = { it },
    ): BudgetBuilder {
        return Arb.budgetBuilder().map {
            block(
                it.copy(id = builderStepToNavArgsMap.getLeftValue(args)),
            )
        }.next()
    }

    private fun viewModel(
        scheduler: TestCoroutineScheduler,
        args: BuilderStepNavArgs,
    ) = BudgetBuilderStepViewModel(
        args = args,
        consumableDelegate = ConsumableDelegate(BuilderStepConsumables()),
        getCategoryBuilder = getCategoryBuilder,
        proceedBuilder = proceedBuilder,
        default = DefaultCoroutineDispatcher(
            UnconfinedTestDispatcher(scheduler),
        ),
    )
}

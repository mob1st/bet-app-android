package br.com.mob1st.features.twocents.builder.impl.ui.builder

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryInput
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.GetSuggestionsUseCase
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.SetCategoryBatchUseCase
import br.com.mob1st.tests.featuresutils.ViewModelTestExtension
import br.com.mob1st.tests.featuresutils.fixture
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(ViewModelTestExtension::class)
internal class BuilderViewModelTest {
    private lateinit var builderUiState: BuilderUiState
    private lateinit var builderStateRestorer: BuilderStateRestorer
    private lateinit var categorySheetDelegate: CategorySheetDelegate
    private lateinit var categoryNameDialogDelegate: CategoryNameDialogDelegate
    private lateinit var getSuggestionsUseCase: GetSuggestionsUseCase
    private lateinit var setCategoryBatchUseCase: SetCategoryBatchUseCase

    @BeforeEach
    fun setUp() {
        builderUiState = BuilderUiState(fixture())
        builderStateRestorer = mockk()
        categorySheetDelegate = mockk()
        categoryNameDialogDelegate = mockk()
        getSuggestionsUseCase = mockk()
        setCategoryBatchUseCase = mockk()
        givenInitialState()
    }

    @Test
    fun `GIVEN a initial state WHEN collect THEN assert suggestion is added`() = runTest {
        // Given
        val viewModel = initViewModel()

        // When
        val expected = builderUiState.copy(
            suggestedSection = builderUiState.suggestedSection.copy(
                suggestions = persistentListOf(defaultSuggestion),
                categories = persistentListOf(defaultSuggestedListItem()),
            ),
        )
        viewModel.uiStateOutput.test {
            assertEquals(
                expected = expected,
                actual = awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a restored state WHEN collect THEN verify state is restored`() = runTest {
        // Given
        givenInitialState(
            userInput = restoredInput(
                manualName = "Manually Added",
                manualValue = "1000",
                suggestedValue = "200",
            ),
        )
        val viewModel = initViewModel()

        val expectedUiState = builderUiState.copy(
            suggestedSection = builderUiState.suggestedSection.copy(
                suggestions = persistentListOf(defaultSuggestion),
                categories = persistentListOf(defaultSuggestedListItem(Money(200))),
            ),
            manuallyAddedSection = builderUiState.manuallyAddedSection.copy(
                categories = persistentListOf(
                    BuilderListItemState(
                        input = CategoryInput(
                            type = builderUiState.categoryType,
                            name = "Manually Added",
                            value = Money(1000),
                            linkedSuggestion = null,
                        ),
                    ),
                ),
            ),
        )

        // When
        viewModel.uiStateOutput.test {
            // Then
            assertEquals(
                expected = expectedUiState,
                actual = awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a state WHEN save state to restore THEN verify state is saved`() = runTest {
        // Given
        val saveFunctionSlot = slot<() -> BuilderUserInput>()
        every { builderStateRestorer.getSavedValue(capture(saveFunctionSlot)) } returns BuilderUserInput()

        val viewModel = initViewModel()
        val expected = BuilderUserInput(
            suggested = mapOf(
                defaultSuggestion.id to BuilderUserInput.Entry(
                    name = "",
                    amount = "0",
                ),
            ),
        )
        viewModel.uiStateOutput.test {
            awaitItem()
            // When
            val actual = saveFunctionSlot.captured.invoke()
            // Then
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `GIVEN a category sheet for update selection WHEN submit sheet THEN assert update is sent`() = runTest {
        // Given
        categorySheetDelegate = mockk(relaxed = true)
        val sheetInput = MutableSharedFlow<CategorySheetState>()
        givenInitialState(categorySheetInput = sheetInput)
        val viewModel = initViewModel()
        val updatedInput = CategoryInput(
            type = builderUiState.categoryType,
            name = "",
            value = Money(200),
            linkedSuggestion = defaultSuggestion,
        )
        val sheetState = CategorySheetState(
            input = updatedInput,
            operation = CategorySheetState.Operation.Update(0),
        )
        val expected = builderUiState.copy(
            suggestedSection = builderUiState.suggestedSection.copy(
                suggestions = persistentListOf(defaultSuggestion),
                categories = persistentListOf(
                    BuilderListItemState(input = updatedInput),
                ),
            ),
        )

        viewModel.uiStateOutput.test {
            // skip initial state
            skipItems(1)

            // When
            sheetInput.emit(sheetState)

            // Then
            assertEquals(
                expected = expected,
                actual = awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a category sheet for manual update WHEN submit sheet THEN assert update is sent`() = runTest {
        // GIVEN
        categorySheetDelegate = mockk(relaxed = true)
        val input = CategoryInput(
            type = builderUiState.categoryType,
            name = "New Manual",
            value = Money(1000),
        )
        val sheet = CategorySheetState(
            input = input,
            operation = CategorySheetState.Operation.Add,
        )
        val sheetInput = MutableSharedFlow<CategorySheetState>()
        givenInitialState(categorySheetInput = sheetInput)
        val viewModel = initViewModel()

        val expected = builderUiState.copy(
            manuallyAddedSection = builderUiState.manuallyAddedSection.copy(
                categories = persistentListOf(BuilderListItemState(input = input)),
            ),
            suggestedSection = builderUiState.suggestedSection.copy(
                suggestions = persistentListOf(defaultSuggestion),
                categories = persistentListOf(defaultSuggestedListItem()),
            ),
        )
        viewModel.uiStateOutput.test {
            skipItems(1)
            // When
            sheetInput.emit(sheet)

            // Then
            assertEquals(
                expected,
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a name dialog opened WHEN submit name THEN verify category sheet is opened`() = runTest {
        // GIVEN
        val nameDialog = "a name"
        val nameDialogInput = MutableSharedFlow<String>()
        givenInitialState(nameInput = nameDialogInput)
        categorySheetDelegate = mockk(relaxed = true)
        val viewModel = initViewModel()

        val expected = CategorySheetState(
            operation = CategorySheetState.Operation.Add,
            input = CategoryInput(
                name = nameDialog,
                type = builderUiState.categoryType,
                value = Money.Zero,
            ),
        )
        viewModel.uiStateOutput.test {
            skipItems(1)

            // When
            nameDialogInput.emit(nameDialog)
            with(viewModel) {
                // THEN
                verify { categorySheetDelegate.showSheet(expected) }
            }
        }
    }

    @Test
    fun `GIVEN an initial state WHEN select suggested category THEN assert sheet is opened`() = runTest {
        // GIVEN
        categorySheetDelegate = mockk(relaxed = true)
        val viewModel = initViewModel()
        val expected = CategorySheetState(
            operation = CategorySheetState.Operation.Update(0),
            input = defaultSuggestedListItem().input,
        )
        viewModel.uiStateOutput.test {
            skipItems(1)

            viewModel.selectSuggestedCategory(0)
            with(viewModel) {
                // THEN
                verify { categorySheetDelegate.showSheet(expected) }
            }
        }
    }

    @Test
    fun `GIVEN an initial state WHEN select manual category THEN assert sheet is opened`() = runTest {
        // GIVEN
        categorySheetDelegate = mockk(relaxed = true)
        givenInitialState(
            userInput = restoredInput(
                manualValue = "1200",
                manualName = "manual item",
                suggestedValue = fixture(),
            ),
        )
        val viewModel = initViewModel()
        val expected = CategorySheetState(
            operation = CategorySheetState.Operation.Update(0),
            input = CategoryInput(
                type = builderUiState.categoryType,
                name = "manual item",
                value = Money(1200),
                linkedSuggestion = null,
            ),
        )
        viewModel.uiStateOutput.test {
            skipItems(1)

            viewModel.selectManualCategory(0)
            with(viewModel) {
                // THEN
                verify { categorySheetDelegate.showSheet(expected) }
            }
        }
    }

    @Test
    fun `GIVEN an initial state WHEN save THEN assert loading is visible`() = runTest {
        // GIVEN
        coEvery { setCategoryBatchUseCase(any()) } coAnswers {
            suspendCancellableCoroutine { }
        }
        val viewModel = initViewModel()
        viewModel.uiStateOutput.test {
            assertFalse(awaitItem().isSaving)

            // When
            viewModel.save()

            // Then
            assertTrue(awaitItem().isSaving)
        }
    }

    @Test
    fun `GIVEN an initial state And a failure to save WHEN save assert loading is removed And error message is displayed`() = runTest {
        // Given
        // since save triggers a coroutine, this dispatcher ensures that it will trigger all state flows before cancel the coroutine
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))

        coEvery { setCategoryBatchUseCase(any()) } throws Exception()
        givenInitialState(
            userInput = restoredInput(
                manualName = "Manually Added",
                manualValue = "1000",
                suggestedValue = "200",
            ),
        )
        val viewModel = initViewModel()
        turbineScope {
            val uiStateTurbine = viewModel.uiStateOutput.testIn(backgroundScope)
            val errorTurbine = viewModel.snackbarOutput.testIn(backgroundScope)
            uiStateTurbine.skipItems(1)
            errorTurbine.skipItems(1)

            viewModel.save()

            assertFalse(uiStateTurbine.awaitItem().isSaving)
        }
    }

    @Test
    fun `GIVEN an initial state And a success to save WHEN save assert loading is removed And the navigation is triggered`() = runTest {
        // Given
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        setCategoryBatchUseCase = mockk(relaxed = true)
        givenInitialState(
            userInput = restoredInput(
                manualName = "Manually Added",
                manualValue = "1000",
                suggestedValue = "200",
            ),
        )
        val viewModel = initViewModel()
        turbineScope {
            val uiStateTurbine = viewModel.uiStateOutput.testIn(backgroundScope)
            val navigationTurbine = viewModel.navigationOutput.testIn(backgroundScope)
            uiStateTurbine.skipItems(1)
            navigationTurbine.skipItems(1)

            // When
            viewModel.save()

            // Then
            assertFalse(uiStateTurbine.awaitItem().isSaving)
            assertNotNull(navigationTurbine.awaitItem())
        }
    }

    private fun givenInitialState(
        categorySheetInput: MutableSharedFlow<CategorySheetState> = MutableSharedFlow(),
        nameInput: MutableSharedFlow<String> = MutableSharedFlow(),
        userInput: BuilderUserInput = BuilderUserInput(),
        suggestions: Flow<List<CategorySuggestion>> = flowOf(listOf(defaultSuggestion)),
    ) {
        every { categorySheetDelegate.categorySheetInput } returns categorySheetInput
        every { getSuggestionsUseCase[any()] } returns suggestions
        every { builderStateRestorer.getSavedValue(any()) } returns userInput
        every { categoryNameDialogDelegate.nameInput } returns nameInput
    }

    private fun initViewModel() = BuilderViewModel(
        initialState = builderUiState,
        builderStateRestorer = builderStateRestorer,
        getSuggestionsUseCase = getSuggestionsUseCase,
        setCategoryBatchUseCase = setCategoryBatchUseCase,
        sideEffects = BuilderViewModel.SideEffects(
            categorySheetDelegate = categorySheetDelegate,
            categoryNameDialogDelegate = categoryNameDialogDelegate,
        ),
    )

    private fun defaultSuggestedListItem(money: Money = Money.Zero): BuilderListItemState {
        return BuilderListItemState(
            input = CategoryInput(
                type = builderUiState.categoryType,
                name = "",
                value = money,
                linkedSuggestion = defaultSuggestion,
            ),
        )
    }

    companion object {
        private val defaultSuggestion = CategorySuggestion(
            id = RowId(),
            name = CategorySuggestion.Name.RENT,
        )

        private fun restoredInput(
            manualName: String,
            manualValue: String,
            suggestedValue: String,
        ) = BuilderUserInput(
            manuallyAdded = listOf(
                BuilderUserInput.Entry(name = manualName, amount = manualValue),
            ),
            suggested = mapOf(
                defaultSuggestion.id to BuilderUserInput.Entry(name = "", amount = suggestedValue),
            ),
        )
    }
}

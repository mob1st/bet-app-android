package br.com.mob1st.features.twocents.builder.impl.ui.builder

import app.cash.turbine.test
import br.com.mob1st.core.kotlinx.structures.Id
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.GetSuggestionsUseCase
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.SetCategoryBatchUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BuilderViewModelTest {
    private lateinit var builderStateSaver: BuilderStateSaver
    private lateinit var builderUiState: BuilderUiState
    private lateinit var getSuggestionsUseCase: GetSuggestionsUseCase
    private lateinit var setCategoryBatchUseCase: SetCategoryBatchUseCase
    private lateinit var categorySheetDelegate: CategorySheetDelegate
    private lateinit var categoryNameDialogDelegate: CategoryNameDialogDelegate

    @BeforeEach
    fun setUp() {
        builderUiState = defaultInitialState()
        builderStateSaver = mockk()
        getSuggestionsUseCase = mockk()
        setCategoryBatchUseCase = mockk()
        categorySheetDelegate = mockk()
        givenInitialState()
    }

    @Test
    fun `GIVEN a initial state WHEN collect THEN assert initial state`() = runTest {
        // Given
        val viewModel = initViewModel()

        // When
        viewModel.uiStateOutput.test {
            assertEquals(
                expected = builderUiState,
                actual = awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a saved initial state WHEN collect THEN assert initial state`() = runTest {
    }

    @Test
    fun `GIVEN a initial state WHEN submit sheet THEN assert item is updated`() = runTest {
        // Given
    }

    @Test
    fun `GIVEN a state with category sheet to add item WHEN submit sheet THEN assert item is added`() = runTest {
        // Given
    }

    @Test
    fun `GIVEN a initial state WHEN click to update manually added category WHEN submit sheet THEN assert item is updated and sheet is hidden`() = runTest {
        // Given
    }

    @Test
    fun `GIVEN a filled state WHEN submit THEN assert state is submitted And loading is removed And navigation is triggered`() = runTest {
        // Given
    }

    @Test
    fun `GIVEN a filled state And a long submission WHEN submit THEN assert loading is displayed`() = runTest {
        // Given
    }

    @Test
    fun `GIVEN a filled state And a failed submission WHEN submit THEN assert error is displayed And loading is removed`() = runTest {
    }

    @Test
    fun `GIVEN a initial state WHEN click to add new category THEN assert category name dialog is displayed`() = runTest {
    }

    @Test
    fun `GIVEN a state with category name dialog WHEN type the category name THEN assert category name is updated`() = runTest {
    }

    @Test
    fun `GIVEN a state category name dialog WHEN type And submit dialog THEN assert dialog is hidden and category sheet is displayed with the name`() = runTest {
    }

    private fun givenInitialState(
        manualItemUpdateInput: MutableSharedFlow<CategorySheetState> = MutableSharedFlow(),
        suggestedItemUpdateInput: MutableSharedFlow<CategorySheetState> = MutableSharedFlow(),
        userInput: BuilderUserInput = BuilderUserInput(),
        suggestions: Flow<List<CategorySuggestion>> = flowOf(listOf(defaultSuggestion)),
    ) {
        every { categorySheetDelegate.manualItemUpdateInput } returns manualItemUpdateInput
        every { categorySheetDelegate.suggestedItemUpdateInput } returns suggestedItemUpdateInput
        every { getSuggestionsUseCase[any()] } returns suggestions
        every { builderStateSaver.getSavedValue(any()) } returns userInput
    }

    private fun defaultInitialState() = mockk<BuilderUiState> {
        every { createSuggestedSection(any(), any()) } returns SuggestedCategoryBuilderSection()
        every { createManualSuggestionsSection(any()) } returns ManualCategoryBuilderSection()
        every { combine(any(), any(), any()) } returns copy()
    }

    private fun initViewModel() = BuilderViewModel(
        initialState = builderUiState,
        builderStateSaver = builderStateSaver,
        getSuggestionsUseCase = getSuggestionsUseCase,
        setCategoryBatchUseCase = setCategoryBatchUseCase,
        sideEffects = BuilderViewModel.SideEffects(
            categorySheetDelegate = categorySheetDelegate,
            categoryNameDialogDelegate = categoryNameDialogDelegate,
        ),
    )

    companion object {
        private val defaultSuggestion = CategorySuggestion(
            id = Id(),
            name = CategorySuggestion.Name.RENT,
        )
    }
}

package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.features.finances.impl.domain.entities.CategoryBuilder
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import com.appmattus.kotlinfixture.Fixture
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CategoryBuilderUiStateTest {
    private lateinit var fixture: Fixture

    @BeforeEach
    fun setUp() {
        fixture = moduleFixture.new {
            factory<CategoryBuilder> {
                CategoryBuilder(
                    id = fixture(),
                    manuallyAdded = fixture {
                        repeatCount { 2 }
                    },
                    suggestions = fixture {
                        repeatCount { 2 }
                    },
                )
            }
        }
    }

    @Test
    fun `GIVEN a category builder with categories WHEN get lists THEN assert lists is correct`() {
        // Given
        val categoryBuilder = fixture<CategoryBuilder>()
        val expectedManuallyAdded = persistentListOf(
            ManualCategoryListItem(
                category = categoryBuilder.manuallyAdded[0],
            ),
            ManualCategoryListItem(
                category = categoryBuilder.manuallyAdded[1],
            ),
            AddCategoryListItem,
        )
        val expectedSuggestions = persistentListOf(
            SuggestionListItem(
                suggestion = categoryBuilder.suggestions[0],
            ),
            SuggestionListItem(
                suggestion = categoryBuilder.suggestions[1],
            ),
        )
        // When
        val categoryBuilderUiState = CategoryBuilderUiState(categoryBuilder)

        // Then
        assertEquals(
            expectedManuallyAdded,
            categoryBuilderUiState.manuallyAdded,
        )
        assertEquals(
            expectedSuggestions,
            categoryBuilderUiState.suggestions,
        )
    }

    @Test
    fun `GIVEN a category builder without categories WHEN get lists THEN assert lists is empty`() {
        // Given
        val categoryBuilder = fixture<CategoryBuilder>().copy(
            manuallyAdded = emptyList(),
            suggestions = emptyList(),
        )
        val expectedManuallyAdded = persistentListOf(AddCategoryListItem)
        val expectedSuggestions = persistentListOf<SuggestionListItem>()
        // When
        val categoryBuilderUiState = CategoryBuilderUiState(categoryBuilder)

        // Then
        assertEquals(
            expectedManuallyAdded,
            categoryBuilderUiState.manuallyAdded,
        )
        assertEquals(
            expectedSuggestions,
            categoryBuilderUiState.suggestions,
        )
    }

    @Test
    fun `GIVEN a no category builder WHEN get lists THEN assert lists are empty`() {
        // Given
        val uiState = CategoryBuilderUiState()
        assertTrue(uiState.manuallyAdded.isEmpty())
        assertTrue(uiState.suggestions.isEmpty())
    }
}

package br.com.mob1st.features.finances.impl.ui.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.androidx.compose.ComposableFunction
import br.com.mob1st.core.design.atoms.colors.material.fixedExpensesFamily
import br.com.mob1st.core.design.atoms.colors.material.incomesFamily
import br.com.mob1st.core.design.atoms.colors.material.seasonalExpensesFamily
import br.com.mob1st.core.design.atoms.colors.material.variableExpensesFamily
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.design.organisms.lists.selectableItem
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepPreviewFixture
import br.com.mob1st.features.finances.impl.ui.utils.texts.MoneyTextState
import br.com.mob1st.features.finances.impl.ui.utils.texts.RecurrencesTextStateFactory
import br.com.mob1st.features.finances.impl.ui.utils.texts.toIconBackground
import coil.compose.AsyncImage

/**
 * Abstraction of a list item to present in the category builder screen.
 * It can be a suggestion, a manually added category, or the "Add category" item.
 */
@Immutable
data class CategorySectionItemState(
    val category: Category,
) {
    /**
     * The key of the item. It's used to identify the item in the list and optimize the rendering.
     */
    val key: Any = category.id

    /**
     * The icon of the item.
     */
    val icon: Icon = Icon(
        background = category.recurrences.toIconBackground(),
        image = category.image,
    )

    /**
     * The leading text of the item. Usually it's the category name or the main instruction
     */
    val headline: String = category.name

    /**
     * The supporting text of the item. It's usually the category description, if any.
     */
    val supporting: TextState? = RecurrencesTextStateFactory.create(category.recurrences)

    /**
     * The value text of the item. It's usually the category amount, if any.
     */
    val trailing: Trailing = Trailing(
        amount = MoneyTextState(category.amount),
        supporting = null,
    )

    /**
     * The icon of for the category.
     * @property background The background color of the icon.
     * @property image The image of the icon.
     */
    @Immutable
    data class Icon(
        val background: IconBackground?,
        val image: Uri,
    )

    /**
     * The background color of the icon depending on its type.
     */
    enum class IconBackground {
        FIXED_EXPENSES,
        VARIABLE_EXPENSES,
        SEASONAL_EXPENSES,
        INCOMES,
    }

    /**
     * The trailing text of the item.
     */
    @Immutable
    data class Trailing(
        val amount: TextState,
        val supporting: TextState?,
    )
}

/**
 * Setup the list item for the category section.
 * It applies the proper alignment and typography for the components.
 */
@Composable
fun CategorySectionItem(
    state: CategorySectionItemState,
    onSelect: () -> Unit,
) {
    val decoratedSupportingContent: ComposableFunction? =
        state.supporting?.let { text ->
            {
                ProvideTextStyle(value = CategoryListItemDefaults.bottomTextStyle) {
                    Text(text = text.resolve())
                }
            }
        }
    ListItem(
        modifier = Modifier.selectableItem(onSelect = onSelect),
        leadingContent = {
            Icon(icon = state.icon)
        },
        headlineContent = {
            Text(text = state.headline)
        },
        supportingContent = decoratedSupportingContent,
        trailingContent = {
            Trailing(trailing = state.trailing)
        },
    )
}

@Composable
private fun Trailing(
    trailing: CategorySectionItemState.Trailing,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSurface,
        ) {
            ProvideTextStyle(value = CategoryListItemDefaults.topTextStyle) {
                Text(text = trailing.amount.resolve())
            }
        }
        trailing.supporting?.let {
            Text(
                text = it.resolve(),
                style = CategoryListItemDefaults.bottomTextStyle,
            )
        }
    }
}

@Composable
private fun Icon(
    icon: CategorySectionItemState.Icon,
) {
    val colorScheme = MaterialTheme.colorScheme
    val colors = when (icon.background) {
        CategorySectionItemState.IconBackground.FIXED_EXPENSES -> {
            colorScheme.fixedExpensesFamily.container to colorScheme.fixedExpensesFamily.onContainer
        }

        CategorySectionItemState.IconBackground.VARIABLE_EXPENSES -> {
            colorScheme.variableExpensesFamily.container to colorScheme.variableExpensesFamily.onContainer
        }

        CategorySectionItemState.IconBackground.SEASONAL_EXPENSES -> {
            colorScheme.seasonalExpensesFamily.container to colorScheme.seasonalExpensesFamily.onContainer
        }

        CategorySectionItemState.IconBackground.INCOMES -> {
            colorScheme.incomesFamily.container to colorScheme.incomesFamily.onContainer
        }

        null -> {
            colorScheme.fixedExpensesFamily.container to colorScheme.fixedExpensesFamily.onContainer
        }
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(percent = 100))
            .background(color = colors.first)
            .size(48.dp)
            .padding(16.dp),
    ) {
        AsyncImage(
            modifier = Modifier.background(Color.Transparent),
            model = icon.image.value,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = colors.second),
        )
    }
}

@Composable
@ThemedPreview
private fun CategorySectionItemPreview() {
    PreviewTheme {
        CategorySectionItem(
            state = BudgetBuilderStepPreviewFixture.uiState.manuallyAdded.first(),
            onSelect = {},
        )
    }
}

internal object CategoryListItemDefaults {
    val topTextStyle
        @Composable get() = MaterialTheme.typography.bodyLarge
    val bottomTextStyle
        @Composable get() = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
}

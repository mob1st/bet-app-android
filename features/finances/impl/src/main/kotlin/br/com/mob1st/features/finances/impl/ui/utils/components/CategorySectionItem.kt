package br.com.mob1st.features.finances.impl.ui.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import br.com.mob1st.core.androidx.compose.ComposableFunction
import br.com.mob1st.core.design.molecules.icons.LeadingIcon
import br.com.mob1st.core.design.organisms.lists.selectableItem
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepPreviewFixture
import coil.compose.AsyncImage

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
        state.recurrences?.let { text ->
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
            Text(text = state.name)
        },
        supportingContent = decoratedSupportingContent,
        trailingContent = {
            Trailing(amount = state.amount)
        },
    )
}

@Composable
private fun Trailing(
    amount: CategorySectionItemState.Amount,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSurface,
        ) {
            ProvideTextStyle(value = CategoryListItemDefaults.topTextStyle) {
                Text(text = amount.amount.resolve())
            }
        }
        Text(
            text = amount.supporting.resolve(),
            style = CategoryListItemDefaults.bottomTextStyle,
        )
    }
}

@Composable
private fun Icon(
    icon: CategorySectionItemState.Icon,
) {
    val colorCombination = icon.combination.resolve()
    LeadingIcon(
        combination = colorCombination,
    ) {
        AsyncImage(
            modifier = Modifier.background(Color.Transparent),
            model = icon.image.value,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = LocalContentColor.current),
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

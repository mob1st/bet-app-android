package br.com.mob1st.features.finances.impl.ui.utils.components

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
import androidx.compose.ui.res.stringResource
import br.com.mob1st.core.androidx.compose.ComposableFunction
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.features.finances.impl.R

/**
 * Setup the list item for the category section.
 * It applies the proper alignment and typography for the components.
 * @param headlineContent the main content of the item. Usually it is the category name.
 * @param supportingContent the secondary content of the item. Usually it is the category recurrences.
 * @param trailingContent the trailing content of the item. Usually it is the category amount.
 */
@Composable
fun CategorySectionItem(
    modifier: Modifier = Modifier,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)?,
    trailingContent: (@Composable () -> Unit)?,
) {
    val decoratedHeadlineContent: ComposableFunction = {
        ProvideTextStyle(value = CategoryListItemDefaults.topTextStyle) {
            headlineContent()
        }
    }
    val decoratedSupportingContent: ComposableFunction? =
        supportingContent?.let {
            {
                ProvideTextStyle(value = CategoryListItemDefaults.bottomTextStyle) {
                    supportingContent()
                }
            }
        }
    val decoratedTrailingContent: ComposableFunction? =
        trailingContent?.let {
            {
                Trailing(trailingContent)
            }
        }
    ListItem(
        modifier = modifier,
        headlineContent = decoratedHeadlineContent,
        supportingContent = decoratedSupportingContent,
        trailingContent = decoratedTrailingContent,
    )
}

@Composable
private fun Trailing(
    trailingContent: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSurface,
        ) {
            ProvideTextStyle(value = CategoryListItemDefaults.topTextStyle) {
                trailingContent()
            }
        }
        Text(
            text = stringResource(id = R.string.finances_builder_commons_list_trailing_support),
            style = CategoryListItemDefaults.bottomTextStyle,
        )
    }
}

@Composable
@ThemedPreview
private fun CategorySectionItemPreview() {
    PreviewTheme {
        CategorySectionItem(
            headlineContent = {
                Text(text = "Headline")
            },
            supportingContent = {
                Text(text = "Supporting")
            },
            trailingContent = {
                Text(text = "$10.00")
            },
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

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
import br.com.mob1st.core.androidx.compose.ComposableFunction
import br.com.mob1st.core.design.atoms.theme.TwoCentsTheme
import br.com.mob1st.core.design.atoms.typography.NumericFontRoles
import br.com.mob1st.core.design.utils.ThemedPreview

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
    val decoratedSupportingContent: ComposableFunction? =
        supportingContent?.let {
            {
                ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
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
        headlineContent = headlineContent,
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
            ProvideTextStyle(value = NumericFontRoles.body.large) {
                trailingContent()
            }
        }
    }
}

@Composable
@ThemedPreview
private fun CategorySectionItemPreview() {
    TwoCentsTheme {
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

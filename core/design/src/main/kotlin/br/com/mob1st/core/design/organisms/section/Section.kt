package br.com.mob1st.core.design.organisms.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.utils.ThemedPreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

inline fun <T> LazyListScope.section(
    crossinline titleContent: @Composable () -> Unit,
    items: ImmutableList<T>,
    noinline key: ((item: T) -> Any)? = null,
    crossinline itemsContentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable (index: Int, item: T) -> Unit,
) {
    item(contentType = SectionTitleContentType) {
        SectionTitle {
            titleContent()
        }
    }
    val keyFunction = if (key != null) {
        { _: Int, item: T ->
            key(item)
        }
    } else {
        null
    }
    itemsIndexed(
        items = items,
        key = keyFunction,
        contentType = itemsContentType,
    ) { index, item ->
        itemContent(index, item)
    }
}

@Composable
fun SectionTitle(
    titleContent: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(Spacings.x12)
            .padding(horizontal = Spacings.x4),
        contentAlignment = Alignment.CenterStart,
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
            titleContent()
        }
    }
}

object SectionTitleContentType

@Composable
@ThemedPreview
private fun SectionPreview() {
    BetTheme {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            section(
                titleContent = {
                    Text("Title")
                },
                items = persistentListOf("Item 1", "Item 2", "Item 3"),
                itemContent = { _, item ->
                    ListItem(
                        headlineContent = { Text(item) },
                    )
                },
            )
        }
    }
}

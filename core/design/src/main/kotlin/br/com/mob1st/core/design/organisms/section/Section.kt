package br.com.mob1st.core.design.organisms.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun Section(
    titleContent: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        SectionTitle {
            titleContent()
        }
        content()
    }
}

@Composable
private fun SectionTitle(
    titleContent: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(48.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
            titleContent()
        }
    }
}

@Composable
@ThemedPreview
private fun SectionPreview() {
    BetTheme {
        Section(
            titleContent = {
                Text("Title")
            },
        ) {
            ListItem(headlineContent = { Text(text = "Item 1") })
            ListItem(headlineContent = { Text(text = "Item 1") })
            ListItem(headlineContent = { Text(text = "Item 1") })
        }
    }
}

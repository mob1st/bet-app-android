package br.com.mob1st.core.design.organisms.lists

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun BulletListItem(
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable () -> Unit,
) {
    ListItem(
        leadingContent = {
            Text(
                text = "•",
                fontSize = 32.sp,
            )
        },
        headlineContent = headlineContent,
        supportingContent = supportingContent,
    )
}

@Composable
@ThemedPreview
private fun BulletListItemPreview() {
    BulletListItem(
        headlineContent = {
            Text("Headline")
        },
        supportingContent = {
            Text("Supporting")
        },
    )
}

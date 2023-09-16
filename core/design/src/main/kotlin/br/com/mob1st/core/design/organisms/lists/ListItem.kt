package br.com.mob1st.core.design.organisms.lists

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.com.mob1st.core.design.atoms.properties.texts.Text
import br.com.mob1st.core.design.atoms.properties.texts.rememberAnnotatedString
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.molecules.loading.Loading
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun ListItem(state: ListItemState, modifier: Modifier = Modifier) {
    val headline = rememberAnnotatedString(text = state.headline)
    val supportingText: @Composable (() -> Unit)? = state.supporting?.let { text ->
        @Composable {
            SupportingText(text = text)
        }
    }
    val leading: @Composable (() -> Unit)? = state.leading?.let {
        @Composable {
            // LeadingAvatar(avatarState = avatarState)
        }
    }
    val trailing: @Composable (() -> Unit)? = state.trailing?.let {
        @Composable {
            Trailing(trailing = it)
        }
    }
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(headline)
        },
        supportingContent = supportingText,
        trailingContent = trailing,
        leadingContent = leading
    )
}

@Composable
private fun SupportingText(text: Text) {
    val supporting = rememberAnnotatedString(text = text)
    Text(text = supporting)
}

@Composable
private fun Trailing(trailing: ListItemState.Trailing) {
    when (trailing) {
        is ListItemState.Text -> {
            Text(text = trailing.value.resolve(LocalContext.current.resources))
        }
        is ListItemState.ActionIcon -> {}
        is ListItemState.Loading -> {
            Loading(
                isLoading = true,
                crossfadeLabel = "item loading"
            ) {
            }
        }
    }
}

@Composable
@ThemedPreview
private fun ListItemPreview(@PreviewParameter(ListItemParameterProvider::class) state: ListItemState) {
    BetTheme {
        ListItem(state = state)
    }
}

private class ListItemParameterProvider : PreviewParameterProvider<ListItemState> {
    override val values: Sequence<ListItemState> = sequenceOf(
        ListItemState(
            headline = Text("Headline"),
            supporting = Text("Supporting"),
            leading = null,
            trailing = ListItemState.Text(Text("Trailing"))
        ),
        ListItemState(
            headline = Text("Headline"),
            leading = null,
            trailing = ListItemState.Text(Text("Trailing"))
        ),
        ListItemState(
            headline = Text("Headline"),
            leading = null,
            trailing = ListItemState.Loading
        ),
        ListItemState(
            headline = Text("Headline"),
            supporting = Text("Supporting"),
            leading = null,
            trailing = null
        ),
        ListItemState(
            headline = Text("Headline"),
            supporting = null,
            leading = null,
            trailing = null
        )
    )
}

package br.com.mob1st.core.design.organisms.lists

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.mob1st.core.design.atoms.properties.Text
import br.com.mob1st.core.design.atoms.properties.rememberAnnotatedString
import br.com.mob1st.core.design.molecules.avatar.AvatarState

@Composable
fun ListItem(state: ListItemState, modifier: Modifier = Modifier) {
    val headline = rememberAnnotatedString(text = state.headline)
    val supportingText: @Composable (() -> Unit)? = state.supporting?.let { text ->
        @Composable {
            SupportingText(text = text)
        }
    }
    val leading: @Composable (() -> Unit)? = state.leading?.let { avatarState ->
        @Composable {
            LeadingAvatar(avatarState = avatarState)
        }
    }
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(headline)
        },
        supportingContent = supportingText,
        leadingContent = leading
    )
}

@Composable
private fun SupportingText(text: Text) {
    val supporting = rememberAnnotatedString(text = text)
    Text(text = supporting)
}

@Composable
private fun LeadingAvatar(avatarState: AvatarState) {
}

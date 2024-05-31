package br.com.mob1st.bet.core.ui.ds.page

import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.ui.ds.templates.InfoTemplate
import br.com.mob1st.bet.core.ui.state.SimpleMessage

@Composable
fun DefaultErrorPage(
    message: SimpleMessage,
    onTryAgain: (message: SimpleMessage) -> Unit,
) {
    InfoTemplate(
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_warning_48),
                contentDescription = stringResource(id = R.string.icon_failure_content_description),
            )
        },
        title = { Text(stringResource(id = R.string.general_message_error_title)) },
        description = { Text(text = stringResource(id = message.descriptionResId)) },
        action = {
            Button(onClick = { onTryAgain(message) }) {
                Text(text = stringResource(id = R.string.try_again))
            }
        },
    )
}

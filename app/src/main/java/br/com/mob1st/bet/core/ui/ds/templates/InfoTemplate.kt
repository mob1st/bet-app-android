package br.com.mob1st.bet.core.ui.ds.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import br.com.mob1st.bet.core.ui.ds.atoms.Dimens

/**
 * Base template for information screens, typically used for error states or to provide some
 * contextualization about something important to the user
 *
 * @param icon the top icon used in the screen. It's typically an [androidx.compose.material3.Icon]
 * @param title the title of the information. It's typically a [androidx.compose.material3.Text]
 * @param description the main information in the UI to provide the context off the information.
 * It's typically a [androidx.compose.material3.Text]
 * @param action the optinal action the user can do after to get out of this screen. It's typically
 * a [androidx.compose.material3.Button]
 */
@Composable
fun InfoTemplate(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    action: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(Dimens.grid.margin),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.height(Dimens.grid.line.times(8))) {
            icon()
        }
        Spacer(modifier = Modifier.size(Dimens.grid.line))
        ProvideTextStyle(value = MaterialTheme.typography.headlineMedium) {
            title()
        }
        Spacer(modifier = Modifier.size(Dimens.grid.line))
        ProvideTextStyle(
            value = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
        ) {
            description()
        }
        Spacer(modifier = Modifier.size(Dimens.grid.line.times(4)))
        action?.let { action() }
    }
}

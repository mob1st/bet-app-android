package br.com.mob1st.core.design.organisms.helper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.atoms.theme.BetTheme

@Composable
fun Helper(
    titleContent: @Composable () -> Unit,
    descriptionContent: @Composable () -> Unit,
    buttonContent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.headlineMedium) {
            titleContent()
        }
        Spacer(modifier = Modifier.size(Spacings.x3))
        ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
            descriptionContent()
        }
        Spacer(modifier = Modifier.size(Spacings.x5))
        buttonContent()
    }
}

@Composable
@Preview
private fun HelperPreview() =
    BetTheme {
        Helper(
            titleContent = {
                Text(text = "Title")
            },
            descriptionContent = {
                Text(text = "This is a description for the helper")
            },
            buttonContent = {
                Text(text = "Action")
            },
        )
    }

package br.com.mob1st.core.design.organisms.helper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.molecules.buttons.PrimaryButton
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun Helper(
    modifier: Modifier = Modifier,
    imageContent: @Composable () -> Unit,
    titleContent: @Composable () -> Unit,
    descriptionContent: @Composable () -> Unit,
    buttonContent: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        imageContent()
        ProvideTextStyle(
            value = MaterialTheme
                .typography
                .displaySmall
                .copy(
                    textAlign = TextAlign.Center,
                ),
        ) {
            titleContent()
        }
        Spacer(modifier = Modifier.size(Spacings.x4))
        ProvideTextStyle(
            value = MaterialTheme
                .typography
                .bodySmall
                .copy(
                    textAlign = TextAlign.Center,
                ),
        ) {
            descriptionContent()
        }
        Spacer(modifier = Modifier.size(Spacings.x8))
        buttonContent()
    }
}

@Composable
@ThemedPreview
private fun HelperPreview() {
    BetTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Helper(
                imageContent = {
                },
                titleContent = {
                    Text(text = "TITLE")
                },
                descriptionContent = {
                    Text(text = "This is a description for the helper")
                },
                buttonContent = {
                    PrimaryButton(onClick = { /*TODO*/ }) {
                        Text(text = "Setup Budget")
                    }
                },
            )
        }
    }
}

package br.com.mob1st.features.finances.impl.ui.builder.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute

@Composable
fun BuilderIntroPage(
    onNext: (BuilderRoute) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {}) {
            Text(text = "Next")
        }
    }
}

@Composable
@ThemedPreview
private fun BuilderIntroPagePreview() {
    BuilderIntroPage(
        onNext = {},
    )
}

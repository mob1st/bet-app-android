package br.com.mob1st.features.onboarding.impl.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LauncherPage() {
    Box {
        Text(text = "LauncherPage")
    }
}

@Composable
@Preview
private fun LauncherPagePreview() {
    LauncherPage()
}

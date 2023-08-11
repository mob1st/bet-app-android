package br.com.mob1st.features.home.impl.ui

import androidx.activity.compose.ReportDrawn
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun HomePage(onClick: () -> Unit = {}) {
    Column {
        Text("Home")
    }
    ReportDrawn()
}

@Preview
@Composable
private fun HomePagePreview() {
    HomePage()
}

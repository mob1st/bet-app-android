package br.com.mob1st.bet.features.home.impl.presentation

import androidx.activity.compose.ReportDrawn
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomePage() {
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

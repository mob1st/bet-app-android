package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.com.mob1st.bet.core.ui.ds.InfoTemplate

@Composable
fun CompetitionsTabScreen() {
    InfoTemplate(
        icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "competition") },
        title = { Text("Aba de competições") },
        description = { Text("Trabalho em progresso. Volte aqui mais tarde para criar apostas") },
    )
}
package br.com.mob1st.bet.features.groups

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.com.mob1st.bet.core.ui.ds.InfoTemplate

@Composable
fun GroupsTabScreen() {
    InfoTemplate(
        icon = { Icon(imageVector = Icons.Default.Star, contentDescription = "group") },
        title = { Text("Aba de grupos") },
        description = { Text("Trabalho em progresso. Volte aqui mais tarde para criar grupos") },
    )
}
package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import br.com.mob1st.bet.core.localization.getText
import br.com.mob1st.bet.core.ui.ds.InfoTemplate
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry

@Composable
fun CompetitionsTabScreen(entry: CompetitionEntry) {
    val context = LocalContext.current
    InfoTemplate(
        icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "competition") },
        title = { Text("Aba de competições") },
        description = { Text("Em breve todos os jogos da ${context.getText(entry.name)} aqui") },
    )
}
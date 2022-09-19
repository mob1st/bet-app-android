package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.bet.core.localization.getText
import br.com.mob1st.bet.core.ui.ds.InfoTemplate
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun CompetitionsTabScreen(entry: CompetitionEntry) {
    val viewModel = koinViewModel<ConfrontationListViewModel> {
        parametersOf(entry)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val name = state.data.entry.name
    val count = state.data.confrontations.size
    val context = LocalContext.current
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Crossfade(targetState = state.loading) { target ->
            if (target) {
                CircularProgressIndicator()
            } else {
                InfoTemplate(
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "competition") },
                    title = { Text("Aba de competições") },
                    description = { Text("Em breve todos os jogos da ${context.getText(name)} aqui com $count partidas") },
                )
            }
        }
    }

}
package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.mob1st.bet.core.utils.objects.Node
import br.com.mob1st.bet.features.competitions.domain.Contest
import br.com.mob1st.bet.features.competitions.domain.Duel
import br.com.mob1st.bet.features.competitions.domain.IntScores
import br.com.mob1st.bet.features.competitions.domain.MatchWinner

@Composable
fun NodeComponent(
    node: Node<Contest>,
    isLast: Boolean,
    onClickNext: () -> Unit
) {
    var selected: Duel.Selection? by remember {
        mutableStateOf(null)
    }
    val paths = remember {
        mutableStateListOf(node.current)
    }
    val matchWinnerComponent = @Composable { matchWinner: MatchWinner ->
        MatchWinnerComponent(
            matchWinner = matchWinner,
            selected = selected,
            onSelectScore = { selection ->
                selected = selection
                if (selection != null) {
                    paths.add(node.paths[selection.pathIndex].current)
                } else {
                    paths.removeLast()
                }
            },
        )
    }
    val intScoresComponent = @Composable { intScores: IntScores ->
        ScoresChipComponent(scores = intScores)
    }
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
    ) {
        paths.forEach { contest ->
            key(contest) {
                ContestComponent(
                    contest = contest,
                    matchWinnerComponent = matchWinnerComponent,
                    intScoresComponent = intScoresComponent
                )
            }
        }
    }

}

@Composable
private fun ContestComponent(
    contest: Contest,
    matchWinnerComponent: @Composable (MatchWinner) -> Unit,
    intScoresComponent: @Composable (IntScores) -> Unit
) {
    when (contest) {
        is IntScores -> {
            intScoresComponent(contest)
        }
        is MatchWinner -> {
            matchWinnerComponent(contest)
        }
    }
}
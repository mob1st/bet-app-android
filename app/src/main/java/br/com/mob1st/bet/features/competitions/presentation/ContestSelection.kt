package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.mob1st.bet.core.tooling.ktx.Node
import br.com.mob1st.bet.core.ui.ds.atoms.LocalCompositionGrid
import br.com.mob1st.bet.features.competitions.domain.Contest
import br.com.mob1st.bet.features.competitions.domain.IntScores
import br.com.mob1st.bet.features.competitions.domain.MatchWinner

@Composable
fun NodeComponent(
    root: Node<Contest>,
    input: ConfrontationInput,
    onInput: (ConfrontationInput) -> Unit
) {
    val matchWinner = root.current as MatchWinner

    MatchWinnerComponent(
        matchWinner = matchWinner,
        selected = input.winner,
        onSelectScore = { newSelected ->
            onInput(input.selectWinner(newSelected))
        }
    )

    AnimatedVisibility(visible = input.scoresVisible) {
        val intScores = root.paths[input.winner!!.pathIndex].current as IntScores
        ScoresChipComponent(
            modifier = Modifier.padding(top = LocalCompositionGrid.current.line * 4),
            scores = intScores,
            selected = input.score,
            onSelect = {
                onInput(input.copy(score = it))
            }
        )
    }
}

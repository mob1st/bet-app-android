package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.mob1st.bet.core.ui.ds.atoms.CompositionLocalGrid
import br.com.mob1st.bet.core.utils.objects.Node
import br.com.mob1st.bet.features.competitions.domain.Contest
import br.com.mob1st.bet.features.competitions.domain.IntScores
import br.com.mob1st.bet.features.competitions.domain.MatchWinner

@Composable
fun NodeComponent(
    root: Node<Contest>,
) {
    val matchWinner = root.current as MatchWinner
    var input: ConfrontationInput by rememberSaveable {
        mutableStateOf(ConfrontationInput())
    }

    MatchWinnerComponent(
        matchWinner = matchWinner,
        selected = input.winner,
        onSelectScore = { newSelected ->
            input = input.selectWinner(newSelected)
        },
    )

    AnimatedVisibility(visible = input.scoresVisible) {
        val intScores = root.paths[input.winner!!.pathIndex].current as IntScores
        ScoresChipComponent(
            modifier = Modifier.padding(CompositionLocalGrid.current.line * 4),
            scores = intScores,
            selected = input.score,
            other = null,
            onSelect = {
                input = input.copy(score = it)
            }
        )
    }

}
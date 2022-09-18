package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import br.com.mob1st.bet.core.utils.objects.Duo
import br.com.mob1st.bet.features.competitions.domain.Bet
import br.com.mob1st.bet.features.competitions.domain.IntScores

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoresChipComponent(
    scores: IntScores
) {
    Row {
        scores.contenders.forEach { bet ->
            key(bet.subject) {
                ScoreChip(bet = bet, selected = false, onSelect = {})
            }
        }
        InputChip(
            selected = false,
            onClick = { /*TODO*/ },
            label = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreChip(
    bet: Bet<Duo<Int>>,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    val subject = bet.subject
    InputChip(
        selected = selected,
        onClick = onSelect,
        label = { Text(text = "${subject.first} X ${subject.second}") }
    )
}
package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.ui.ds.atoms.BetTheme
import br.com.mob1st.bet.core.ui.ds.atoms.CompositionLocalGrid
import br.com.mob1st.bet.core.utils.objects.Duo
import br.com.mob1st.bet.features.competitions.domain.American
import br.com.mob1st.bet.features.competitions.domain.Bet
import br.com.mob1st.bet.features.competitions.domain.IntScores

@Composable
@Preview
fun ScoresChipComponentPreview() {
    val mock = IntScores(
        contenders = listOf(
            Bet(
                subject = 1 to 0,
                odds = American(1)
            ),
            Bet(
                subject = 2 to 0,
                odds = American(1)
            ),
            Bet(
                subject = 2 to 1,
                odds = American(1)
            )
        )
    )
    BetTheme(systemBars = { /*TODO*/ }) {
        ScoresChipComponent(scores = mock, selected = null, onSelect = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoresChipComponent(
    modifier: Modifier = Modifier,
    scores: IntScores,
    selected: Duo<Int>? = null,
    other: Duo<Int>? = null,
    onSelect: (Duo<Int>?) -> Unit
) {

    val selectOrReselect = { duo: Duo<Int> ->
        if (duo == selected) {
            onSelect(null)
        } else {
            onSelect(duo)
        }
    }

    Column(modifier) {
        Text(
            text = stringResource(id = R.string.confrontation_detail_score_header),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(CompositionLocalGrid.current.line * 2)
        )

        Row {
            scores.contenders.forEach { bet ->
                key(bet.subject) {
                    ScoreChip(
                        bet = bet,
                        selected = bet.subject == selected,
                        onSelect = { selectOrReselect(bet.subject) }
                    )
                }
                Spacer(modifier = Modifier.width(CompositionLocalGrid.current.gutter))
            }
            InputChip(
                selected = other!= null && other == selected,
                onClick = { /*TODO*/ },
                label = {
                    Text(
                        other?.toScoreText() ?: stringResource(id = R.string.confrontation_detail_score_other)
                    )
                },
            )
        }
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
        label = {
            Text(text = subject.toScoreText())
        }
    )
}

private fun Duo<Int>.toScoreText() = "$first X $second"
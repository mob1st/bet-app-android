package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.localization.getText
import br.com.mob1st.bet.core.ui.ds.atoms.BetTheme
import br.com.mob1st.bet.core.ui.ds.atoms.CompositionLocalGrid
import br.com.mob1st.bet.features.competitions.domain.American
import br.com.mob1st.bet.features.competitions.domain.Bet
import br.com.mob1st.bet.features.competitions.domain.Duel
import br.com.mob1st.bet.features.competitions.domain.MatchWinner
import br.com.mob1st.bet.features.competitions.domain.Odds
import br.com.mob1st.bet.features.competitions.domain.Team
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
@Preview(showBackground = true)
fun MatchWinnerComponentPreview() {
    val mock = MatchWinner(
        contender1 = Bet(
            odds = American(1),
            subject = Team(
                id = "1",
                name = mapOf("en-us" to "Brazil"),
                url = "https://countryflagsapi.com/png/br"
            )
        ),
        contender2 = Bet(
            odds = American(1),
            subject = Team(
                id = "2",
                name = mapOf("en-us" to "Argentina"),
                url = "https://countryflagsapi.com/png/ar"
            )
        ),
        draw = Bet(
            odds = American(1),
            subject = "DRAW"
        )
    )
    BetTheme(systemBars = { /*TODO*/ }) {
        MatchWinnerComponent(
            matchWinner = mock,
            onSelectScore = { }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchWinnerComponent(
    matchWinner: MatchWinner,
    selected: Duel.Selection? = null,
    onSelectScore: (Duel.Selection) -> Unit
) {

    Column(
        modifier = Modifier
            .width(CompositionLocalGrid.current.columns(4))
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.confrontation_detail_header),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(CompositionLocalGrid.current.line * 4))

        InputChip(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            selected = selected == Duel.Selection.CONTENDER_1,
            onClick = { onSelectScore(Duel.Selection.CONTENDER_1) },
            trailingIcon = { OddsText(odds = matchWinner.contender1.odds) },
            leadingIcon = { ContenderImage(team = matchWinner.contender1.subject) },
            label = { ContenderText(team = matchWinner.contender1.subject) }
        )

        InputChip(
            modifier = Modifier.fillMaxWidth(),
            selected = selected == Duel.Selection.CONTENDER_2,
            onClick = { onSelectScore(Duel.Selection.CONTENDER_2) },
            trailingIcon = { OddsText(odds = matchWinner.contender2.odds) },
            leadingIcon = { ContenderImage(team = matchWinner.contender2.subject) },
            label = { ContenderText(team = matchWinner.contender2.subject) }
        )

        InputChip(
            modifier = Modifier.fillMaxWidth(),
            selected = selected == Duel.Selection.DRAW,
            onClick = { onSelectScore(Duel.Selection.DRAW) },
            trailingIcon = { OddsText(odds = matchWinner.draw.odds) },
            leadingIcon = { Icon(imageVector = Icons.Filled.Warning, contentDescription = null) },
            label = { Text(text = stringResource(id = R.string.draw)) }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContenderImage(team: Team) {
    AsyncImage(
        modifier = Modifier.size(InputChipDefaults.AvatarSize),
        model = ImageRequest.Builder(LocalContext.current)
            .data(team.url)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun ContenderText(team: Team) {
    Text(text = LocalContext.current.getText(team.name))
}

@Composable
private fun OddsText(
    odds: Odds
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "${odds.value}x",
        textAlign = TextAlign.End,
    )
}
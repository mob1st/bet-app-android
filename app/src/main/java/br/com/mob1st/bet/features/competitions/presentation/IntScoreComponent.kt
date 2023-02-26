package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.tooling.ktx.Duo
import br.com.mob1st.bet.core.ui.ds.atoms.BetTheme
import br.com.mob1st.bet.core.ui.ds.atoms.LocalCompositionGrid
import br.com.mob1st.bet.features.competitions.domain.American
import br.com.mob1st.bet.features.competitions.domain.Bet
import br.com.mob1st.bet.features.competitions.domain.IntScores

@Composable
@Preview
private fun ScoresChipComponentPreview() {
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

@Composable
@Preview
private fun ScoreDialogPreview() {
    BetTheme(systemBars = { /*TODO*/ }) {
        ScoreDialog(
            currentScore = 1 to 0,
            onDone = { },
            onDismiss = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoresChipComponent(
    modifier: Modifier = Modifier,
    scores: IntScores,
    selected: Duo<Int>? = null,
    onSelect: (Duo<Int>?) -> Unit
) {
    var isOtherDialogOpened by remember {
        mutableStateOf(false)
    }
    val selectOrReselect = { duo: Duo<Int> ->
        if (duo == selected) {
            onSelect(null)
        } else {
            onSelect(duo)
        }
    }

    val isOtherSelected = selected != null && !scores.contenders.any { it.subject == selected }

    Column(modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.confrontation_detail_score_header),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(LocalCompositionGrid.current.line * 2)
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
                Spacer(modifier = Modifier.width(LocalCompositionGrid.current.gutter))
            }
            InputChip(
                selected = selected != null && !scores.contenders.any { it.subject == selected },
                onClick = {
                    isOtherDialogOpened = true
                },
                label = {
                    Text(
                        if (isOtherSelected) {
                            selected!!.toScoreText()
                        } else {
                            stringResource(id = R.string.confrontation_detail_score_other)
                        }
                    )
                }
            )
        }
    }

    if (isOtherDialogOpened) {
        ScoreDialog(
            currentScore = if (isOtherSelected) selected!! else null,
            onDone = {
                isOtherDialogOpened = false
                onSelect(it)
            },
            onDismiss = { isOtherDialogOpened = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreChip(
    bet: Bet<Duo<Int>>,
    selected: Boolean,
    onSelect: () -> Unit
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

@Composable
fun ScoreDialog(
    currentScore: Duo<Int>?,
    onDone: (Duo<Int>) -> Unit,
    onDismiss: () -> Unit
) {
    val firstRequester = remember {
        FocusRequester()
    }
    val secondRequester = remember {
        FocusRequester()
    }
    var firstScore by remember(currentScore?.first) {
        mutableStateOf(currentScore?.first?.toString().orEmpty())
    }
    var secondScore by remember(currentScore?.second) {
        mutableStateOf(currentScore?.second?.toString().orEmpty())
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        confirmButton = {
            DialogConfirmButton(
                firstScore = firstScore,
                secondScore = secondScore,
                onDone = onDone,
                onDismiss = onDismiss
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.confrontation_detail_score_header))
        },
        text = {
            DialogText(
                firstScore = firstScore,
                secondScore = secondScore,
                firstRequester = firstRequester,
                secondRequester = secondRequester,
                onFirstIme = {
                    firstScore = it
                    secondRequester.requestFocus()
                },
                onSecondIme = {
                    secondScore = it
                    if (firstScore.isNotEmpty() && secondScore.isNotEmpty()) {
                        onDone(firstScore.toInt() to secondScore.toInt())
                    } else {
                        firstRequester.requestFocus()
                    }
                }
            )
        }
    )
    if (firstScore.isEmpty()) {
        LaunchedEffect(Unit) {
            firstRequester.requestFocus()
        }
    }
}

@Composable
private fun DialogConfirmButton(
    firstScore: String,
    secondScore: String,
    onDone: (Duo<Int>) -> Unit,
    onDismiss: () -> Unit
) {
    TextButton(
        onClick = {
            if (firstScore.isNotEmpty() && secondScore.isNotEmpty()) {
                onDone(firstScore.toInt() to secondScore.toInt())
            } else {
                onDismiss()
            }
        }
    ) {
        Text(text = stringResource(id = R.string.done))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DialogText(
    firstScore: String,
    secondScore: String,
    firstRequester: FocusRequester,
    secondRequester: FocusRequester,
    onFirstIme: (value: String) -> Unit,
    onSecondIme: (value: String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreTextField(
            modifier = Modifier
                .focusRequester(firstRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show()
                    }
                },
            value = firstScore,
            imeAction = ImeAction.Next,
            onIme = {
                onFirstIme(it)
            }
        )
        Text(text = "X")
        ScoreTextField(
            modifier = Modifier.focusRequester(secondRequester),
            value = secondScore,
            imeAction = ImeAction.Done,
            onIme = {
                onSecondIme(it)

            }
        )
    }
}

@Composable
private fun ScoreTextField(
    modifier: Modifier = Modifier,
    value: String,
    imeAction: ImeAction,
    onIme: (value: String) -> Unit
) {
    var state by remember(value) {
        mutableStateOf(value)
    }
    Box(
        modifier = modifier
            .width(LocalCompositionGrid.current.column)
            .height(LocalCompositionGrid.current.column)
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(2.dp)),
        contentAlignment = Alignment.Center
    ) {
        val currentTextStyle = LocalTextStyle.current
        BasicTextField(
            modifier = Modifier.wrapContentSize(),
            value = state,
            textStyle = currentTextStyle.copy(textAlign = TextAlign.Center),
            onValueChange = {
                state = it
                // jump to the next field
                onIme(state)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = KeyboardType.Number,
                autoCorrect = false
            ),
            keyboardActions = KeyboardActions(
                onNext = { onIme(state) },
                onDone = { onIme(state) }
            )
        )
    }
}

private fun Duo<Int>.toScoreText() = "$first X $second"

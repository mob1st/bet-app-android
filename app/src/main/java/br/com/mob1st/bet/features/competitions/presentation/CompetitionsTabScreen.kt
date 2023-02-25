package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.bet.core.localization.getText
import br.com.mob1st.bet.core.ui.compose.LocalLazyListState
import br.com.mob1st.bet.core.ui.compose.LocalSnackbarState
import br.com.mob1st.bet.core.ui.ds.atoms.CompositionLocalGrid
import br.com.mob1st.bet.core.ui.ds.molecule.RetrySnackbar
import br.com.mob1st.bet.core.ui.ds.organisms.FetchedCrossfade
import br.com.mob1st.bet.core.ui.ds.page.DefaultErrorPage
import br.com.mob1st.bet.core.ui.ds.templates.InfoTemplate
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.tooling.ktx.ifNotEmpty
import br.com.mob1st.bet.features.competitions.domain.IntScores
import br.com.mob1st.bet.features.competitions.domain.MatchWinner

@Composable
fun CompetitionsTabScreen(
    viewModel: ConfrontationListViewModel,
    navigateTo: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val updatedNavigateTo by rememberUpdatedState(navigateTo)
    ConfrontationsPage(
        state = state,
        onTryAgain = { viewModel.fromUi(ConfrontationUiEvent.TryAgain(it)) },
        onSelect = { viewModel.fromUi(ConfrontationUiEvent.SetSelection(it)) },
        onDismiss = { viewModel.messageShown(it) }
    )
    state.data.selected?.let { index ->
        LaunchedEffect(index) {
            updatedNavigateTo(index)
        }
    }
}

@Composable
fun ConfrontationsPage(
    state: AsyncState<ConfrontationData>,
    onTryAgain: (SimpleMessage) -> Unit,
    onSelect: (Int) -> Unit,
    onDismiss: (SimpleMessage) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        FetchedCrossfade(
            state = state,
            emptyError = { _, message -> DefaultErrorPage(message, onTryAgain) },
            emptyLoading = { EmptyLoading() },
            empty = { ConfrontationsEmptyData() },
            data = {
                ConfrontationsData(
                    state = it,
                    onSelect = onSelect,
                    onTryAgain = onTryAgain,
                    onDismiss = onDismiss
                )
            }
        )
    }
}

@Composable
fun EmptyLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ConfrontationsEmptyData() {
    InfoTemplate(
        icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "competition") },
        title = { Text("Sem competições") },
        description = { Text("Atualize o app meu mano pra ve se tem outros campeonato") }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConfrontationsData(
    state: AsyncState<ConfrontationData>,
    onSelect: (Int) -> Unit,
    onTryAgain: (SimpleMessage) -> Unit,
    onDismiss: (SimpleMessage) -> Unit,
) {
    state.messages.ifNotEmpty {
        RetrySnackbar(
            snackbarHostState = LocalSnackbarState.current,
            message = stringResource(id = it.descriptionResId),
            onDismiss = { onDismiss(it) },
            onRetry = { onTryAgain(it) }
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = LocalLazyListState.current,
    ) {
        stickyHeader {
            Surface(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
            ) {
                val context = LocalContext.current
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = CompositionLocalGrid.current.line * 4)
                        .padding(horizontal = CompositionLocalGrid.current.margin),
                    text = context.getText(state.data.subscription.competition.name),
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
        itemsIndexed(
            state.data.confrontations,
            key = { _, item -> item.id },
        ) { index, item ->
            when (val contest = item.contest.current) {
                is IntScores -> IntScoresItem(contest)
                is MatchWinner -> MatchWinnerItem(index, contest, onSelect)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MatchWinnerItem(
    index: Int,
    matchWinner: MatchWinner,
    onSelect: (Int) -> Unit,
) {

    ListItem(
        modifier = Modifier.clickable { onSelect(index) },
        headlineText = {
            val context = LocalContext.current
            val team1 = context.getText(matchWinner.contender1.subject.name)
            val team2 = context.getText(matchWinner.contender2.subject.name)
            Text(text = "${index + 1}° $team1 X $team2")
        },
    )
    Divider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntScoresItem(
    intScores: IntScores,
) {
    Row {
        intScores.contenders.forEachIndexed { _, bet ->
            SuggestionChip(
                onClick = { /*TODO*/ },
                label = {
                    Text(text = "${bet.subject.first} X ${bet.subject.second}")
                }
            )
        }
    }
}
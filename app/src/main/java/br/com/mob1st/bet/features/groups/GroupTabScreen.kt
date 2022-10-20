package br.com.mob1st.bet.features.groups

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.bet.core.ui.compose.LocalLazyListState
import br.com.mob1st.bet.core.ui.compose.LocalSnackbarState
import br.com.mob1st.bet.core.ui.ds.molecule.AddButton
import br.com.mob1st.bet.core.ui.ds.molecule.RetrySnackbar
import br.com.mob1st.bet.core.ui.ds.organisms.FetchedCrossfade
import br.com.mob1st.bet.core.ui.ds.organisms.GroupRow
import br.com.mob1st.bet.core.ui.ds.page.DefaultErrorPage
import br.com.mob1st.bet.core.ui.ds.templates.InfoTemplate
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.utils.extensions.ifNotEmpty

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun GroupsTabScreen(
    viewModel: GroupTabViewModel,
    onNavigateToCreateGroups: () -> Unit,
    onNavigateToGroupDetails: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listGroups = viewModel.groups

    LaunchedEffect(key1 = true) {
        listGroups.collect() {
            viewModel.getGroups()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top ,
        modifier = Modifier.fillMaxSize()
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineMedium){
            Text("Seus grupos")
        }
        AddButton(onNavigateToCreateGroups)

        GroupsPage(
            state = state,
            onTryAgain = { viewModel.fromUi(GroupsUIEvent.TryAgain(it)) } ,
            onDismiss = { viewModel.messageShown(it) }
        )
    }
}

@Composable
fun GroupsPage(
    state: AsyncState<GroupsListData>,
    onTryAgain: (SimpleMessage) -> Unit,
    onDismiss: (SimpleMessage) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        FetchedCrossfade(
            state = state,
            emptyError = {_, message -> DefaultErrorPage(message, onTryAgain)},
            emptyLoading = { EmptyLoading() },
            empty = { GroupsEmptyData() },
            data = {
                GroupsData(
                    state = it,
                    onTryAgain = onTryAgain,
                    onDismiss = onDismiss
                )
            }
        )
    }
}

@Composable
fun GroupsData(
    state: AsyncState<GroupsListData>,
    onTryAgain: (SimpleMessage) -> Unit,
    onDismiss: (SimpleMessage) -> Unit
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
        state = LocalLazyListState.current
    ) {
        itemsIndexed(
            state.data.groupList,
            key = { _, item -> item.id },
        ) { _, item ->
            GroupRow(groupName = item.name, currentMembersNumber = 4, maxMembers = 4) {}
        }
    }
}

@Composable
fun EmptyLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun GroupsEmptyData() {
    InfoTemplate(
        icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "groups tab") },
        title = { Text("Sem grupos") },
        description = { Text("Tente criar ou entrar em algum grupo.") }
    )
}
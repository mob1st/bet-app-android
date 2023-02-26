package br.com.mob1st.bet.features.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.mob1st.bet.core.ui.ds.molecule.AddButton
import br.com.mob1st.bet.core.ui.ds.organisms.GroupRow

@Composable
fun GroupsTabScreen(
    onNavigateToCreateGroups: () -> Unit,
    onNavigateToGroupDetails: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
            Text("Seus grupos")
        }
        AddButton(onNavigateToCreateGroups)
        GroupRow(
            groupName = "Primeiro grupo",
            currentMembersNumber = 1,
            maxMembers = 25,
            onNavigateToGroupDetails
        )
        GroupRow(
            groupName = "Segundo grupo",
            currentMembersNumber = 3,
            maxMembers = 25,
            onNavigateToGroupDetails
        )
    }
}

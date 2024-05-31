package br.com.mob1st.bet.core.ui.ds.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.mob1st.bet.core.ui.ds.atoms.PurpleGrey40
import br.com.mob1st.bet.core.ui.ds.molecule.GroupIcon

@Composable
fun GroupRow(
    groupName: String,
    currentMembersNumber: Int,
    maxMembers: Int,
    onNavigateToGroupDetails: () -> Unit,
) {
    Divider(color = Color.White, thickness = 1.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier =
            Modifier.fillMaxWidth()
                .height(50.dp)
                .background(PurpleGrey40).clickable { onNavigateToGroupDetails() },
    ) {
        GroupIcon()
        Text(groupName, color = Color.White)
        Text("$currentMembersNumber/$maxMembers", color = Color.White)
    }
}

@Composable
@Preview
fun GroupRowPreview() {
    // GroupRow(groupName = "Primeiro grupo", currentMembersNumber = 2, maxMembers = 25, () -> Unit)
}

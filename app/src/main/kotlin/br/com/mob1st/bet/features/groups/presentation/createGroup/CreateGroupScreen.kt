package br.com.mob1st.bet.features.groups.presentation.createGroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.mob1st.bet.core.ui.ds.molecule.AddButton
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(onCreateGroupAction: () -> Unit) {
    val viewModel = koinViewModel<CreateGroupViewModel>()

    var groupName by remember {
        mutableStateOf("")
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Criação de Grupo")
        OutlinedTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text(text = "Group Name") },
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            colors = TextFieldDefaults.outlinedTextFieldColors(Color.Black),
        )
        AddButton(
            onAction = {
                viewModel.fromUi(GroupsUIEvent.CreateGroup(groupName))
                onCreateGroupAction()
            },
        )
    }
}

package br.com.mob1st.bet.features.groups.createGroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import br.com.mob1st.bet.core.ui.ds.molecule.AddButton
import br.com.mob1st.bet.core.ui.ds.molecule.InputText

@Composable
fun CreateGroupScreen() {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Criação de Grupo")
        InputText()
        AddButton {}
    }
}
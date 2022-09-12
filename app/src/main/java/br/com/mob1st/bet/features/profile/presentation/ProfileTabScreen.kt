package br.com.mob1st.bet.features.profile.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.com.mob1st.bet.core.ui.ds.InfoTemplate

@Composable
fun ProfileTabScreen() {
    InfoTemplate(
        icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "group") },
        title = { Text("Aba de grupos") },
        description = { Text("Trabalho em progresso. Volte aqui editar o seu perfil") },
    )
}
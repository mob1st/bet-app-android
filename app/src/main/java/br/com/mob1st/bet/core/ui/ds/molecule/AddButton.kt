package br.com.mob1st.bet.core.ui.ds.molecule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.mob1st.bet.core.ui.ds.atoms.Purple40

@Composable
fun AddButton(
    onNavigate: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(
            onClick = { onNavigate() },
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            border = BorderStroke(5.dp, Purple40),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color.Blue)
        ) {
            Icon(Icons.Default.Add, contentDescription = "AddButton", tint = Purple40,
                modifier = Modifier.size(50.dp))
        }
    }
}

@Composable
@Preview
fun PreviewAddButton() {
    //AddButton()
}
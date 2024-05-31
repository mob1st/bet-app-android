package br.com.mob1st.bet.core.ui.ds.molecule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.mob1st.bet.core.ui.ds.atoms.Purple40

@Composable
fun GroupIcon() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier.size(25.dp),
        border = BorderStroke(1.dp, Color.White),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(Purple40),
    ) {
    }
}

@Composable
@Preview
fun GroupIconPreview() {
    GroupIcon()
}

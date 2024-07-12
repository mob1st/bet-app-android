package br.com.mob1st.core.design.atoms.colors.tonals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
private fun NeutralTonalPreview() {
    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        NeutralColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            neutralTonal = BlackTonal,
        )
        NeutralColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            neutralTonal = GreyTonal,
        )
        NeutralColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            neutralTonal = WhiteTonal,
        )
    }
}

@Composable
private fun NeutralColumn(
    modifier: Modifier,
    neutralTonal: NeutralTonal,
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .neutralColor(neutralTonal.x1),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .neutralColor(neutralTonal.x2),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .neutralColor(neutralTonal.x3),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .neutralColor(neutralTonal.x4),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .neutralColor(neutralTonal.x5),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .neutralColor(neutralTonal.x6),
        )
    }
}

context(ColumnScope)
private fun Modifier.neutralColor(color: Color) = weight(1f)
    .fillMaxWidth()
    .background(color)

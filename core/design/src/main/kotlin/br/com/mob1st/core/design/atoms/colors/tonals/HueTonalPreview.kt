package br.com.mob1st.core.design.atoms.colors.tonals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
private fun HueTonalPreview() {
    Row {
        TonalColumn(
            modifier = Modifier.weight(1f),
            tonal = UranianBlueTonal,
        )
        TonalColumn(
            modifier = Modifier.weight(1f),
            tonal = JasperTonal,
        )
        TonalColumn(
            modifier = Modifier.weight(1f),
            tonal = AmaranthPinkTonal,
        )
        TonalColumn(
            modifier = Modifier.weight(1f),
            tonal = VanillaTonal,
        )
        TonalColumn(
            modifier = Modifier.weight(1f),
            tonal = MauveTonal,
        )
        TonalColumn(
            modifier = Modifier.weight(1f),
            tonal = NyanzaTonal,
        )
    }
}

@Composable
@Suppress("LongMethod")
private fun TonalColumn(
    modifier: Modifier,
    tonal: HueTonal,
) {
    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x0),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x10),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x20),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x30),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x40),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x50),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x60),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x70),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x80),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x90),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x95),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x99),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(tonal.x100),
        )
    }
}

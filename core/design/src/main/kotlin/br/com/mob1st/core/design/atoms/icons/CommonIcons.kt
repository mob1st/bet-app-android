package br.com.mob1st.core.design.atoms.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.R
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun BackIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        Icons.AutoMirrored.Filled.ArrowBack,
        modifier = modifier,
        contentDescription = stringResource(id = R.string.core_design_back_button_content_description),
    )
}

@Composable
fun NextIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        Icons.AutoMirrored.Filled.ArrowForward,
        modifier = modifier,
        contentDescription = stringResource(id = R.string.core_design_back_button_content_description),
    )
}

@Composable
fun CheckIcon(
    modifier: Modifier = Modifier,
    contentDescription: String,
) {
    Icon(
        Icons.Default.Check,
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Composable
@ThemedPreview
private fun IconsPreview() {
    BetTheme {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(40.dp),
        ) {
            item {
                GridItem {
                    BackIcon()
                }
            }
            item {
                GridItem {
                    NextIcon()
                }
            }
            item {
                GridItem {
                    CheckIcon(
                        contentDescription = "Finish",
                    )
                }
            }
        }
    }
}

@Composable
private fun GridItem(
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(8.dp),
    ) {
        content()
    }
}

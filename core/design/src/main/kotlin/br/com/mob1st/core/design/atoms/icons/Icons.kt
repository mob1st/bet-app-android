package br.com.mob1st.core.design.atoms.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.R
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun BackIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        painterResource(id = R.drawable.ic_arrow_back),
        modifier = modifier,
        contentDescription = stringResource(id = R.string.core_design_back_button_content_description),
    )
}

@Composable
fun CheckIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_check),
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Composable
fun SetupBudgetIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_finances_setup_budget),
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Composable
fun FixedExpensesIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_finances_fixed_expenses),
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Composable
fun FixedIncomeIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_finances_incomes),
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Composable
fun VariableExpensesIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_finances_variable_expenses),
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Composable
fun SeasonalExpensesIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_finances_seasonal_expenses),
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Composable
fun Delete(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_delete),
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_calendar_day),
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Composable
fun Undo(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_undo),
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Composable
@ThemedPreview
private fun IconsPreview() {
    val iconSize = 24.dp
    PreviewTheme {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(40.dp),
        ) {
            item {
                GridItem { BackIcon(modifier = Modifier.size(iconSize)) }
            }
            item {
                GridItem { CheckIcon(modifier = Modifier.size(iconSize)) }
            }
            item {
                GridItem { FixedExpensesIcon(modifier = Modifier.size(iconSize)) }
            }
            item {
                GridItem { FixedIncomeIcon(modifier = Modifier.size(iconSize)) }
            }
            item {
                GridItem { VariableExpensesIcon(modifier = Modifier.size(iconSize)) }
            }
            item {
                GridItem { SetupBudgetIcon(modifier = Modifier.size(iconSize)) }
            }
            item {
                GridItem { Undo(modifier = Modifier.size(iconSize)) }
            }
            item {
                GridItem { Delete(modifier = Modifier.size(iconSize)) }
            }
            item {
                GridItem { Calendar(modifier = Modifier.size(iconSize)) }
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
            .size(40.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

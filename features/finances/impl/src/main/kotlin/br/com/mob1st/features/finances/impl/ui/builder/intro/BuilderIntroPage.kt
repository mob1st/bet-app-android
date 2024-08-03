package br.com.mob1st.features.finances.impl.ui.builder.intro

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.core.androidx.compose.NavigationSideEffect
import br.com.mob1st.core.androidx.compose.SnackbarSideEffect
import br.com.mob1st.core.design.atoms.colors.material.LocalExtensionsColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.families.ColorCombination
import br.com.mob1st.core.design.atoms.icons.FixedExpensesIcon
import br.com.mob1st.core.design.atoms.icons.FixedIncomeIcon
import br.com.mob1st.core.design.atoms.icons.SeasonalExpensesIcon
import br.com.mob1st.core.design.atoms.icons.VariableExpensesIcon
import br.com.mob1st.core.design.molecules.icons.IconScale
import br.com.mob1st.core.design.molecules.icons.LeadingIcon
import br.com.mob1st.core.design.molecules.loading.CrossfadeLoading
import br.com.mob1st.core.design.templates.FeatureSummaryScaffold
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.events.builderIntroScreenViewEvent
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute
import br.com.mob1st.features.utils.observability.TrackEventSideEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun BuilderIntroPage(
    onNext: (BuilderRoute) -> Unit,
) {
    val viewModel = koinViewModel<BuilderIntroViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val consumables by viewModel.consumableUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    BuilderIntroScreen(
        snackbarHostState = remember {
            SnackbarHostState()
        },
        uiState = uiState,
        onClickNext = viewModel::start,
    )
    IntroSideEffects(
        consumables = consumables,
        snackbarHostState = snackbarHostState,
        onNavigate = {
            onNext(it)
            viewModel.consume(BuilderIntroConsumables.nullableRoute)
        },
        onDismissSnackbar = {
            viewModel.consume(BuilderIntroConsumables.nullableSnackbar)
        },
    )
}

@Composable
private fun BuilderIntroScreen(
    snackbarHostState: SnackbarHostState,
    uiState: BuilderIntroUiState,
    onClickNext: () -> Unit,
) {
    FeatureSummaryScaffold(
        snackbarHostState = snackbarHostState,
        onClickButton = onClickNext,
        titleContent = { Text(stringResource(id = R.string.finances_builder_intro_header)) },
        subtitleContent = { Text(stringResource(id = R.string.finances_builder_intro_subheader)) },
        buttonContent = { ButtonContent(isLoading = uiState.isLoading) },
    ) {
        IntroScaffoldContent()
    }
}

@Composable
private fun IntroScaffoldContent() {
    Column {
        BuilderSummaryItem(
            iconContent = {
                VariableExpensesIcon(contentDescription = null)
            },
            combination = LocalExtensionsColorFamilies.current.variableExpenses.containerCombination,
            title = R.string.finances_builder_intro_item1_headline,
            description = R.string.finances_builder_intro_item1_description,
        )
        BuilderSummaryItem(
            iconContent = {
                FixedExpensesIcon(contentDescription = null)
            },
            combination = LocalExtensionsColorFamilies.current.fixedExpenses.containerCombination,
            title = R.string.finances_builder_intro_item2_headline,
            description = R.string.finances_builder_intro_item2_description,
        )

        BuilderSummaryItem(
            iconContent = {
                SeasonalExpensesIcon(contentDescription = null)
            },
            combination = LocalExtensionsColorFamilies.current.seasonalExpenses.containerCombination,
            title = R.string.finances_builder_intro_item3_headline,
            description = R.string.finances_builder_intro_item3_description,
        )

        BuilderSummaryItem(
            iconContent = {
                FixedIncomeIcon(contentDescription = null)
            },
            combination = LocalExtensionsColorFamilies.current.incomes.containerCombination,
            title = R.string.finances_builder_intro_item4_headline,
            description = R.string.finances_builder_intro_item4_description,
        )
    }
}

@Composable
private fun BuilderSummaryItem(
    iconContent: @Composable () -> Unit,
    combination: ColorCombination,
    @StringRes title: Int,
    @StringRes description: Int,
) {
    ListItem(
        leadingContent = {
            LeadingIcon(
                combination = combination,
                scale = IconScale.Small,
            ) {
                iconContent()
            }
        },
        headlineContent = { Text(text = stringResource(id = title)) },
        supportingContent = { Text(text = stringResource(id = description)) },
    )
}

@Composable
private fun IntroSideEffects(
    consumables: BuilderIntroConsumables,
    snackbarHostState: SnackbarHostState,
    onNavigate: (BuilderRoute) -> Unit,
    onDismissSnackbar: () -> Unit,
) {
    TrackEventSideEffect(event = AnalyticsEvent.builderIntroScreenViewEvent())
    NavigationSideEffect(
        target = consumables.route,
        onNavigate = onNavigate,
    )
    SnackbarSideEffect(
        snackbarHostState = snackbarHostState,
        snackbarVisuals = consumables.snackbar?.resolve(),
        onDismiss = onDismissSnackbar,
        onPerformAction = {},
    )
}

@Composable
private fun RowScope.ButtonContent(isLoading: Boolean) {
    CrossfadeLoading(
        modifier = Modifier.align(Alignment.CenterVertically),
        isLoading = isLoading,
        crossfadeLabel = "loadingButton",
    ) {
        Text(text = stringResource(id = R.string.finances_builder_intro_button))
    }
}

@Composable
@ThemedPreview
private fun BuilderIntroScreenPreview() {
    PreviewTheme {
        BuilderIntroScreen(
            snackbarHostState = SnackbarHostState(),
            uiState = BuilderIntroUiState(),
            onClickNext = {},
        )
    }
}

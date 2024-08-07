package br.com.mob1st.core.design.organisms.header

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import br.com.mob1st.core.design.organisms.buttons.TopBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PrimaryTitle(
    scrollBehavior: TopAppBarScrollBehavior,
    titleContent: @Composable () -> Unit,
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            titleContent()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SecondaryTitle(
    scrollBehavior: TopAppBarScrollBehavior,
    titleContent: @Composable () -> Unit,
    onBackClicked: (() -> Unit),
) {
    MediumTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            TopBackButton(onBackClicked = onBackClicked)
        },
        title = {
            titleContent()
        },
    )
}

object TitleDefaults {
    @OptIn(ExperimentalMaterial3Api::class)
    val scrollBehavior: TopAppBarScrollBehavior
        @Composable get() = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState(),
        )
}

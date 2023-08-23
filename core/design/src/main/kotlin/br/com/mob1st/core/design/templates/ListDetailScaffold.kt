package br.com.mob1st.core.design.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.mob1st.core.design.atoms.theme.LocalDisplayFeatures
import br.com.mob1st.core.design.atoms.theme.LocalWindowWidthSizeClass
import br.com.mob1st.core.design.atoms.theme.PreviewSetup
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
fun ListDetailScaffold(
    useSingleWhenMediumWindow: Boolean = true,
    list: @Composable (Pane) -> Unit,
    detail: @Composable (Pane) -> Unit,
) {
    when (LocalWindowWidthSizeClass.current) {
        WindowWidthSizeClass.Compact -> {
            CompactLayout(
                useSingleWhenMediumWindow = useSingleWhenMediumWindow,
                list = list
            )
        }
        WindowWidthSizeClass.Medium -> {
            MediumLayout(
                useSingleWhenMediumWindow = useSingleWhenMediumWindow,
                list = list,
                detail = detail
            )
        }
        WindowWidthSizeClass.Expanded -> {
            ExpandedLayout(
                list = list,
                detail = detail
            )
        }
    }
}

@Composable
private fun CompactLayout(useSingleWhenMediumWindow: Boolean, list: @Composable (Pane) -> Unit) {
    List(
        useSingleWhenMediumWindow = useSingleWhenMediumWindow,
        windowWidthSizeClass = WindowWidthSizeClass.Compact,
        content = list
    )
}

@Composable
private fun MediumLayout(
    useSingleWhenMediumWindow: Boolean,
    list: @Composable (Pane) -> Unit,
    detail: @Composable (Pane) -> Unit,
) {
    if (useSingleWhenMediumWindow) {
        List(
            useSingleWhenMediumWindow = true,
            windowWidthSizeClass = WindowWidthSizeClass.Medium,
            content = list
        )
    } else {
        ListDetail(
            useSingleWhenMediumWindow = false,
            windowWidthSizeClass = WindowWidthSizeClass.Medium,
            list = list,
            detail = detail
        )
    }
}

@Composable
private fun ExpandedLayout(list: @Composable (Pane) -> Unit, detail: @Composable (Pane) -> Unit) {
    ListDetail(
        useSingleWhenMediumWindow = false,
        windowWidthSizeClass = WindowWidthSizeClass.Expanded,
        list = list,
        detail = detail
    )
}

@Composable
private fun ListDetail(
    useSingleWhenMediumWindow: Boolean,
    windowWidthSizeClass: WindowWidthSizeClass,
    list: @Composable (Pane) -> Unit,
    detail: @Composable (Pane) -> Unit,
) {
    val splitFraction = remember(windowWidthSizeClass) {
        windowWidthSizeClass.splitListDetail()
    }
    TwoPane(
        modifier = Modifier.fillMaxSize(),
        first = {
            List(
                useSingleWhenMediumWindow = useSingleWhenMediumWindow,
                windowWidthSizeClass = windowWidthSizeClass,
                content = list
            )
        },
        second = {
            Detail(
                windowWidthSizeClass = windowWidthSizeClass,
                content = detail
            )
        },
        strategy = HorizontalTwoPaneStrategy(
            splitFraction = splitFraction,
            gapWidth = ListDetailConstants.Spacing.large
        ),
        displayFeatures = LocalDisplayFeatures.current
    )
}

@Composable
private fun List(
    useSingleWhenMediumWindow: Boolean,
    windowWidthSizeClass: WindowWidthSizeClass,
    content: @Composable (Pane) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val pane = remember(useSingleWhenMediumWindow, windowWidthSizeClass, maxWidth) {
            ListPaneFactory.create(
                width = maxWidth,
                windowWidthSizeClass = windowWidthSizeClass,
                shouldBeSingleWhenMediumWindow = useSingleWhenMediumWindow
            )
        }
        content(pane)
    }
}

@Composable
private fun Detail(windowWidthSizeClass: WindowWidthSizeClass, content: @Composable (Pane) -> Unit) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val pane = remember(windowWidthSizeClass, maxWidth) {
            DetailPaneFactory.create(
                width = maxWidth,
                windowWidthSizeClass = windowWidthSizeClass
            )
        }
        content(pane)
    }
}

@Preview(widthDp = 400)
@Composable
private fun ListDetailPanesCompactPreview() {
    PreviewSetup(windowSizeClass = WindowWidthSizeClass.Compact) {
        ListDetailScaffoldPreview(true)
    }
}

@Preview(widthDp = 800)
@Composable
private fun ListDetailMediumAndSinglePreview() {
    PreviewSetup(windowSizeClass = WindowWidthSizeClass.Medium) {
        ListDetailScaffoldPreview(true)
    }
}

@Preview(widthDp = 800)
@Composable
private fun ListDetailMediumAndMultiplePreview() {
    PreviewSetup(windowSizeClass = WindowWidthSizeClass.Medium) {
        ListDetailScaffoldPreview(false)
    }
}

@Preview(widthDp = 1400)
@Composable
private fun ListDetailExpandedPreview() {
    PreviewSetup(windowSizeClass = WindowWidthSizeClass.Expanded) {
        ListDetailScaffoldPreview(true)
    }
}

@Composable
private fun ListDetailScaffoldPreview(useSingleWhenMediumWindow: Boolean) {
    ListDetailScaffold(
        useSingleWhenMediumWindow = useSingleWhenMediumWindow,
        list = {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    MaterialTheme.colorScheme.primaryContainer
                ),
                contentAlignment = Alignment.Center
            ) {
                Text("LIST")
            }
        },
        detail = {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    MaterialTheme.colorScheme.tertiaryContainer
                ),
                contentAlignment = Alignment.Center
            ) {
                Text("DETAIL")
            }
        }
    )
}

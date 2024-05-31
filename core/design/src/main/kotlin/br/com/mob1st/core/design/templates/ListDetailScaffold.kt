@file:Suppress("TooManyFunctions")

package br.com.mob1st.core.design.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.mob1st.core.design.atoms.theme.LocalDisplayFeatures
import br.com.mob1st.core.design.atoms.theme.LocalLayoutSpec
import br.com.mob1st.core.design.atoms.theme.PreviewSetup
import com.google.accompanist.adaptive.TwoPane

@Composable
fun ListDetailScaffold(
    useSingleWhenMediumWindow: Boolean = true,
    list: @Composable (Pane) -> Unit,
    detail: @Composable (Pane) -> Unit,
) {
    val layoutSpec = LocalLayoutSpec.current
    val layout =
        remember(layoutSpec, useSingleWhenMediumWindow) {
            ListDetailLayout(layoutSpec, useSingleWhenMediumWindow)
        }
    when (layoutSpec) {
        LayoutSpec.Compact -> {
            CompactLayout(
                layout = layout,
                list = list,
            )
        }
        LayoutSpec.Medium -> {
            MediumLayout(
                layout = layout,
                list = list,
                detail = detail,
            )
        }
        LayoutSpec.Expanded -> {
            ExpandedLayout(
                layout = layout,
                list = list,
                detail = detail,
            )
        }
    }
}

@Composable
private fun CompactLayout(
    layout: ListDetailLayout,
    list: @Composable (Pane) -> Unit,
) {
    List(
        layout = layout,
        content = list,
    )
}

@Composable
private fun MediumLayout(
    layout: ListDetailLayout,
    list: @Composable (Pane) -> Unit,
    detail: @Composable (Pane) -> Unit,
) {
    if (layout.useSingleWhenMedium) {
        List(
            layout,
            content = list,
        )
    } else {
        ListDetail(
            layout = layout,
            list = list,
            detail = detail,
        )
    }
}

@Composable
private fun ExpandedLayout(
    layout: ListDetailLayout,
    list: @Composable (Pane) -> Unit,
    detail: @Composable (Pane) -> Unit,
) {
    ListDetail(
        layout = layout,
        list = list,
        detail = detail,
    )
}

@Composable
private fun ListDetail(
    layout: ListDetailLayout,
    list: @Composable (Pane) -> Unit,
    detail: @Composable (Pane) -> Unit,
) {
    val strategy =
        remember(layout) {
            layout.strategy()
        }
    Box(modifier = Modifier.padding(horizontal = layout.layoutSpec.horizontalSpacing)) {
        TwoPane(
            modifier = Modifier.fillMaxSize(),
            first = {
                List(
                    layout = layout,
                    content = list,
                )
            },
            second = {
                Detail(
                    layout = layout,
                    content = detail,
                )
            },
            strategy = strategy,
            displayFeatures = LocalDisplayFeatures.current,
        )
    }
}

@Composable
private fun List(
    layout: ListDetailLayout,
    content: @Composable (Pane) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val pane =
            remember(layout, maxWidth) {
                layout.list(width = maxWidth)
            }
        content(pane)
    }
}

@Composable
private fun Detail(
    layout: ListDetailLayout,
    content: @Composable (Pane) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val pane =
            remember(layout, maxWidth) {
                layout.detail(width = maxWidth)
            }
        content(pane)
    }
}

@Preview(widthDp = 400, showBackground = true)
@Composable
private fun ListDetailPanesCompactPreview() {
    PreviewSetup(layoutSpec = LayoutSpec.Compact) {
        ListDetailScaffoldPreview(true)
    }
}

@Preview(widthDp = 800, showBackground = true)
@Composable
private fun ListDetailMediumAndSinglePreview() {
    PreviewSetup(layoutSpec = LayoutSpec.Medium) {
        ListDetailScaffoldPreview(true)
    }
}

@Preview(widthDp = 800, showBackground = true)
@Composable
private fun ListDetailMediumAndMultiplePreview() {
    PreviewSetup(layoutSpec = LayoutSpec.Medium) {
        ListDetailScaffoldPreview(false)
    }
}

@Preview(widthDp = 1400, showBackground = true)
@Composable
private fun ListDetailExpandedPreview() {
    PreviewSetup(layoutSpec = LayoutSpec.Expanded) {
        ListDetailScaffoldPreview(true)
    }
}

@Composable
private fun ListDetailScaffoldPreview(useSingleWhenMediumWindow: Boolean) {
    ListDetailScaffold(
        useSingleWhenMediumWindow = useSingleWhenMediumWindow,
        list = {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text("LIST")
            }
        },
        detail = {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text("DETAIL")
            }
        },
    )
}

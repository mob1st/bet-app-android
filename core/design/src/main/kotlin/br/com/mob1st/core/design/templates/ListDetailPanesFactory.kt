package br.com.mob1st.core.design.templates

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val SMALLEST_PANE_COLUMNS = 4
private const val LARGEST_PANE_COLUMNS = 8

/**
 * A factory for creating [Pane]s for the list side of a ListDetail layout.
 */
internal object ListPaneFactory {
    fun create(shouldBeSingleWhenMediumWindow: Boolean, windowWidthSizeClass: WindowWidthSizeClass, width: Dp): Pane {
        val isCompact = windowWidthSizeClass == WindowWidthSizeClass.Compact
        val isMedium = windowWidthSizeClass == WindowWidthSizeClass.Medium
        val isSingle = isCompact || (shouldBeSingleWhenMediumWindow && isMedium)
        val columns = when {
            isSingle && isMedium -> LARGEST_PANE_COLUMNS
            else -> SMALLEST_PANE_COLUMNS
        }
        val spacing = if (isCompact) {
            ListDetailConstants.Spacing.small
        } else {
            ListDetailConstants.Spacing.large
        }
        return Pane(
            isSingle = isSingle,
            isFirst = true,
            columns = columns,
            spacing = spacing,
            maxWidth = width
        )
    }
}

/**
 * A factory for creating [Pane]s for the detail side of a ListDetail layout.
 */
internal object DetailPaneFactory {
    fun create(windowWidthSizeClass: WindowWidthSizeClass, width: Dp): Pane {
        require(windowWidthSizeClass >= WindowWidthSizeClass.Medium) {
            "Window must be medium or expanded. Current is $windowWidthSizeClass."
        }
        return Pane(
            isSingle = false,
            isFirst = false,
            spacing = ListDetailConstants.Spacing.large,
            maxWidth = width,
            columns = if (windowWidthSizeClass == WindowWidthSizeClass.Expanded) {
                LARGEST_PANE_COLUMNS
            } else {
                SMALLEST_PANE_COLUMNS
            }
        )
    }
}

/**
 * Calculates the split for the panes in a ListDetail layout.
 */
internal fun WindowWidthSizeClass.splitListDetail(): Float {
    require(this != WindowWidthSizeClass.Compact) {
        "ListDetail strategy is not allowed for single pane layouts."
    }
    return if (this == WindowWidthSizeClass.Expanded) {
        ListDetailConstants.Split.expanded
    } else {
        ListDetailConstants.Split.medium
    }
}

/**
 * Constants used by the ListDetail layout.
 * @see [https://m3.material.io/foundations/layout/applying-layout/compact#4b2b6814-c64a-4bc0-a07d-6652a91737e6]
 */
internal object ListDetailConstants {

    /**
     * The spacing constants to the used by panes in ListDetail layouts.
     * These values will be used as horizontal margins, spacer between panes and gutter between columns.
     */
    object Spacing {
        /**
         * Used in windows in [WindowWidthSizeClass.Expanded] mode or [WindowWidthSizeClass.Medium] mode.
         */
        val large = 24.dp

        /**
         * The horizontal padding to apply to the pane and the gutter between columns in [WindowWidthSizeClass.Compact]
         * windows.
         */
        val small = 16.dp
    }

    /**
     * The split constants to the used by panes in ListDetail layouts.
     */
    object Split {

        /**
         * The split to use when the window is in [WindowWidthSizeClass.Expanded] mode.
         */
        const val expanded = 0.3f

        /**
         * The split to use when the window is in [WindowWidthSizeClass.Medium] mode.
         */
        const val medium = 0.5f
    }
}

package br.com.mob1st.core.design.atoms.properties.dimensions

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * Represents a dimension value that can be converted to DP.
 */
@Immutable
sealed interface Dimension {
    @JvmInline
    value class Fixed(private val value: Dp) : Dimension {
        override fun Modifier.width() = width(value)

        override fun Modifier.height() = height(value)

        override fun Modifier.size() = size(value)
    }

    data class WrapContent(
        private val horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        private val verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        private val alignment: Alignment = Alignment.Center,
        private val unbounded: Boolean = false,
    ) : Dimension {
        override fun Modifier.width() = wrapContentWidth(horizontalAlignment, unbounded)

        override fun Modifier.height() = wrapContentHeight(verticalAlignment, unbounded)

        override fun Modifier.size() = wrapContentSize(alignment, unbounded)
    }

    @JvmInline
    value class FillMax(
        private val fraction: Float = 1f,
    ) : Dimension {
        override fun Modifier.width() = fillMaxWidth(fraction)

        override fun Modifier.height() = fillMaxHeight(fraction)

        override fun Modifier.size() = fillMaxSize(fraction)
    }

    fun Modifier.width(): Modifier

    fun Modifier.height(): Modifier

    fun Modifier.size(): Modifier
}

fun Modifier.width(dimension: Dimension) = dimension.run { width() }

fun Modifier.height(dimension: Dimension) = dimension.run { height() }

fun Modifier.size(dimension: Dimension) = dimension.run { size() }

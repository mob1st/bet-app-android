package br.com.mob1st.core.design.atoms.spacing

import androidx.annotation.DimenRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Represents a dimension value that can be converted to DP.
 */
sealed class Dimension {

    data class Resource(@DimenRes val id: Int) : Dimension()

    data class Fixed(val value: Dp) : Dimension()
    data class WrapContent(
        val horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
        val verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        val alignment: Alignment = Alignment.Center,
        val unbounded: Boolean = false,
    ) : Dimension()
    data class FillMax(
        val fraction: Float = 1f,
    ) : Dimension()
}

val Dp.dimension get() = Dimension.Fixed(this)
val Int.dimension get() = Dimension.Fixed(dp)

fun Modifier.width(dimension: Dimension) = when (dimension) {
    is Dimension.Resource -> composed {
        width(dimensionResource(id = dimension.id))
    }
    is Dimension.Fixed -> width(dimension.value)
    is Dimension.WrapContent -> wrapContentWidth(dimension.horizontalAlignment, dimension.unbounded)
    is Dimension.FillMax -> fillMaxWidth(dimension.fraction)
}

fun Modifier.height(dimension: Dimension) = when (dimension) {
    is Dimension.Resource -> composed {
        height(dimensionResource(id = dimension.id))
    }
    is Dimension.Fixed -> height(dimension.value)
    is Dimension.WrapContent -> wrapContentHeight(dimension.verticalAlignment, dimension.unbounded)
    is Dimension.FillMax -> fillMaxHeight(dimension.fraction)
}

fun Modifier.size(dimension: Dimension) = when (dimension) {
    is Dimension.Resource -> composed {
        size(dimensionResource(id = dimension.id))
    }
    is Dimension.Fixed -> size(dimension.value)
    is Dimension.WrapContent -> wrapContentSize(dimension.alignment, dimension.unbounded)
    is Dimension.FillMax -> fillMaxSize(dimension.fraction)
}

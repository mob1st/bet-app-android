package br.com.mob1st.core.design.atoms.shapes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.spacing.Spacings

internal object ShapesFactory {
    fun create(): Shapes {
        return Shapes(
            extraSmall = RoundedCornerShape(size = 16.dp),
            small = RoundedCornerShape(percent = 100),
            medium = RoundedCornerShape(percent = 40),
            large = RoundedCornerShape(percent = 100),
            extraLarge = RoundedCornerShape(size = 32.dp),
        )
    }
}

@Composable
@Preview
private fun ShapesPreview() {
    val shapes = ShapesFactory.create()
    MaterialTheme(shapes = shapes) {
        Surface(
            modifier = Modifier
                .padding(Spacings.x4)
                .background(Color.Red),
        ) {
            Box(
                modifier = Modifier
                    .padding(Spacings.x4)
                    .background(Color.Black)
                    .clip(shapes.extraLarge),
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary,
                        )
                        .fillMaxSize()
                        .padding(Spacings.x4)
                        .clip(shapes.extraLarge),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                            )
                            .padding(Spacings.x4)
                            .clip(MaterialTheme.shapes.medium),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                                .padding(Spacings.x4)
                                .clip(MaterialTheme.shapes.extraSmall),
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ShapeIsolatedPreview() {
    val shapes = ShapesFactory.create()
    MaterialTheme(
        shapes = shapes,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = Spacings.x8, vertical = Spacings.x16),
            verticalArrangement = Arrangement.spacedBy(Spacings.x8),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.extraSmall),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Box(
                    modifier = Modifier.padding(Spacings.x4),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Extra Small")
                }
            }
            Surface(
                modifier = Modifier
                    .width(128.dp)
                    .height(56.dp)
                    .clip(shapes.small),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Box(
                    modifier = Modifier.padding(Spacings.x4),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Small")
                }
            }
            Surface(
                modifier = Modifier
                    .width(164.dp)
                    .height(96.dp)
                    .clip(shapes.medium),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Medium")
                }
            }

            Surface(
                modifier = Modifier
                    .size(96.dp)
                    .clip(shapes.large),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Large")
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
                    .clip(shapes.extraLarge),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Extra Large")
                }
            }
        }
    }
}

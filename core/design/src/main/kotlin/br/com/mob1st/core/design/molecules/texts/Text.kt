package br.com.mob1st.core.design.molecules.texts

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import br.com.mob1st.core.design.atoms.properties.texts.TextState

@Composable
fun Display(
    modifier: Modifier = Modifier,
    text: TextState,
    textSize: TextSize = TextSize.Medium,
) {
    BaseText(
        text = text,
        textSize = textSize,
        textStyleProvider = BodyStyleProvider,
        modifier = modifier,
    )
}

@Composable
fun Headline(
    modifier: Modifier = Modifier,
    text: TextState,
    textSize: TextSize = TextSize.Medium,
) {
    BaseText(
        text = text,
        textSize = textSize,
        textStyleProvider = HeadlineStyleProvider,
        modifier = modifier,
    )
}

@Composable
fun Title(
    modifier: Modifier = Modifier,
    text: TextState,
    textSize: TextSize = TextSize.Medium,
) {
    BaseText(
        text = text,
        textSize = textSize,
        textStyleProvider = TitleStyleProvider,
        modifier = modifier,
    )
}

@Composable
fun Body(
    modifier: Modifier = Modifier,
    text: TextState,
    textSize: TextSize = TextSize.Medium,
) {
    BaseText(
        text = text,
        textSize = textSize,
        textStyleProvider = BodyStyleProvider,
        modifier = modifier,
    )
}

@Composable
fun Label(
    modifier: Modifier = Modifier,
    text: TextState,
    textSize: TextSize = TextSize.Medium,
) {
    BaseText(
        text = text,
        textSize = textSize,
        textStyleProvider = LabelStyleProvider,
        modifier = modifier,
    )
}

@Composable
internal fun BaseText(
    text: TextState,
    textSize: TextSize,
    textStyleProvider: TextStyleProvider,
    modifier: Modifier,
) {
    val typography = MaterialTheme.typography

    val style = remember(textSize) {
        textStyleProvider.of(typography, textSize)
    }
    Text(
        modifier = modifier,
        text = text.resolve(),
        style = style,
    )
}

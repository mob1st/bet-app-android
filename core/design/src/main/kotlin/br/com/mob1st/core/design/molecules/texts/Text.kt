package br.com.mob1st.core.design.molecules.texts

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import br.com.mob1st.core.design.atoms.properties.Text
import br.com.mob1st.core.design.atoms.properties.rememberAnnotedString

@Composable
fun Display(text: Text, textSize: TextSize = TextSize.Medium, modifier: Modifier = Modifier) {
    BaseText(
        text = text,
        textSize = textSize,
        textStyleProvider = BodyStyleProvider,
        modifier = modifier
    )
}

@Composable
fun Headline(text: Text, textSize: TextSize = TextSize.Medium, modifier: Modifier = Modifier) {
    BaseText(
        text = text,
        textSize = textSize,
        textStyleProvider = HeadlineStyleProvider,
        modifier = modifier
    )
}

@Composable
fun Title(text: Text, textSize: TextSize = TextSize.Medium, modifier: Modifier = Modifier) {
    BaseText(
        text = text,
        textSize = textSize,
        textStyleProvider = TitleStyleProvider,
        modifier = modifier
    )
}

@Composable
fun Body(text: Text, textSize: TextSize = TextSize.Medium, modifier: Modifier = Modifier) {
    BaseText(
        text = text,
        textSize = textSize,
        textStyleProvider = BodyStyleProvider,
        modifier = modifier
    )
}

@Composable
fun Label(text: Text, textSize: TextSize = TextSize.Medium, modifier: Modifier = Modifier) {
    BaseText(
        text = text,
        textSize = textSize,
        textStyleProvider = LabelStyleProvider,
        modifier = modifier
    )
}

@Composable
internal fun BaseText(text: Text, textSize: TextSize, textStyleProvider: TextStyleProvider, modifier: Modifier) {
    val typography = MaterialTheme.typography
    val annotatedString = rememberAnnotedString(text)
    val style = remember(textSize) {
        textStyleProvider.of(typography, textSize)
    }
    Text(
        text = annotatedString,
        style = style
    )
}

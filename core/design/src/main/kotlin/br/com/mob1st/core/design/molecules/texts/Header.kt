package br.com.mob1st.core.design.molecules.texts

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.Text

@Immutable
interface Header {
    val text: Text
    val description: Text?
}

fun Header(
    text: Text,
    description: Text? = null,
): Header =
    HeaderImpl(
        text = text,
        description = description,
    )

private data class HeaderImpl(
    override val text: Text,
    override val description: Text?,
) : Header

package br.com.mob1st.core.design.atoms.typography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

/**
 * Wraps the font roles to be used in Material3 texts.
 * @see [https://m3.material.io/styles/typography/applying-type]
 */
interface Material3Roles {
    /**
     * @see [https://m3.material.io/styles/typography/applying-type#fea95f28-348c-42ae-95e1-1c5bfd819524]
     */
    val display: FontRole

    /**
     * @see [https://m3.material.io/styles/typography/applying-type#43511b5a-fe60-4125-ac0c-571c4e6f0642]
     */
    val headline: FontRole

    /**
     * @see [https://m3.material.io/styles/typography/applying-type#e9e0cea3-10cb-405d-98a9-cf6a90758967]
     */
    val title: FontRole

    /**
     * @see [https://m3.material.io/styles/typography/applying-type#19205dc2-64ec-4954-a95c-6e6b214c707e]
     */
    val body: FontRole
    val label: FontRole
}

/**
 * Wraps a set of [TextStyle] objects for different font roles in Material3.
 * @see [https://m3.material.io/styles/typography/type-scale-tokens]
 */
@Immutable
data class FontRole(
    val small: TextStyle,
    val medium: TextStyle,
    val large: TextStyle,
)

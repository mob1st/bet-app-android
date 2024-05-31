package br.com.mob1st.core.design.molecules.buttons

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ButtonBar {
    @Immutable
    interface SingleButton : ButtonBar {
        val primary: ButtonState
    }

    @Immutable
    interface PrimarySecondary : ButtonBar {
        val primary: ButtonState
        val secondary: ButtonState
    }

    companion object {
        fun SingleButton(primary: ButtonState): SingleButton =
            ButtonBarSingleButtonImpl(
                primary = primary,
            )

        fun ButtonBar.PrimarySecondary(
            primary: ButtonState,
            secondary: ButtonState,
        ): PrimarySecondary =
            ButtonBarPrimarySecondaryImpl(
                primary = primary,
                secondary = secondary,
            )
    }
}

private data class ButtonBarSingleButtonImpl(
    override val primary: ButtonState,
) : ButtonBar.SingleButton

private data class ButtonBarPrimarySecondaryImpl(
    override val primary: ButtonState,
    override val secondary: ButtonState,
) : ButtonBar.PrimarySecondary

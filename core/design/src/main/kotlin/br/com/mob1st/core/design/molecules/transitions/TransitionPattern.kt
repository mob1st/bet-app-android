package br.com.mob1st.core.design.molecules.transitions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition

/**
 * A pattern of transitions to be applied during navigation between screens.
 * @see [https://m3.material.io/styles/motion/transitions/transition-patterns]
 */
sealed interface TransitionPattern {
    /**
     * The enter transition to be applied when navigating to a new screen.
     * @return [EnterTransition] to be applied on the enter transition.
     */
    fun enter(): EnterTransition

    /**
     * The exit transition to be applied when navigating to a new screen.
     * @return [ExitTransition] to be applied on the exit transition.
     */
    fun exit(): ExitTransition
}

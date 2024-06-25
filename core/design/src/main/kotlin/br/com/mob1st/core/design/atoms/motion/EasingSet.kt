package br.com.mob1st.core.design.atoms.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.runtime.Immutable

/**
 * A set of [CubicBezierEasing] to apply on transitions and general motions
 *
 * @see [https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration]
 */
@Immutable
internal sealed interface EasingSet {
    /**
     *  Generally used for transitions that begin and end on screen.
     */
    val default: CubicBezierEasing

    /**
     * Generally used for transitions that enters the screen.
     */
    val decelerate: CubicBezierEasing

    /**
     * Generally used for transitions of small/medium pieces components exiting the screen.
     */
    val accelerate: CubicBezierEasing
}

/**
 * [EasingSet] for to be used in the most prominent transitions on UI.
 * Usually applied in motions that movement large pieces of screen, such as navigations
 */
internal data object EmphasizedEasingSet : EasingSet {
    /**
     * Generally used for large pieces of UI.
     */
    override val default = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    /**
     * Generally used for transitions of small/medium pieces of UI.
     */
    override val decelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)

    /**
     * Generally used for transitions of small/medium pieces of UI.
     */
    override val accelerate = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
}

/**
 * Transitions applied for small pieces of UI or non prominent motion
 */
internal data object StandardEasingSet : EasingSet {
    /**
     * Almost linear transition
     */
    override val default = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    /**
     * For standard entering transition
     */
    override val decelerate = CubicBezierEasing(0f, 0f, 0f, 1f)

    /**
     * For standard exiting transition
     */
    override val accelerate = CubicBezierEasing(0.3f, 0f, 1f, 1f)
}

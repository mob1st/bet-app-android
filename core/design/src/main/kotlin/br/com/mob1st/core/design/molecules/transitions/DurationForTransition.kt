package br.com.mob1st.core.design.molecules.transitions

import br.com.mob1st.core.design.atoms.motion.DurationSet

/**
 * Set of durations for transitions.
 * @property actual The actual duration for the transition.
 */
class DurationForTransition private constructor(
    val actual: Int,
) {
    /**
     * The outgoing duration for the transition.
     */
    val outgoing = (actual * PROGRESS_THRESHOLD).toInt()

    /**
     * The incoming duration for the transition.
     */
    val incoming = actual - outgoing

    companion object {
        private const val PROGRESS_THRESHOLD = 0.35f

        /**
         * Setup for the duration of the entering transitions.
         */
        val Enter = DurationForTransition(DurationSet.medium.x4)

        /**
         * Setup for the duration of the exiting transitions.
         */
        val Exit = DurationForTransition(DurationSet.short.x4)
    }
}

package br.com.mob1st.core.design.atoms.motion

/**
 * Duration tokens to apply on transitions.
 *
 * Check the material 3 documentation to understand how to combine Easing and durations.
 * @see <a href="https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration">Easing and Duration</a>
 */
internal object Duration {
    /*
    Short: 50ms - 200ms
     */
    const val SHORT_4: Int = 200

    /*
    Medium: 250ms - 400ms
     */
    const val MEDIUM_3: Int = 300
    const val MEDIUM_4: Int = 400

    /*
    Long: 450ms - 600ms
     */
    const val LONG_2: Int = 500
}

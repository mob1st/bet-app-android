package br.com.mob1st.core.design.atoms.motion

/**
 * Duration tokens to apply on transitions.
 *
 * Check the material 3 documentation to understand how to combine Easing and durations.
 * @see <a href="https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration">Easing and Duration</a>
 */
internal sealed interface Duration {
    val one: Int
    val two: Int
    val three: Int
    val four: Int

    companion object {
        val short: Duration = ShortDuration
        val medium: Duration = MediumDuration
        val long: Duration = LongDuration
    }
}

private data object ShortDuration : Duration {
    override val one: Int = 50
    override val two: Int = 100
    override val three: Int = 150
    override val four: Int = 200
}

private data object MediumDuration : Duration {
    override val one: Int = 250
    override val two: Int = 300
    override val three: Int = 350
    override val four: Int = 400
}

private data object LongDuration : Duration {
    override val one: Int = 450
    override val two: Int = 500
    override val three: Int = 550
    override val four: Int = 600
}

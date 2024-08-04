package br.com.mob1st.core.design.atoms.motion

/**
 * Duration tokens to apply on transitions.
 *
 * Check the material 3 documentation to understand how to combine Easing and durations.
 * @see [https://m3.material.io/styles/motion/easing-and-duration/applying-easing-and-duration]
 */
internal sealed interface DurationSet {
    val x1: Int
    val x2: Int
    val x3: Int
    val x4: Int

    companion object {
        val short: DurationSet = ShortDurationSet
        val medium: DurationSet = MediumDurationSet
        val long: DurationSet = LongDurationSet
        val extraLong: DurationSet = ExtraLongDurationSet
    }
}

private data object ShortDurationSet : DurationSet {
    override val x1: Int = 50
    override val x2: Int = 100
    override val x3: Int = 150
    override val x4: Int = 200
}

private data object MediumDurationSet : DurationSet {
    override val x1: Int = 250
    override val x2: Int = 300
    override val x3: Int = 350
    override val x4: Int = 400
}

private data object LongDurationSet : DurationSet {
    override val x1: Int = 450
    override val x2: Int = 500
    override val x3: Int = 550
    override val x4: Int = 600
}

private data object ExtraLongDurationSet : DurationSet {
    override val x1: Int = 700
    override val x2: Int = 800
    override val x3: Int = 900
    override val x4: Int = 1000
}

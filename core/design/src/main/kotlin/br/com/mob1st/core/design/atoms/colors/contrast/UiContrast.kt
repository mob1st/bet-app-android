package br.com.mob1st.core.design.atoms.colors.contrast

/**
 * Possible options of UI contrast selection available in the device settings.
 * This enum is necessary because the current contrast API is available only for XML UI in the material library.
 * @param threshold The threshold that determine the contrast level of the enum.
 */
internal enum class UiContrast(val threshold: Float) {
    STANDARD(0f),
    MEDIUM(1 / 3f),
    HIGH(2 / 3f),
    ;

    companion object {
        /**
         * Uses the given [contrast] value to determine which [UiContrast] enum it represents.
         * The logic to determine which float contrast value represents each enum was taken from ColorContrast class
         * @param contrast The contrast value to be converted to an enum.
         * @return The [UiContrast] enum that represents the given contrast value.
         * @see [https://developer.android.com/reference/com/google/android/material/color/ColorContrast]
         */
        fun from(contrast: Float): UiContrast {
            return when {
                contrast < MEDIUM.threshold -> STANDARD
                contrast < HIGH.threshold -> MEDIUM
                else -> HIGH
            }
        }
    }
}

package br.com.mob1st.core.design.molecules.transitions

import android.os.Parcelable
import kotlinx.serialization.Serializable

/**
 * A navigation entry point that can be triggered to create a composable screen.
 * It defines the entering navigation pattern to be applied when this route enters the screen.
 * The [Parcelable] implementation is not necessary for the direct instances of this class, but it's important because
 * it forces the properties to implement it, which is a requirement for the type safe navigation.
 * @see route
 */
interface NavRoute {
    /**
     * The entering navigation pattern to be applied when this route enters the screen.
     */
    val enteringPatternKey: PatternKey?
}

/**
 * The keys that can be used to define the navigation pattern to be applied when a route enters the screen.
 */
@Serializable
enum class PatternKey {
    BackAndForward,
    TopLevel,
    BottomSheet,
}

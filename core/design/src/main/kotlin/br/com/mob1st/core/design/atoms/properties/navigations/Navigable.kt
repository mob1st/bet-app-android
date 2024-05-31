package br.com.mob1st.core.design.atoms.properties.navigations

import androidx.compose.runtime.Immutable

/**
 * Helper function for a class to provide a [NavTarget] as part of the state of the UI.
 *
 * This can be used to side-effect navigation events triggered by ViewModels to the UI layer.
 * With this interface is possible to match the same name convention in the project.
 */
@Immutable
interface Navigable<T : NavTarget> {
    /**
     * The navigation target.
     */
    val navTarget: T?
}

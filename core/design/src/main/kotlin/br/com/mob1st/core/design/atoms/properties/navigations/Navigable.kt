package br.com.mob1st.core.design.atoms.properties.navigations

import androidx.compose.runtime.Immutable

/**
 * This can be used to side-effect navigation events triggered by ViewModels to the UI layer.
 * With this interface is possible to match the same name convention in the project.
 */
@Immutable
interface Navigable<T> {
    /**
     * The navigation target.
     */
    val navTarget: T?
}

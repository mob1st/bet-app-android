package br.com.mob1st.core.kotlinx.structures

import kotlinx.collections.immutable.ImmutableList

/**
 * Represents a state that has a selected item and a list of options.
 * @param S The type of the selected item.
 * @param L The type of the options.
 */
interface SelectableState<S, L> {
    /**
     * The selected item.
     */
    val selected: S

    /**
     * The list of options.
     */
    val options: ImmutableList<L>
}

/**
 * Represents a state that has a selected list of items and a list of options.
 */
interface MultiSelectableState<S, L> {
    val selected: ImmutableList<S>
    val options: ImmutableList<L>
}

/**
 * Represents a state that has a selected index and a list of options.
 */
interface IndexSelectableState<L> : SelectableState<Int, L> {
    /**
     * The selected index.
     */
    override val selected: Int

    /**
     * The list of options.
     */
    override val options: ImmutableList<L>
}

/**
 * Represents a state that has a selected list of indexes and a list of options.
 * @param L The type of the options.
 * @property selected The list of selected indexes.
 */
interface MultiIndexSelectableState<L> : MultiSelectableState<Int, L> {
    override val selected: ImmutableList<Int>
    override val options: ImmutableList<L>
}

package br.com.mob1st.features.twocents.builder.impl.ui.builder

import android.os.Parcelable
import br.com.mob1st.core.kotlinx.collections.update
import br.com.mob1st.core.kotlinx.structures.Id
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

/**
 * The users input for the builder screen.
 * It's not part of the UI state, so no need to be immutable.
 * @param manuallyAdded The manually added categories.
 * @param suggested The suggested categories.
 */
@Parcelize
internal data class BuilderUserInput(
    val manuallyAdded: PersistentList<Entry> = persistentListOf(),
    val suggested: PersistentMap<Id, Entry> = persistentHashMapOf(),
) : Parcelable {
    /**
     * Category entry state. It can be a manually added category or a suggested category.
     * @param name The name of the category.
     * @param amount The value of the category.
     */
    @Parcelize
    internal data class Entry(
        val name: String = "",
        val amount: String = "",
    ) : Parcelable

    /**
     * Adds a new manually added category.
     * @param entry The manually added category to add.
     * @return A new [BuilderUserInput] with the new manually category added.
     */
    operator fun plus(entry: Entry): BuilderUserInput = copy(manuallyAdded = manuallyAdded.add(entry))

    /**
     * Updates a manually added category.
     * @param position The position of the category to update.
     * @param block The block to update the category.
     * @return A new [BuilderUserInput] with the category updated.
     */
    fun update(
        position: Int,
        block: (entry: Entry) -> Entry,
    ): BuilderUserInput = copy(manuallyAdded = manuallyAdded.update(position, block))

    fun update(
        suggestionId: Id,
        block: (entry: Entry) -> Entry,
    ): BuilderUserInput {
        val new = block(suggested[suggestionId] ?: Entry())
        return copy(
            suggested = suggested.put(suggestionId, new),
        )
    }
}

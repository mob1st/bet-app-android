package br.com.mob1st.core.design.organisms.lists

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

/**
 * Handy function for list items that are selectable.
 * It applies the [Role.Button] semantics to the item, improving accessibility for lists.
 * Avoid use the [clickable] modifier directly, use this function instead.
 * @param onSelect the action to be executed when the item is selected.
 * @return the modifier with the [Role.Button] semantics.
 */
fun Modifier.selectableItem(
    enabled: Boolean = true,
    onSelect: () -> Unit,
) = this.clickable(
    enabled = enabled,
    role = Role.Button,
    onClick = onSelect,
)

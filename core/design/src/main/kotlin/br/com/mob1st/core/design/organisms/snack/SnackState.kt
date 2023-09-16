package br.com.mob1st.core.design.organisms.snack

import br.com.mob1st.core.design.atoms.properties.icons.Icon
import br.com.mob1st.core.design.atoms.properties.texts.Text
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class SnackState(
    val supporting: Text,
    val action: Text? = null,
    val icon: Icon? = null,
)

data class SnackQueue(
    private val items: List<SnackState> = emptyList(),
) {
    val snack: SnackState? = items.firstOrNull()
    fun pop(): SnackQueue = copy(items = items.drop(1))
    fun push(item: SnackState): SnackQueue = copy(items = items + item)
}

fun MutableStateFlow<SnackQueue>.show(text: Text) = show(SnackState(text))
fun MutableStateFlow<SnackQueue>.show(item: SnackState) {
    update { it.push(item) }
}

fun MutableStateFlow<SnackQueue>.dismiss() {
    update { it.pop() }
}

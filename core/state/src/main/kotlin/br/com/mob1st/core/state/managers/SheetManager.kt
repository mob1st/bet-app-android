package br.com.mob1st.core.state.managers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

interface SheetManager<T> {
    val sheetOutput: StateFlow<T?>

    context(ViewModel)
    fun showSheet(sheetState: T)

    fun consumeSheet()
}

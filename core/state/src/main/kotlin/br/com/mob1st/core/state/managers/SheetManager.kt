package br.com.mob1st.core.state.managers

import androidx.lifecycle.ViewModel

interface SheetManager<T> {
    context(ViewModel)
    fun showSheet(sheetState: T)

    fun consumeSheet()
}

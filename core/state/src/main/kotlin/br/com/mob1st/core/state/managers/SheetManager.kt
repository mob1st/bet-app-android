package br.com.mob1st.core.state.managers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the sheets to be shown in the UI and allow the consumption of them.
 */
interface SheetManager<T> {
    /**
     * Handy method for ViewModels to directly show the sheets.
     */
    context(ViewModel)
    fun showSheet(sheet: T)

    /**
     * The output of the sheets to be shown in the UI.
     */
    val sheetOutput: StateFlow<T?>

    /**
     * Consumes the sheets, removing them from the UI.
     */
    fun consumeSheet()
}

class SheetDelegate<T> : SheetManager<T>, MutableStateFlow<T?> by MutableStateFlow(null) {
    override val sheetOutput: StateFlow<T?> = asStateFlow()

    context(ViewModel)
    override fun showSheet(sheet: T) {
        value = sheet
    }

    override fun consumeSheet() {
        value = null
    }
}

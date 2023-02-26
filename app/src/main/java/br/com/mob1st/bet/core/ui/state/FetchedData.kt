package br.com.mob1st.bet.core.ui.state

import androidx.compose.runtime.Immutable

/**
 * A data that will be fecthed asynchronously and can have a kind of empty state in cases there is
 * no data available for the user yet.
 */
@Immutable
interface FetchedData {

    /**
     * Returns true if there is data to be displayed in the UI
     */
    fun hasData(): Boolean
}

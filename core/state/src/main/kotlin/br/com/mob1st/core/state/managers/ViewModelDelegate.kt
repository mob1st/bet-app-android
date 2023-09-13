package br.com.mob1st.core.state.managers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

/**
 * A small piece of state management that can be used by ViewModels that can share similar state managements.
 * It's useful to avoid code duplication and to make the ViewModel more readable.
 */
fun interface ViewModelDelegate<State> {

    /**
     * The output of the state management. Give a [CoroutineScope] to the delegate so it can be used to launch
     * coroutines internally and expose the output as a [StateFlow].
     */
    fun output(scope: CoroutineScope): StateFlow<State>
}

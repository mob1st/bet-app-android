package br.com.mob1st.core.state.manager

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable

/**
 * A small piece of state management that can be used by ViewModels that can share similar state managements.
 *
 * It manages its own coroutine scope and will be cancelled when the ViewModel is cleared.
 * To use it in a ViewModel, extends this class and use the ViewModel.addCloseable method to add it to the ViewModel.
 * @see androidx.lifecycle.ViewModel.addCloseable
 */
abstract class StateManager : Closeable {

    /**
     * The dispatcher used to run the coroutines.
     */
    protected open val dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate

    /**
     * The job used to run the [CoroutineScope] of this class.
     */
    protected open val job: Job = SupervisorJob()

    /**
     * The [CoroutineScope] of this class.
     * It follows the same lifecycle than [androidx.lifecycle.viewModelScope] but is not tied to it.
     *
     * When the [androidx.lifecycle.ViewModel.onCleared] method is called, this scope is cancelled.
     */
    protected val managerScope by lazy {
        CoroutineScope(job + dispatcher)
    }

    override fun close() {
        managerScope.cancel()
    }
}

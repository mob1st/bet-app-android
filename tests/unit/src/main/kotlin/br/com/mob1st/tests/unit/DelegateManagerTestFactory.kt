package br.com.mob1st.tests.unit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

/**
 * Will create new [ViewModelStore], add view model into it using [ViewModelProvider]
 * and then call [ViewModelStore.clear], that will cause [ViewModel.onCleared] to be called
 */
fun <T : ViewModel> T.clear() {
    val viewModelStore = ViewModelStore()
    val viewModelProvider =
        ViewModelProvider(
            viewModelStore,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return this@clear as T
                }
            },
        )
    viewModelProvider[this@clear::class.java]
    viewModelStore.clear()
}

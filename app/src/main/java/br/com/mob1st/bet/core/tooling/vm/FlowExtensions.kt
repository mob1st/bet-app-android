package br.com.mob1st.bet.core.tooling.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.bet.core.tooling.flow.next
import br.com.mob1st.bet.core.tooling.flow.onCollect
import br.com.mob1st.bet.core.tooling.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

context(ViewModel)
fun <T> MutableSharedFlow<T>.next(
    value: T
): Job = next(viewModelScope, value)

context(ViewModel)
fun <T, U> MutableStateFlow<T>.update(
    source: Flow<U>,
    combine: (currentState: T, newData: U) -> T
): Job = update(viewModelScope, source, combine)

context(ViewModel)
fun <T> Flow<T>.onCollect(block: suspend (T) -> Unit): Job = onCollect(viewModelScope, block)

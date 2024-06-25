package br.com.mob1st.core.state.extensions

import arrow.optics.Copy
import arrow.optics.copy
import kotlinx.coroutines.flow.MutableSharedFlow

typealias CopyBlock<T> = (T) -> T
typealias ArrowCopyBlock<T> = Copy<T>.(me: T) -> Unit

suspend fun <T> MutableSharedFlow<CopyBlock<T>>.emitUpdate(block: CopyBlock<T>) {
    emit(block)
}

suspend fun <T> MutableSharedFlow<CopyBlock<T>>.emitArrowUpdate(block: ArrowCopyBlock<T>) {
    emitUpdate { me ->
        me.copy {
            block(me)
        }
    }
}

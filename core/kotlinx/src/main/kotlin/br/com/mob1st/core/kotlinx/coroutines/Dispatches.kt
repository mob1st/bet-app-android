package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper class for
 */
@JvmInline
value class IoCoroutineDispatcher(
    private val delegated: CoroutineDispatcher = Dispatchers.IO,
) : CoroutineContext by delegated

@JvmInline
value class MainCoroutineDispatcher(
    private val delegated: CoroutineDispatcher = Dispatchers.Main,
) : CoroutineContext by delegated

@JvmInline
value class DefaultCoroutineDispatcher(
    private val delegated: CoroutineDispatcher = Dispatchers.Default,
) : CoroutineContext by delegated

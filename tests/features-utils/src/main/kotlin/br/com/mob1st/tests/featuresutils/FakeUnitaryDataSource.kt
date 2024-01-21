package br.com.mob1st.tests.featuresutils

import br.com.mob1st.core.data.UnitaryDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull

/**
 * Fake implementation of [UnitaryDataSource]
 */
class FakeUnitaryDataSource<T>(
    val setState: MutableStateFlow<T?>,
) : UnitaryDataSource<T> {

    override val data: Flow<T> = setState.mapNotNull { it }
    override suspend fun set(data: T) {
        setState.value = data
    }
}

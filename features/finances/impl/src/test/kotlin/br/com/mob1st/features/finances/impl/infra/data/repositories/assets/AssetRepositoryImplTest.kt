package br.com.mob1st.features.finances.impl.infra.data.repositories.assets

import br.com.mob1st.core.androidx.assets.AssetsGetter
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Asset
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class AssetRepositoryImplTest {
    private lateinit var repository: AssetRepositoryImpl
    private lateinit var assetsGetter: AssetsGetter

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        assetsGetter = mockk()
        val io = IoCoroutineDispatcher(
            UnconfinedTestDispatcher(),
        )
        repository = AssetRepositoryImpl(assetsGetter, io)
    }

    @Test
    fun `GIVEN a nullable file WHEN get by tag THEN assert error is thrown`() = runTest {
        coEvery { assetsGetter.get(any()) } returns null
        assertThrows<IllegalStateException> {
            repository.getByTag("any").first()
        }
    }

    @Test
    fun `GIVEN a file WHEN get by tag THEN assert asset is returned`() = runTest {
        coEvery { assetsGetter.get(any()) } returns "file"
        val actual = repository.getByTag("any").first()
        assertEquals(
            listOf(Asset(Uri("file"))),
            actual,
        )
    }
}

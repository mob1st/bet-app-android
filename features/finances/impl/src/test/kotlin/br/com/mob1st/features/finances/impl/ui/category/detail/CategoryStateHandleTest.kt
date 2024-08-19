package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.ui.fixtures.categoryEntry
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class CategoryStateHandleTest {
    @Test
    fun `GIVEN an existing value WHEN get a different entry THEN assert existing value is returned`() {
        val existingEntry = Arb.categoryEntry().next()
        val category = Arb.category().next()
        val stateHandle = SavedStateHandle(
            mapOf("entry_key" to existingEntry),
        )
        val sut = CategoryStateHandle(stateHandle)
        val flow = sut.entry(category)
        assertEquals(
            existingEntry,
            flow.value,
        )
    }

    @Test
    fun `GIVEN an existing value WHEN update a different entry THEN assert new value is returned`() {
        val existingEntry = Arb.categoryEntry().next()
        val newEntry = Arb.categoryEntry().next()
        val category = Arb.category().next()
        val stateHandle = SavedStateHandle(
            mapOf("entry_key" to existingEntry),
        )
        val sut = CategoryStateHandle(stateHandle)
        val flow = sut.entry(category)
        sut.update(newEntry)
        assertEquals(
            newEntry,
            flow.value,
        )
    }

    @Test
    fun `GIVEN no existing value WHEN get entry THEN assert default value is returned`() {
        val category = Arb.category().next()
        val stateHandle = SavedStateHandle()
        val sut = CategoryStateHandle(stateHandle)
        val flow = sut.entry(category)
        assertEquals(
            CategoryEntry(category),
            flow.value,
        )
    }

    @Test
    fun `GIVEN no existing value WHEN update entry THEN assert new value is returned`() {
        val newEntry = Arb.categoryEntry().next()
        val category = Arb.category().next()
        val stateHandle = SavedStateHandle()
        val sut = CategoryStateHandle(stateHandle)
        val flow = sut.entry(category)
        sut.update(newEntry)
        assertEquals(
            newEntry,
            flow.value,
        )
    }
}

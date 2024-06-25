package br.com.mob1st.core.kotlinx.structures

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class UuidTest {
    @Test
    fun `GIVEN two IDs WHEN compare the greater THEN result should be true`() {
        assertTrue(Uuid("60") > Uuid("50"))
    }

    @Test
    fun `GIVEN two IDs WHEN compare the lower THEN result should be true`() {
        assertTrue(Uuid("a") < Uuid("b"))
    }

    @Test
    fun `GIVEN two identical ID WHEN compare THEN should return zero`() {
        assertEquals(Uuid("a"), Uuid("a"))
    }

    @Test
    fun `GIVEN a ID with default constructor WHEN compare THEN should be different`() {
        val id1 = Uuid()
        val id2 = Uuid()
        assertNotEquals(id1, id2)
    }
}

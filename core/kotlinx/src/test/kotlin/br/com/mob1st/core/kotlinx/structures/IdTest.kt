package br.com.mob1st.core.kotlinx.structures

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class IdTest {
    @Test
    fun `GIVEN two IDs WHEN compare the greater THEN result should be true`() {
        assertTrue(Id("60") > Id("50"))
    }

    @Test
    fun `GIVEN two IDs WHEN compare the lower THEN result should be true`() {
        assertTrue(Id("a") < Id("b"))
    }

    @Test
    fun `GIVEN two identical ID WHEN compare THEN should return zero`() {
        assertEquals(Id("a"), Id("a"))
    }

    @Test
    fun `GIVEN a ID with default constructor WHEN compare THEN should be different`() {
        val id1 = Id()
        val id2 = Id()
        assertNotEquals(id1, id2)
    }
}

package br.com.mob1st.core.kotlinx.structures

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class EitherTest {

    private sealed class Subject : Either<Subject.Left, Subject.Right> {
        data class Left(val value: Int) : Subject()
        data class Right(val value: String) : Subject()
    }

    @Test
    fun `GIVEN a Left WHEN leftOrNull THEN expect the value`() {
        val subject = Subject.Left(1)
        assertEquals(subject, subject.leftOrNull())
    }

    @Test
    fun `GIVEN a Right WHEN leftOrNull THEN expect null`() {
        val subject = Subject.Right("1")
        assertEquals(null, subject.leftOrNull())
    }

    @Test
    fun `GIVEN a Right WHEN rightOrNull THEN expect the value`() {
        val subject = Subject.Right("1")
        assertEquals(subject, subject.rightOrNull())
    }

    @Test
    fun `GIVEN a Left WHEN rightOrNull THEN expect null`() {
        val subject = Subject.Left(1)
        assertEquals(null, subject.rightOrNull())
    }

    @Test
    fun `GIVEN a Left WHEN left THEN expect the value`() {
        val subject = Subject.Left(1)
        assertEquals(subject, subject.left())
    }

    @Test
    fun `GIVEN a Right WHEN left THEN expect exception`() {
        val subject = Subject.Right("1")
        assertThrows<IllegalStateException> {
            subject.left()
        }
    }

    @Test
    fun `GIVEN a Right WHEN right THEN expect the value`() {
        val subject = Subject.Right("1")
        assertEquals(subject, subject.right())
    }

    @Test
    fun `GIVEN a Left WHEN right THEN expect exception`() {
        val subject = Subject.Left(1)
        assertThrows<IllegalStateException> {
            subject.right()
        }
    }

    @Test
    fun `GIVEN a Left WHEN fold THEN expect the value`() {
        val subject = Subject.Left(1)
        assertEquals(subject, subject.fold({ it }, { it }))
    }

    @Test
    fun `GIVEN a Right WHEN fold THEN expect the value`() {
        val subject = Subject.Right("1")
        assertEquals(subject, subject.fold({ it }, { it }))
    }

    @Test
    fun `GIVEN a left WHEN on do onLeft THEN expect the value`() {
        val subject = Subject.Left(1)
        subject
            .onLeft {
                assertEquals(subject, it)
            }
            .onRight {
                throw IllegalStateException("Should not be called")
            }
    }

    @Test
    fun `GIVEN a right WHEN on do onRight THEN expect the value`() {
        val subject = Subject.Right("1")
        subject
            .onRight {
                assertEquals(subject, it)
            }
            .onLeft {
                throw IllegalStateException("Should not be called")
            }
    }
}

package br.com.mob1st.tests.featuresutils

import br.com.mob1st.core.kotlinx.structures.generateId
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Mocks the [generateId] function to enable custom id generation in tests
 */
class RandomIdGenerator : BeforeAllCallback, AfterAllCallback {
    override fun beforeAll(context: ExtensionContext?) {
        mockkStatic(::generateId)
    }

    override fun afterAll(context: ExtensionContext?) {
        unmockkStatic(::generateId)
    }
}

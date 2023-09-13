package br.com.mob1st.tests.featuresutils

import br.com.mob1st.core.androidx.flows.WhileSubscribedOrRetained
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Extension utilities for ViewModels tests.
 * It will setup the main dispatcher and mock the [WhileSubscribedOrRetained] command (which depends on Android).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTestExtension :
    BeforeEachCallback,
    AfterEachCallback,
    BeforeAllCallback,
    AfterAllCallback {

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    override fun beforeAll(context: ExtensionContext?) {
        mockkObject(WhileSubscribedOrRetained)
        every { WhileSubscribedOrRetained.command(any()) } returns flowOf(SharingCommand.START)
    }

    override fun afterAll(context: ExtensionContext?) {
        unmockkObject(WhileSubscribedOrRetained)
    }

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}

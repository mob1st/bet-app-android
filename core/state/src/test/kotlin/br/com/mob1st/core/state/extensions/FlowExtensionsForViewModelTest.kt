package br.com.mob1st.core.state.extensions

import androidx.lifecycle.ViewModel
import br.com.mob1st.core.state.async.Async
import br.com.mob1st.core.state.async.launch
import br.com.mob1st.tests.unit.MainCoroutineListener
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher

class FlowExtensionsForViewModelTest : FunSpec({

    val dispatcher = StandardTestDispatcher()
    listener(MainCoroutineListener(dispatcher))

    test("WHEN emit a value THEN assert value is updated") {
        val viewModel = FakeViewModel()
        viewModel.emit()
        dispatcher.scheduler.runCurrent()
        viewModel.mutableStateFlow.first().data shouldBe "1"
    }
})

private class FakeViewModel : ViewModel() {
    private val async =
        Async<String> {
            flowOf("1")
        }

    private val fakeInput = MutableSharedFlow<Unit>()
    val mutableStateFlow = MutableStateFlow(FakeDate())

    init {
        mutableStateFlow.collectUpdate(async.success) { currentState, newData ->
            currentState.copy(data = currentState.data + newData)
        }

        fakeInput.onCollect {
            async.launch()
        }
    }

    fun emit() {
        fakeInput.launchEmit(Unit)
    }
}

private data class FakeDate(
    val data: String = "",
)

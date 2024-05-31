package br.com.mob1st.bet.core.tooling.vm

import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope

@ExperimentalKotest
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ActionTest : BehaviorSpec({
    val scope = TestScope(TestCoroutineScheduler())
    Given("a failure flow") {
        val exception = Exception()
        val action =
            scope.actionFromFlow<Any> {
                flow {
                    throw exception
                }
            }
        When("trigger") {
            scope.testScheduler.advanceUntilIdle()
            action.trigger()
            scope.testScheduler.advanceUntilIdle()
        }
        Then("failure should be exception") {
            action.failure.first() shouldBe exception
        }
        Then("loading should be false") {
            action.loading.first() shouldBe false
        }
        Then("success should be empty") {
            action.success.produceIn(scope).isEmpty shouldBe true
        }
    }

    Given("a indeterminate flow") {
        val action =
            scope.actionFromFlow<Any> {
                flow { }
            }
        When("trigger") {
            scope.testScheduler.advanceUntilIdle()
            action.trigger()
            scope.testScheduler.advanceUntilIdle()
        }

        Then("failure should be empty") {
            action.failure.produceIn(scope).isEmpty shouldBe true
        }

        Then("loading should be true") {
            action.loading.first() shouldBe true
        }

        Then("success shoud be true") {
            action.success.produceIn(scope).isEmpty shouldBe true
        }
    }

    Given("a success flow") {
        val expected = "value"
        val action =
            scope.actionFromFlow<Any> {
                flow {
                    emit(expected)
                }
            }

        When("trigger") {
            scope.testScheduler.advanceUntilIdle()
            action.trigger()
            scope.testScheduler.advanceUntilIdle()
        }

        Then("failure should be empty") {
            action.failure.produceIn(scope).isEmpty shouldBe true
        }

        Then("loading should be false") {
            action.loading.first() shouldBe false
        }

        Then("success should be value") {
            action.success.first() shouldBe expected
        }
    }
})

package br.com.mob1st.core.state.async

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle

@OptIn(ExperimentalCoroutinesApi::class)
class AsyncTest : BehaviorSpec({

    val scope = TestScope(TestCoroutineScheduler())

    Given("a initial state") {
        val async =
            AsyncImpl<Int, String>(scope) {
                flowOf(it.toString())
            }
        And("collect loading") {
            Then("loading is false") {
                async.loading.first() shouldBe false
            }
        }
        And("collect success") {
            Then("success is empty") {
                async.success.produceIn(scope).isEmpty shouldBe true
            }
        }

        And("collect failure") {
            Then("failure is empty") {
                async.failure.produceIn(scope).isEmpty shouldBe true
            }
        }
    }

    Given("a loading state") {
        val async =
            AsyncImpl<Unit, Unit>(scope) {
                flow { }
            }
        When("invoke") {
            scope.advanceUntilIdle()
            async.launch()
            scope.advanceUntilIdle()
            And("collect loading") {
                Then("loading is true") {
                    async.loading.first() shouldBe true
                }
            }
            And("collect success") {
                Then("success is empty") {
                    async.success.produceIn(scope).isEmpty shouldBe true
                }
            }
            And("collect failure") {
                Then("failure is empty") {
                    async.failure.produceIn(scope).isEmpty shouldBe true
                }
            }
        }
    }

    Given("a failure state") {
        val exception = Exception()
        val async =
            AsyncImpl<Unit, Unit>(scope) {
                flow {
                    throw exception
                }
            }

        When("Invoke") {
            scope.advanceUntilIdle()
            async.launch()
            scope.advanceUntilIdle()
            And("collect loading") {
                Then("loading is false") {
                    async.loading.first() shouldBe false
                }
            }
            And("collect success") {
                Then("success is empty") {
                    async.success.produceIn(scope).isEmpty shouldBe true
                }
            }
            And("collect failure") {
                Then("failure should be the cause") {
                    async.failure.first() shouldBe exception
                }
            }
        }
    }

    Given("a successful state") {
        val result = "1"
        val async =
            AsyncImpl<Int, String>(scope) {
                flowOf(it.toString())
            }

        When("Invoke") {
            scope.advanceUntilIdle()
            async.launch(1)
            scope.advanceUntilIdle()
            And("collect loading") {
                Then("loading is false") {
                    async.loading.first() shouldBe false
                }
            }
            And("collect success") {
                Then("success should be the result") {
                    async.success.first() shouldBe result
                }
            }
            And("collect failure") {
                Then("failure is empty") {
                    async.failure.produceIn(scope).isEmpty shouldBe true
                }
            }
        }
    }
})

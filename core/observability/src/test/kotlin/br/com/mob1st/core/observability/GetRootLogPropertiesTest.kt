package br.com.mob1st.core.observability

import br.com.mob1st.core.observability.crashes.getRootLogProperties
import br.com.mob1st.core.observability.debug.Debuggable
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class GetRootLogPropertiesTest : BehaviorSpec({

    Given("an debuggable exception") {
        And("it has a debuggable causes") {
            And("its causes has a same loggable key") {
                val cause = FakeDebuggableException("key1", "value1")
                val exception = FakeDebuggableException("key1", "value2", cause)
                When("get root log properties") {
                    val props = exception.getRootLogProperties()
                    Then("it should be the log properties of the exception") {
                        props shouldBe exception.logInfo
                    }
                }
            }
            And("its causes has different loggable keys") {
                val cause = FakeDebuggableException("key1", "value1")
                val exception = FakeDebuggableException("key2", "value2", cause)
                When("get root log properties") {
                    val props = exception.getRootLogProperties()
                    Then("it should be the log properties of the exception") {
                        props shouldBe cause.logInfo + exception.logInfo
                    }
                }
            }
        }
        And("it has no debuggable causes") {
            val exception = FakeDebuggableException("key1", "value1")
            When("get root log properties") {
                val props = exception.getRootLogProperties()
                Then("it should be the log properties of the exception") {
                    props shouldBe exception.logInfo
                }
            }
        }
    }
    Given("a debuggable direct cause") {
        val cause = FakeDebuggableException("key1", "value1")
        val exception = Exception(cause)
        When("get root log properties") {
            val props = exception.getRootLogProperties()
            Then("it should be the log properties of the cause") {
                props shouldBe cause.logInfo
            }
        }
    }
    Given("a debuggable cause in a chain of 3 causes") {
        val cause1 = FakeDebuggableException("key1", "value1")
        val cause2 = Exception(cause1)
        val cause3 = Exception(cause2)
        val exception = Exception(cause3)
        When("get root log properties") {
            val props = exception.getRootLogProperties()
            Then("it should be the log properties of the exception") {
                props shouldBe cause1.logInfo
            }
        }
    }
    Given("a debuggable cause in a chain with more than 3 causes") {
        val cause1 = FakeDebuggableException("key1", "value1")
        val cause2 = Exception(cause1)
        val cause3 = Exception(cause2)
        val cause4 = Exception(cause3)
        val exception = Exception(cause4)
        When("get root log properties") {
            val props = exception.getRootLogProperties()
            Then("it should be an empty map") {
                props shouldBe emptyMap()
            }
        }
    }
    Given("a non debuggable exception with no debuggable causes") {
        val cause = Exception()
        val exception = Exception(cause)
        When("get root log properties") {
            val props = exception.getRootLogProperties()
            Then("it should be an empty map") {
                props shouldBe emptyMap()
            }
        }
    }
})

private class FakeDebuggableException(
    key: String,
    value: String,
    cause: Throwable? = null,
) : Exception(cause), Debuggable {
    override val logInfo: Map<String, Any?> = mapOf(key to value)
}

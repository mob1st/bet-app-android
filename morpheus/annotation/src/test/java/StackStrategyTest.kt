
import br.com.mob1st.morpheus.annotation.strategy.StackStrategy
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class StackStrategyTest : BehaviorSpec({

    Given("a list with multiple items") {
        val list = listOf(1, 2)
        When("consume") {
            val newList = StackStrategy<Int>().consume(list)
            Then("the new list size should be 1") {
                newList.size shouldBe 1
            }
            Then("the new list head element should be 1") {
                newList.first() shouldBe 1
            }
        }
    }

    Given("a list with a single item") {
        val list = listOf(1)
        When("consume") {
            val newList = StackStrategy<Int>().consume(list)
            Then("the new list should be empty") {
                newList.isEmpty() shouldBe true
            }
        }
    }

    Given("an empty list") {
        val list = emptyList<String>()
        When("consume") {
            shouldThrow<IndexOutOfBoundsException> {
                StackStrategy<String>().consume(list)
            }
        }
    }
})

import br.com.mob1st.morpheus.annotation.strategy.QueueStrategy
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class QueueStrategyTest : BehaviorSpec({

    Given("a list with many items") {
        val list = listOf(1, 2)
        When("consume") {
            val newList = QueueStrategy<Int>().consume(list)
            Then("the new list size should be 1") {
                newList.size shouldBe 1
            }
            Then("the new list head element should be 2") {
                newList.first() shouldBe 2
            }
        }
    }

    Given("a list with a single item") {
        val list = listOf(1)
        When("consume") {
            val newList = QueueStrategy<Int>().consume(list)
            Then("the new list should be empty") {
                newList.isEmpty() shouldBe true
            }
        }
    }

    Given("an empty list") {
        val list = emptyList<String>()
        When("consume") {
            shouldThrow<IndexOutOfBoundsException> {
                QueueStrategy<String>().consume(list)
            }
        }
    }
})

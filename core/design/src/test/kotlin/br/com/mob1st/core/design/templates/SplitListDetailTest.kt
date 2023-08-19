package br.com.mob1st.core.design.templates

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class SplitListDetailTest : BehaviorSpec({
    Given("a compact window width class size") {
        val windowWidthSizeClass = WindowWidthSizeClass.Compact
        When("split list detail") {
            Then("should throw an exception") {
                shouldThrow<IllegalArgumentException> {
                    windowWidthSizeClass.splitListDetail()
                }
            }
        }
    }

    Given("a medium window width class size") {
        val windowWidthSizeClass = WindowWidthSizeClass.Medium
        When("split list detail") {
            val actual = windowWidthSizeClass.splitListDetail()
            Then("should return a medium split list detail") {
                actual shouldBe ListDetailConstants.Split.medium
            }
        }
    }

    Given("an expanded window width class size") {
        val windowWidthSizeClass = WindowWidthSizeClass.Expanded
        When("split list detail") {
            val actual = windowWidthSizeClass.splitListDetail()
            Then("actual should be the expanded split") {
                actual shouldBe ListDetailConstants.Split.expanded
            }
        }
    }
})

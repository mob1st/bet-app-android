package br.com.mob1st.core.design.templates

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DetailPaneFactoryTest : BehaviorSpec({
    Given("a detaile pane factory") {
        val detailPaneFactory = DetailPaneFactory
        And("a window width class size is compact") {
            When("create") {
                Then("should throw an exception") {
                    shouldThrow<IllegalArgumentException> {
                        detailPaneFactory.create(
                            windowWidthSizeClass = WindowWidthSizeClass.Compact,
                            width = 1200.dp
                        )
                    }
                }
            }
        }
        And("a window width class size is medium") {
            When("create") {
                val actual = detailPaneFactory.create(
                    windowWidthSizeClass = WindowWidthSizeClass.Medium,
                    width = 1200.dp
                )
                Then("actual should be the expected medium pane") {
                    actual shouldBe Expected.mediumPane(
                        maxWidth = 1200.dp
                    )
                }
            }
        }
        And("a window width class size is expanded") {
            When("create") {
                val actual = detailPaneFactory.create(
                    windowWidthSizeClass = WindowWidthSizeClass.Expanded,
                    width = 1200.dp
                )
                Then("actual should be the expected large pane") {
                    actual shouldBe Expected.expandedPane(
                        maxWidth = 1200.dp
                    )
                }
            }
        }
    }
}) {
    private object Expected {
        fun mediumPane(maxWidth: Dp): Pane = Pane(
            isSingle = false,
            isFirst = false,
            columns = 4,
            spacing = ListDetailConstants.Spacing.large,
            maxWidth = maxWidth
        )

        fun expandedPane(maxWidth: Dp): Pane = Pane(
            isSingle = false,
            isFirst = false,
            columns = 8,
            spacing = ListDetailConstants.Spacing.large,
            maxWidth = maxWidth
        )
    }
}

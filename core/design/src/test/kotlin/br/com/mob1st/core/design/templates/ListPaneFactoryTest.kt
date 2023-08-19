package br.com.mob1st.core.design.templates

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ListPaneFactoryTest : BehaviorSpec({
    Given("a List pane factory") {
        val factory = ListPaneFactory
        And("a window width class size is compact") {
            When("create") {
                val actual = factory.create(
                    shouldBeSingleWhenMediumWindow = true,
                    windowWidthSizeClass = WindowWidthSizeClass.Compact,
                    width = 300.dp
                )
                Then("actual should be the expected compact pane") {
                    actual shouldBe Expected.compactPane(
                        maxWidth = 300.dp
                    )
                }
            }
        }

        And("a window width class size is medium") {
            And("use a single pane") {
                val actual = factory.create(
                    shouldBeSingleWhenMediumWindow = true,
                    windowWidthSizeClass = WindowWidthSizeClass.Medium,
                    width = 500.dp
                )
                Then("subject should be the expected medium pane") {
                    actual shouldBe Expected.mediumPane(
                        maxWidth = 500.dp,
                        isSingle = true,
                        columns = 8
                    )
                }
            }
            And("use a double pane") {
                val actual = factory.create(
                    shouldBeSingleWhenMediumWindow = false,
                    windowWidthSizeClass = WindowWidthSizeClass.Medium,
                    width = 700.dp
                )
                Then("subject should be the expected medium pane and not single") {
                    actual shouldBe Expected.mediumPane(
                        maxWidth = 700.dp,
                        isSingle = false,
                        columns = 4
                    )
                }
            }
        }

        And("a class size is expanded") {
            When("create") {
                val subject = factory.create(
                    shouldBeSingleWhenMediumWindow = false,
                    windowWidthSizeClass = WindowWidthSizeClass.Expanded,
                    width = 1400.dp
                )
                Then("subject should be the expected expanded pane and is not single") {
                    subject shouldBe Expected.expandedPane(1400.dp)
                }
            }
        }
    }
}) {
    private object Expected {

        fun compactPane(
            maxWidth: Dp,
        ) = Pane(
            columns = 4,
            isSingle = true,
            isFirst = true,
            spacing = ListDetailConstants.Spacing.small,
            maxWidth = maxWidth
        )

        fun mediumPane(
            maxWidth: Dp,
            columns: Int,
            isSingle: Boolean,
        ) = Pane(
            columns = columns,
            isSingle = isSingle,
            isFirst = true,
            spacing = ListDetailConstants.Spacing.large,
            maxWidth = maxWidth
        )

        fun expandedPane(maxWidth: Dp) = Pane(
            columns = 4,
            isSingle = false,
            isFirst = true,
            spacing = ListDetailConstants.Spacing.large,
            maxWidth = maxWidth
        )
    }
}

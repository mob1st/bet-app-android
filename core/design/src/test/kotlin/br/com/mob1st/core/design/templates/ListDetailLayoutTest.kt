package br.com.mob1st.core.design.templates

import androidx.compose.ui.unit.dp
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.boolean
import io.kotest.property.exhaustive.map

class ListDetailLayoutTest : BehaviorSpec({

    Given("a compact window") {
        val paneWidth = Arb.int(360, 599).map { it.dp }
        val layout = Exhaustive.boolean().map {
            ListDetailLayout(
                layoutSpec = LayoutSpec.Compact,
                useSingleWhenMedium = it
            )
        }

        When("create a list pane") {
            Then("pane should be the expected") {
                checkAll(layout, paneWidth) { layout, width ->
                    layout.list(width) shouldBe Pane(
                        maxWidth = width,
                        layoutSpec = LayoutSpec.Compact,
                        columnsLimit = ColumnsLimit.Four,
                        isSingle = true
                    )
                }
            }
        }

        When("create a detail pane") {
            Then("should throw an exception") {
                checkAll(layout, paneWidth) { layout, width ->
                    shouldThrow<IllegalArgumentException> {
                        layout.detail(width)
                    }
                }
            }
        }
    }

    Given("a medium window") {
        val paneWidth = Arb.int(600, 859).map { it.dp }
        When("create a list pane") {
            And("use single") {
                Then("pane should be the expected") {
                    checkAll(paneWidth) {
                        ListDetailLayout(
                            layoutSpec = LayoutSpec.Medium,
                            useSingleWhenMedium = true
                        ).list(it) shouldBe Pane(
                            maxWidth = it,
                            layoutSpec = LayoutSpec.Medium,
                            columnsLimit = ColumnsLimit.Eight,
                            isSingle = true
                        )
                    }
                }
            }
            And("not use single") {
                Then("pane should be the expected") {
                    checkAll(paneWidth) {
                        ListDetailLayout(
                            layoutSpec = LayoutSpec.Medium,
                            useSingleWhenMedium = false
                        ).list(it) shouldBe Pane(
                            maxWidth = it,
                            layoutSpec = LayoutSpec.Medium,
                            columnsLimit = ColumnsLimit.Four,
                            isSingle = false
                        )
                    }
                }
            }
        }

        When("create a detail pane") {
            Then("pane should be the expected") {
            }
        }
    }

    Given("a expanded window") {
        val paneWidth = Arb.int(860, 1920).map { it.dp }
        When("create a list pane") {
            And("use single") {
                Then("pane should be the expected") {
                }
            }
            And("not use single") {
                Then("pane should be the expected") {
                }
            }
        }

        When("create a detail pane") {
            Then("pane should be the expected") {
            }
        }
    }
})

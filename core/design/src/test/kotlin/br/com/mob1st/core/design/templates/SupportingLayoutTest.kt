package br.com.mob1st.core.design.templates

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class SupportingLayoutTest : BehaviorSpec({

    Given("a compact window") {
        When("create a primary pane") {
            Then("pane should be the expected") {
                ScreenWidth.compact.checkAll { width ->
                    val actual = SupportingLayout(LayoutSpec.Compact).primary(width)
                    actual shouldBe Pane(
                        layoutSpec = LayoutSpec.Compact,
                        columnsLimit = ColumnsLimit.Four,
                        maxWidth = width
                    )
                }
            }
        }

        When("create a secondary pane") {
            Then("pane should be the expected") {
                ScreenWidth.compact.checkAll { width ->
                    val actual = SupportingLayout(LayoutSpec.Compact).secondary(width)
                    actual shouldBe Pane(
                        layoutSpec = LayoutSpec.Compact,
                        columnsLimit = ColumnsLimit.Four,
                        maxWidth = width
                    )
                }
            }
        }
    }

    Given("a medium window") {
        When("create a primary pane") {
            Then("pane should be the expected") {
                ScreenWidth.medium.checkAll { width ->
                    val actual = SupportingLayout(LayoutSpec.Medium).primary(width)
                    actual shouldBe Pane(
                        layoutSpec = LayoutSpec.Medium,
                        columnsLimit = ColumnsLimit.Eight,
                        maxWidth = width
                    )
                }
            }
        }

        When("create a secondary pane") {
            Then("pane should be the expected") {
                ScreenWidth.medium.checkAll { width ->
                    val actual = SupportingLayout(LayoutSpec.Medium).secondary(width)
                    actual shouldBe Pane(
                        layoutSpec = LayoutSpec.Medium,
                        columnsLimit = ColumnsLimit.Eight,
                        maxWidth = width
                    )
                }
            }
        }
    }

    Given("a expanded window") {
        When("create a primary pane") {
            Then("pane should be the expected") {
                ScreenWidth.expanded.checkAll { width ->
                    val actual = SupportingLayout(LayoutSpec.Expanded).primary(width)
                    actual shouldBe Pane(
                        layoutSpec = LayoutSpec.Expanded,
                        columnsLimit = ColumnsLimit.Eight,
                        maxWidth = width
                    )
                }
            }
        }

        When("create a secondary pane") {
            Then("pane should be the expected") {
                ScreenWidth.expanded.checkAll { width ->
                    val actual = SupportingLayout(LayoutSpec.Expanded).secondary(width)
                    actual shouldBe Pane(
                        layoutSpec = LayoutSpec.Expanded,
                        columnsLimit = ColumnsLimit.Four,
                        maxWidth = width
                    )
                }
            }
        }
    }
})

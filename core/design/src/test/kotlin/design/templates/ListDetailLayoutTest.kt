package design.templates

import br.com.mob1st.core.design.templates.ColumnsLimit
import br.com.mob1st.core.design.templates.LayoutSpec
import br.com.mob1st.core.design.templates.ListDetailLayout
import br.com.mob1st.core.design.templates.Pane
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.boolean
import io.kotest.property.exhaustive.map

class ListDetailLayoutTest : BehaviorSpec({

    Given("a compact window") {
        val layout =
            Exhaustive.boolean().map {
                ListDetailLayout(
                    layoutSpec = LayoutSpec.Compact,
                    useSingleWhenMedium = it,
                )
            }

        When("create a list pane") {
            Then("pane should be the expected") {
                checkAll(layout, ScreenWidth.compact) { layout, width ->
                    layout.list(width) shouldBe
                        Pane(
                            maxWidth = width,
                            layoutSpec = LayoutSpec.Compact,
                            columnsLimit = ColumnsLimit.Four,
                        )
                }
            }
        }

        When("create a detail pane") {
            Then("should throw an exception") {
                checkAll(layout, ScreenWidth.compact) { layout, width ->
                    shouldThrow<IllegalArgumentException> {
                        layout.detail(width)
                    }
                }
            }
        }
    }

    Given("a medium window") {
        When("create a list pane") {
            And("use single") {
                Then("pane should be the expected") {
                    checkAll(ScreenWidth.medium) {
                        ListDetailLayout(
                            layoutSpec = LayoutSpec.Medium,
                            useSingleWhenMedium = true,
                        ).list(it) shouldBe
                            Pane(
                                maxWidth = it,
                                layoutSpec = LayoutSpec.Medium,
                                columnsLimit = ColumnsLimit.Eight,
                            )
                    }
                }
            }
            And("not use single") {
                Then("pane should be the expected") {
                    checkAll(ScreenWidth.medium) {
                        ListDetailLayout(
                            layoutSpec = LayoutSpec.Medium,
                            useSingleWhenMedium = false,
                        ).list(it) shouldBe
                            Pane(
                                maxWidth = it,
                                layoutSpec = LayoutSpec.Medium,
                                columnsLimit = ColumnsLimit.Four,
                            )
                    }
                }
            }
        }

        When("create a detail pane") {
            And("use single") {
                Then("should throw an exception") {
                    checkAll(ScreenWidth.medium) { width ->
                        shouldThrow<IllegalArgumentException> {
                            ListDetailLayout(
                                layoutSpec = LayoutSpec.Medium,
                                useSingleWhenMedium = true,
                            ).detail(width)
                        }
                    }
                }
            }

            And("not use single") {
                Then("pane should be the expected") {
                    checkAll(ScreenWidth.medium) { width ->
                        ListDetailLayout(
                            layoutSpec = LayoutSpec.Medium,
                            useSingleWhenMedium = false,
                        ).detail(width) shouldBe
                            Pane(
                                maxWidth = width,
                                layoutSpec = LayoutSpec.Medium,
                                columnsLimit = ColumnsLimit.Four,
                            )
                    }
                }
            }
        }
    }

    Given("a expanded window") {
        When("create a list pane") {
            Then("pane should be the expected") {
                checkAll(ScreenWidth.expanded, Exhaustive.boolean()) { width, useSingle ->
                    ListDetailLayout(
                        layoutSpec = LayoutSpec.Expanded,
                        useSingleWhenMedium = useSingle,
                    ).list(width) shouldBe
                        Pane(
                            maxWidth = width,
                            layoutSpec = LayoutSpec.Expanded,
                            columnsLimit = ColumnsLimit.Four,
                        )
                }
            }
        }

        When("create a detail pane") {
            Then("pane should be the expected") {
                checkAll(ScreenWidth.expanded) { width ->
                    ListDetailLayout(
                        layoutSpec = LayoutSpec.Expanded,
                        useSingleWhenMedium = true,
                    ).detail(width) shouldBe
                        Pane(
                            maxWidth = width,
                            layoutSpec = LayoutSpec.Expanded,
                            columnsLimit = ColumnsLimit.Eight,
                        )
                }
            }
        }
    }
})

package design.templates

import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.properties.dimensions.Dimension
import br.com.mob1st.core.design.templates.ColumnsLimit
import br.com.mob1st.core.design.templates.LayoutSpec
import br.com.mob1st.core.design.templates.Pane
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.merge
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.enum
import io.kotest.property.exhaustive.exhaustive
import io.kotest.property.exhaustive.ints

class PaneTest : BehaviorSpec({
    Given("a compact window") {
        val panes =
            ScreenWidth.compact.map { with ->
                Pane(
                    layoutSpec = LayoutSpec.Compact,
                    columnsLimit = ColumnsLimit.Four,
                    maxWidth = with,
                )
            }
        When("get horizontal margins") {
            val horizontalMargin = panes.map { it.horizontalMargins }
            Then("should be compact spacings") {
                horizontalMargin.checkAll { it shouldBe LayoutSpec.Compact.horizontalSpacing }
            }
        }

        When("get columns into the screen limit range") {
            val counts = Exhaustive.ints(1.rangeUntil(4))
            Then("should be the expected") {
                panes.checkAll { pane ->
                    counts.checkAll { count ->
                        val emptyArea = 32.dp + (16.dp * 3)
                        val columnWidth = (pane.maxWidth - emptyArea) / 4
                        val gutters = 16.dp * (count - 1)
                        pane.columns(count) shouldBe Dimension.Fixed(columnWidth * count + gutters)
                    }
                }
            }
        }
    }

    Given("a non compact window") {
        val panes =
            Arb.bind(
                listOf(LayoutSpec.Medium, LayoutSpec.Expanded).exhaustive(),
                listOf(ColumnsLimit.Four, ColumnsLimit.Eight, ColumnsLimit.Twelve).exhaustive(),
                ScreenWidth.medium.merge(ScreenWidth.expanded),
            ) { layoutSpec, columnsLimit, width ->
                Pane(
                    layoutSpec = layoutSpec,
                    columnsLimit = columnsLimit,
                    maxWidth = width,
                )
            }
        When("get horizontal paddings") {
            val margins = panes.map { it.horizontalMargins }
            Then("should be zero") {
                margins.checkAll { it shouldBe 0.dp }
            }
        }

        When("get columns into the screen limit range") {
            Then("should be the expected") {
                panes.checkAll { pane ->
                    val counts = Exhaustive.ints(1.rangeUntil(pane.columnsLimit.value))
                    counts.checkAll { count ->
                        val emptyArea = 24.dp * (pane.columnsLimit.value - 1)
                        val columnWidth = (pane.maxWidth - emptyArea) / pane.columnsLimit.value
                        val gutters = 24.dp * (count - 1)
                        pane.columns(count) shouldBe Dimension.Fixed(columnWidth * count + gutters)
                    }
                }
            }
        }
    }

    Given("any pane") {
        val panes =
            Arb.bind(
                Exhaustive.enum<LayoutSpec>(),
                listOf(ColumnsLimit.Four, ColumnsLimit.Eight, ColumnsLimit.Twelve).exhaustive(),
                ScreenWidth.compact.merge(ScreenWidth.medium).merge(ScreenWidth.expanded),
            ) { layoutSpec, columnsLimit, width ->
                Pane(
                    layoutSpec = layoutSpec,
                    columnsLimit = columnsLimit,
                    maxWidth = width,
                )
            }

        When("get columns lower than minimum range") {
            Then("should throw an exception") {
                checkAll(panes, Arb.int(max = 0)) { pane, count ->
                    shouldThrow<IllegalArgumentException> {
                        pane.columns(count)
                    }
                }
            }
        }

        When("get columns greater than the current range") {
            Then("should be full width") {
                panes.checkAll { pane ->
                    pane.columns(pane.columnsLimit.value) shouldBe Dimension.FillMax()
                }
            }
        }
    }
})

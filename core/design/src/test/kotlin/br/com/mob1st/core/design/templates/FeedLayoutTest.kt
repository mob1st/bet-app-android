package br.com.mob1st.core.design.templates

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.merge
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.enum

class FeedLayoutTest : ShouldSpec({

    should("use the given parameters") {
        checkAll(
            Exhaustive.enum<LayoutSpec>(),
            ScreenWidth.compact.merge(ScreenWidth.medium).merge(ScreenWidth.expanded)
        ) { layoutSpec, width ->
            val actual = FeedLayout(layoutSpec, width).pane()
            actual shouldBe Pane(
                layoutSpec = layoutSpec,
                columnsLimit = layoutSpec.columnsLimit,
                maxWidth = width
            )
        }
    }
})

package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroNextStepNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderCompleteNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNavArgs
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNextNavEvent
import br.com.mob1st.features.finances.impl.ui.category.navigation.CategoryCoordinator
import br.com.mob1st.tests.featuresutils.FakeNavigationApi
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.next
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BuilderCoordinatorTest {
    private lateinit var coordinator: BuilderCoordinator
    private lateinit var navigationApi: FakeNavigationApi
    private lateinit var categoryCoordinator: CategoryCoordinator

    @BeforeEach
    fun setUp() {
        navigationApi = FakeNavigationApi()
        categoryCoordinator = mockk(relaxed = true)
        coordinator = BuilderCoordinator(navigationApi, categoryCoordinator)
    }

    @Test
    fun `GIVEN a next step WHEN navigate from intro THEN assert it goes to next step route`() {
        val event = Arb.bind<BuilderIntroNextStepNavEvent>().next()
        coordinator.navigate(event)
        assertEquals(
            BuilderNavRoute.Step(event.step),
            navigationApi.routes.first(),
        )
    }

    @Test
    fun `GIVEN a next step WHEN navigate from step THEN assert it goes to the next step route`() {
        val args = Arb.enum<BuilderStepNavArgs>().next()
        coordinator.navigate(BuilderStepNextNavEvent(args))
        assertEquals(
            BuilderNavRoute.Step(args),
            navigationApi.routes.first(),
        )
    }

    @Test
    fun `GIVEN a complete builder action WHEN navigate from step THEN assert it goes to the completion route`() {
        coordinator.navigate(BuilderCompleteNavEvent)
        assertEquals(
            BuilderNavRoute.Completion(),
            navigationApi.routes.first(),
        )
    }
}

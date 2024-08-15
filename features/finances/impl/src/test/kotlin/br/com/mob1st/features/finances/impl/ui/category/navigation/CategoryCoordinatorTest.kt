package br.com.mob1st.features.finances.impl.ui.category.navigation

import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailArgs
import br.com.mob1st.tests.featuresutils.FakeNavigationApi
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoryCoordinatorTest {
    private lateinit var coordinator: CategoryCoordinator
    private lateinit var fakeNavigationApi: FakeNavigationApi

    @BeforeEach
    fun setUp() {
        fakeNavigationApi = FakeNavigationApi()
        coordinator = CategoryCoordinator(fakeNavigationApi)
    }

    @Test
    fun `GIVEN a intent WHEN navigate THEN assert it goes to category detail`() {
        val args = Arb.bind<CategoryDetailArgs>().next()
        coordinator.navigate(args)
        val expected = CategoryNavRoute.Detail(args = args)
        assertEquals(expected, fakeNavigationApi.routes.first())
    }
}

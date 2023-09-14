package br.com.mob1st.features.dev.impl.menu.domain

import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.tests.unit.randomEnum
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DevMenuTest {

    @Test
    fun `GIVEN a backend environment WHEN create THEN assert the expected entries`() {
        val backendEnvironment = BackendEnvironment.values().random()
        val devMenu = DevMenu(backendEnvironment)
        assertEquals(
            expected = listOf(
                DevMenuEntry.Environment(backendEnvironment),
                DevMenuEntry.FeatureFlags,
                DevMenuEntry.Gallery,
                DevMenuEntry.EntryPoint
            ),
            actual = devMenu.entries
        )
    }

    @Test
    fun `GIVEN a gallery position WHEN check if it's allowed THEN returns true`() {
        val devMenu = DevMenu(BackendEnvironment.values().random())
        val galleryIndex = devMenu.entries.indexOf(DevMenuEntry.Gallery)
        assertEquals(
            expected = true,
            actual = devMenu.isAllowed(galleryIndex)
        )
    }

    @Test
    fun `GIVEN a non gallery position WHEN check if it's allowed THEN returns false`() {
        val devMenu = DevMenu(currentEnv = randomEnum())
        val noGalleryEntries = devMenu.entries - DevMenuEntry.Gallery
        val nonGalleryIndex = List(noGalleryEntries.size) { index -> index }.random()
        assertEquals(
            expected = false,
            actual = devMenu.isAllowed(nonGalleryIndex)
        )
    }
}

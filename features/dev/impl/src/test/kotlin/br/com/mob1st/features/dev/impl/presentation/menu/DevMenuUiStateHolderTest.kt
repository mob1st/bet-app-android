package br.com.mob1st.features.dev.impl.presentation.menu

import br.com.mob1st.features.dev.impl.R
import br.com.mob1st.features.dev.impl.domain.DevMenu
import br.com.mob1st.features.dev.impl.domain.DevMenuEntry
import br.com.mob1st.features.dev.impl.presentation.gallery.GalleryNavTarget
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class DevMenuUiStateHolderTest {
    private lateinit var stateHolder: DevMenuUiStateHolder

    @BeforeEach
    fun setUp() {
        stateHolder = DevMenuUiStateHolder()
    }

    @Test
    fun `GIVEN a dev menu WHEN get state THEN assert headline and supporting list items`() {
        // Given
        val devMenu = DevMenu(
            backendEnvironment = BackendEnvironment.STAGING,
            entries = DevMenuEntry.entries,
        )

        // When
        val uiState = stateHolder.asUiState(devMenu)

        // Then
        val expected = DevMenuUiState.Loaded(
            items = persistentListOf(
                DevMenuUiState.ListItem(
                    headline = R.string.dev_menu_list_item_environment_headline,
                    supporting = R.string.dev_menu_list_item_featureflags_supporting,
                    trailing = R.string.dev_menu_list_item_environment_trailing_staging,
                ),
                DevMenuUiState.ListItem(
                    headline = R.string.dev_menu_list_item_gallery_headline,
                    supporting = R.string.dev_menu_list_item_gallery_supporting,
                ),
                DevMenuUiState.ListItem(
                    headline = R.string.dev_menu_list_item_featureflags_headline,
                    supporting = R.string.dev_menu_list_item_featureflags_supporting,
                ),
                DevMenuUiState.ListItem(
                    headline = R.string.dev_menu_list_item_entrypoints_headline,
                    supporting = R.string.dev_menu_list_item_entrypoints_supporting,
                ),
            ),
        )
        assertEquals(expected, uiState)
    }

    @ParameterizedTest
    @ArgumentsSource(BackendEnvironmentProvider::class)
    fun `GIVEN a backed environment WHEN get ui state THEN assert trailing list item`(
        backendEnvironment: BackendEnvironment,
        expectedTrailing: Int,
    ) {
        // Given
        val devMenu = DevMenu(
            backendEnvironment = backendEnvironment,
            entries = DevMenuEntry.entries,
        )

        // When
        val uiState = stateHolder.asUiState(devMenu) as DevMenuUiState.Loaded

        // Then
        assertEquals(
            expectedTrailing,
            uiState.items[0].trailing,
        )
    }

    @Test
    fun `GIVEN a dev menu WHEN select implemented item THEN return true`() {
        // Given
        val devMenu = DevMenu(
            backendEnvironment = BackendEnvironment.STAGING,
            entries = DevMenuEntry.entries,
        )
        stateHolder.asUiState(devMenu)

        // When
        assertTrue(stateHolder.isImplemented(1))
    }

    @Test
    fun `GIVEN a dev menu WHEN select not implemented item THEN return false`() {
        // Given
        val devMenu = DevMenu(
            backendEnvironment = BackendEnvironment.STAGING,
            entries = DevMenuEntry.entries,
        )
        stateHolder.asUiState(devMenu)

        // When
        assertFalse(stateHolder.isImplemented(0))
    }

    @ParameterizedTest
    @ArgumentsSource(DevEntryPerTargetProvider::class)
    fun `GIVEN a dev menu WHEN get navigation target THEN assert return`(
        entry: DevMenuEntry,
        expectedNavTarget: DevSettingsNavTarget,
    ) {
        // Given
        val devMenu = DevMenu(
            backendEnvironment = BackendEnvironment.STAGING,
            entries = listOf(entry),
        )
        stateHolder.asUiState(devMenu)

        // When
        val navTarget = stateHolder.getNavTarget(0)

        // Then
        assertEquals(expectedNavTarget, navTarget)
    }

    object BackendEnvironmentProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(BackendEnvironment.QA, R.string.dev_menu_list_item_environment_trailing_qa),
                Arguments.of(BackendEnvironment.STAGING, R.string.dev_menu_list_item_environment_trailing_staging),
                Arguments.of(BackendEnvironment.PRODUCTION, R.string.dev_menu_list_item_environment_trailing_production),
            )
        }
    }

    object DevEntryPerTargetProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(DevMenuEntry.Environment, DevSettingsNavTarget.BackendEnvironment),
                Arguments.of(DevMenuEntry.Gallery, GalleryNavTarget.Root),
                Arguments.of(DevMenuEntry.FeatureFlags, DevSettingsNavTarget.FeatureFlags),
                Arguments.of(DevMenuEntry.EntryPoint, DevSettingsNavTarget.EntryPoints),
            )
        }
    }
}

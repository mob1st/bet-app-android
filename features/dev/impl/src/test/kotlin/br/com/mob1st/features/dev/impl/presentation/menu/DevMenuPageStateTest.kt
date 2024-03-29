package br.com.mob1st.features.dev.impl.presentation.menu

import br.com.mob1st.core.design.atoms.properties.texts.Text
import br.com.mob1st.core.design.organisms.helper.HelperState
import br.com.mob1st.core.design.organisms.lists.ListItemState
import br.com.mob1st.core.design.organisms.snack.SnackState
import br.com.mob1st.core.kotlinx.errors.NoConnectivityException
import br.com.mob1st.features.dev.impl.R
import br.com.mob1st.features.dev.impl.domain.DevMenu
import br.com.mob1st.features.dev.impl.domain.DevMenuEntry
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.tests.featuresutils.fixture
import br.com.mob1st.tests.featuresutils.randomEnum
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

internal class DevMenuPageStateTest {

    @ParameterizedTest
    @ArgumentsSource(TestFailureArguments::class)
    fun `GIVEN a failure WHEN transform THEN assert state failed banner`(
        failure: Throwable,
        expected: HelperState,
    ) {
        val result = Result.failure<DevMenu>(failure)
        val actual = DevMenuPageState.transform(result = result) as DevMenuPageState.Failed
        assertEquals(
            expected = expected,
            actual = actual.helper
        )
    }

    @ParameterizedTest
    @ArgumentsSource(TestListItemsArguments::class)
    internal fun `GIVEN a dev menu WHEN transform THEN assert state loaded list`(
        entry: DevMenuEntry,
        expected: ListItemState,
    ) {
        val result = Result.success(DevMenu(entries = listOf(entry)))
        val actual = DevMenuPageState.transform(result = result) as DevMenuPageState.Loaded
        assertEquals(
            expected = expected,
            actual = actual.items.first()
        )
    }

    @Test
    fun `GIVEN a dev menu WHEN transform THEN assert all items were mapped`() {
        val result = devMenu()
        val actual = DevMenuPageState.transform(result = result) as DevMenuPageState.Loaded
        assertEquals(
            expected = result.getOrThrow().entries.size,
            actual = actual.items.size
        )
    }

    @Test
    fun `GIVEN a dev menu And a snackbar WHEN transform THEN assert state snackbar`() {
        val result = devMenu()
        val expected = SnackState(
            supporting = Text("a"),
            action = Text("b")
        )
        val actual = DevMenuPageState.transform(
            result = result,
            snack = expected
        ) as DevMenuPageState.Loaded
        assertEquals(
            expected = expected,
            actual = actual.snack
        )
    }

    @ParameterizedTest
    @ArgumentsSource(TestNavigationTargetArguments::class)
    fun `GIVEN a dev menu And a any selected item WHEN transform THEN assert state navigation target`(
        selectedItem: Int,
        expected: DevSettingsNavTarget?,
    ) {
        val result = devMenu()
        val actual = DevMenuPageState.transform(
            result = result,
            selectedItem = selectedItem
        ) as DevMenuPageState.Loaded
        assertEquals(
            expected = expected,
            actual = actual.navTarget
        )
    }

    private fun devMenu(
        currentEnv: BackendEnvironment = randomEnum(),
    ): Result<DevMenu> {
        val devMenu = DevMenu(currentEnv = currentEnv)
        return Result.success(devMenu)
    }

    private object TestFailureArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    Throwable(),
                    CommonError.Unknown.helper
                ),
                Arguments.of(
                    NoConnectivityException(fixture(), Throwable()),
                    CommonError.NoConnectivity.helper
                )
            )
        }
    }

    private object TestListItemsArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    DevMenuEntry.Environment(BackendEnvironment.QA),
                    ListItemState(
                        headline = Text(R.string.dev_menu_list_ite_environment_headline),
                        supporting = Text(R.string.dev_menu_list_item_featureflags_supporting),
                        trailing = ListItemState.Text(
                            Text(R.string.dev_menu_list_item_environment_trailing_qa)
                        )
                    )
                ),
                Arguments.of(
                    DevMenuEntry.Environment(BackendEnvironment.STAGING),
                    ListItemState(
                        headline = Text(R.string.dev_menu_list_ite_environment_headline),
                        supporting = Text(R.string.dev_menu_list_item_featureflags_supporting),
                        trailing = ListItemState.Text(
                            Text(R.string.dev_menu_list_item_environment_trailing_staging)
                        )
                    )
                ),
                Arguments.of(
                    DevMenuEntry.Environment(BackendEnvironment.PRODUCTION),
                    ListItemState(
                        headline = Text(R.string.dev_menu_list_ite_environment_headline),
                        supporting = Text(R.string.dev_menu_list_item_featureflags_supporting),
                        trailing = ListItemState.Text(
                            Text(R.string.dev_menu_list_item_environment_trailing_production)
                        )
                    )
                ),
                Arguments.of(
                    DevMenuEntry.FeatureFlags,
                    ListItemState(
                        headline = Text(R.string.dev_menu_list_item_featureflags_headline),
                        supporting = Text(R.string.dev_menu_list_item_featureflags_supporting)
                    )
                ),
                Arguments.of(
                    DevMenuEntry.Gallery,
                    ListItemState(
                        headline = Text(R.string.dev_menu_list_item_gallery_headline),
                        supporting = Text(R.string.dev_menu_list_item_gallery_supporting)
                    )
                ),
                Arguments.of(
                    DevMenuEntry.EntryPoint,
                    ListItemState(
                        headline = Text(R.string.dev_menu_list_item_entrypoints_headline),
                        supporting = Text(R.string.dev_menu_list_item_entrypoints_supporting)
                    )
                )
            )
        }
    }
    private object TestNavigationTargetArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    0,
                    null
                ),
                Arguments.of(
                    1,
                    null
                ),
                Arguments.of(
                    2,
                    DevSettingsNavTarget.Gallery
                ),
                Arguments.of(
                    3,
                    null
                )
            )
        }
    }
}

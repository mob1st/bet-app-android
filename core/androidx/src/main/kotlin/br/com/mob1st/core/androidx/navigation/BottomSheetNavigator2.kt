@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.mob1st.core.androidx.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.util.fastForEach
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorState
import androidx.navigation.compose.LocalOwnersProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * Makes a bottom sheet a possible destination in a navigation graph.
 * @param sheetState the state of the bottom sheet
 */
@Navigator.Name("bottomSheet")
class BottomSheetNavigator2(
    internal val sheetState: SheetState,
) : Navigator<BottomSheetNavigator2.Destination>() {
    internal var sheetEnabled by mutableStateOf(false)
        private set
    private var attached by mutableStateOf(false)

    internal var sheetContent: @Composable ColumnScope.() -> Unit = {}
    internal var onDismissRequest: () -> Unit = {}

    internal val sheetInitializer: @Composable () -> Unit = {
        val saveableStateHolder = rememberSaveableStateHolder()
        val transitionsInProgressEntries by transitionsInProgress.collectAsState()

        // The latest back stack entry, retained until the sheet is completely hidden
        // While the back stack is updated immediately, we might still be hiding the sheet, so
        // we keep the entry around until the sheet is hidden
        val retainedEntry by produceBackStackEntry(backStack = backStack) {
            // Always hide the sheet when the back stack is updated
            // Regardless of whether we're popping or pushing, we always want to hide
            // the sheet first before deciding whether to re-show it or keep it hidden
            sheetEnabled = false
        }
        val checkedRetainedEntry = retainedEntry
        if (checkedRetainedEntry != null) {
            LaunchOnSheetBecomeVisible(
                sheetState = sheetState,
                retainedEntry = retainedEntry,
                onSheetShown = {
                    transitionsInProgressEntries.forEach(state::markTransitionComplete)
                },
            )
            LaunchedEffect(retainedEntry) {
                sheetEnabled = true
                sheetContent = {
                    checkedRetainedEntry.LocalOwnersProvider(saveableStateHolder) {
                        val content = (checkedRetainedEntry.destination as Destination).content
                        content(checkedRetainedEntry)
                    }
                }
                onDismissRequest = { dismissSheet(transitionsInProgressEntries, checkedRetainedEntry) }
            }
            val scope = rememberCoroutineScope()
            BackHandler {
                scope
                    .launch { sheetState.hide() }
                    .invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest()
                        }
                    }
            }
        } else {
            LaunchedEffect(Unit) {
                sheetContent = {}
                onDismissRequest = {}
            }
        }
    }

    private fun dismissSheet(
        transitionsInProgressEntries: Set<NavBackStackEntry>,
        retainedEntry: NavBackStackEntry,
    ) {
        sheetEnabled = false

        if (transitionsInProgressEntries.contains(retainedEntry)) {
            // Sheet dismissal can be started through popBackStack in which case we have a
            // transition that we'll want to complete
            state.markTransitionComplete(retainedEntry)
        } else {
            // If there is no transition in progress, the sheet has been dimissed by the
            // user (for example by tapping on the scrim or through an accessibility action)
            // In this case, we will immediately pop without a transition as the sheet has
            // already been hidden
            state.pop(popUpTo = retainedEntry, saveState = false)
        }
    }

    /**
     * Get the back stack from the [state].
     */
    internal val backStack: StateFlow<List<NavBackStackEntry>>
        get() = if (attached) {
            state.backStack
        } else {
            MutableStateFlow(emptyList())
        }

    /**
     * Get the transitionsInProgress from the [state]. In some cases, the [sheetInitializer] might be
     * composed before the Navigator is attached, so we specifically return an empty flow if we
     * aren't attached yet.
     */
    internal val transitionsInProgress: StateFlow<Set<NavBackStackEntry>>
        get() = if (attached) {
            state.transitionsInProgress
        } else {
            MutableStateFlow(emptySet())
        }

    override fun createDestination(): Destination = Destination(
        this,
        content = {},
    )

    override fun onAttach(state: NavigatorState) {
        super.onAttach(state)
        attached = true
    }

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?,
    ) {
        entries.fastForEach { entry ->
            state.push(entry)
        }
    }

    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        state.pop(popUpTo, savedState)
    }

    /**
     * [NavDestination] specific to [BottomSheetNavigator2]
     */
    @NavDestination.ClassType(Composable::class)
    class Destination(
        navigator: BottomSheetNavigator2,
        internal val content: @Composable ColumnScope.(NavBackStackEntry) -> Unit,
    ) : NavDestination(navigator), FloatingWindow
}

@Composable
private fun produceBackStackEntry(
    backStack: StateFlow<List<NavBackStackEntry>>,
    onBackStackChange: () -> Unit,
): State<NavBackStackEntry?> = produceState<NavBackStackEntry?>(
    initialValue = null,
    key1 = backStack,
) {
    backStack
        .transform { backStackEntries ->
            try {
                onBackStackChange()
            } catch (_: CancellationException) {
                // We catch but ignore possible cancellation exceptions as we don't want
                // them to bubble up and cancel the whole produceState coroutine
            } finally {
                emit(backStackEntries.lastOrNull())
            }
        }
        .collect {
            value = it
        }
}

@Composable
private fun LaunchOnSheetBecomeVisible(
    sheetState: SheetState,
    retainedEntry: NavBackStackEntry?,
    onSheetShown: () -> Unit,
) {
    val currentOnSheetShown by rememberUpdatedState(onSheetShown)
    LaunchedEffect(sheetState, retainedEntry) {
        snapshotFlow { sheetState.isVisible }
            // We are only interested in changes in the sheet's visibility
            .distinctUntilChanged()
            // distinctUntilChanged emits the initial value which we don't need
            .drop(1)
            .collect { visible ->
                if (visible) {
                    currentOnSheetShown()
                }
            }
    }
}

/**
 * Create and remember a [BottomSheetNavigator]
 */
@Composable
fun rememberBottomSheetNavigator(
    skipPartiallyExpanded: Boolean = true,
): BottomSheetNavigator {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
    )

    return remember(sheetState) { BottomSheetNavigator(sheetState) }
}

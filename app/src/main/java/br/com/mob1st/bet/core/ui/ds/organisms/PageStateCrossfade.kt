package br.com.mob1st.bet.core.ui.ds.organisms

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun <T> PageStateCrossfade(
    modifier: Modifier = Modifier,
    pageState: PageState<T>,
    empty: @Composable (PageState.Empty<T>) -> Unit,
    helper: @Composable (PageState.Helper<T>) -> Unit,
    main: @Composable (PageState.Main<T>) -> Unit
) {
    val switcher = remember(pageState) {
        PageSwithcer(pageState)
    }
    Crossfade(modifier = modifier, targetState = switcher) { target ->
        when (val ps = target.pageState) {
            is PageState.Empty -> empty(ps)
            is PageState.Helper -> helper(ps)
            is PageState.Main -> main(ps)
        }
    }
}

@Stable
class PageSwithcer<T>(pageState: PageState<T>) {

    val state: State = when (pageState) {
        is PageState.Empty -> State.Empty
        is PageState.Helper -> State.Helper
        is PageState.Main -> State.Main
    }

    var pageState by mutableStateOf(pageState)

    override fun equals(other: Any?): Boolean {
        return (this === other) || (other is PageSwithcer<*> && state == other.state)
    }

    override fun hashCode(): Int {
        return state.hashCode()
    }

    enum class State {
        Empty, Helper, Main
    }
}

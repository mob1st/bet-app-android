package br.com.mob1st.bet.core.ui.ds.organisms

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.FetchedData
import br.com.mob1st.bet.core.ui.state.SimpleMessage

/**
 * Apply crossfade transitions to replace the state of the UI based in the give [state]
 *
 * Make the [AsyncState.data] type implements [FetchedData] to be able to determine what happens
 * when the asynchronous operation have finished but still with no data provided
 *
 * @param state the state used as key to determine the changes in the animation
 * @param emptyError what will be shown in case of the error and no data is available
 * @param emptyLoading what will be shown in case the screen still empty and the loading of the
 * data is not finished yed. It it typically the first composable the user see
 * @param empty what will be shown when the load is finished but there is no data to be displayed
 * @param data what will be shown when there is data. It can be called with loading or error if the
 * [FetchedData.hasData] is true
 */
@Composable
fun <T : FetchedData> FetchedCrossfade(
    state: AsyncState<T>,
    emptyError: @Composable (state: AsyncState<T>, firstMessage: SimpleMessage) -> Unit,
    emptyLoading: @Composable (state: AsyncState<T>) -> Unit,
    empty: @Composable (state: AsyncState<T>) -> Unit,
    data: @Composable (state: AsyncState<T>) -> Unit,
) {
    Crossfade(targetState = state) { current ->
        when {
            current.data.hasData() -> {
                data(current)
            }
            current.messages.isEmpty() && current.loading -> {
                emptyLoading(current)
            }
            current.messages.isEmpty() -> {
                empty(current)
            }
            else -> emptyError(current, current.messages.first())
        }
    }
}
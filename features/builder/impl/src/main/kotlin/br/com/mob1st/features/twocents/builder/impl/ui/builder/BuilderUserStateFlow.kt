package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import br.com.mob1st.core.androidx.parcelables.getStateProviderParcelable
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A wrapper on top of the given [SavedStateHandle] to save and restore the user inputs through the [BuilderUserInput].
 * @param savedStateHandle The [SavedStateHandle] to save and restore the user inputs.
 */
internal class BuilderUserStateFlow(
    private val savedStateHandle: SavedStateHandle,
) : MutableStateFlow<BuilderUserInput> by MutableStateFlow(
        savedStateHandle.getStateProviderParcelable(
            providerKey = INPUT_PROVIDER_KEY,
            bundleKey = INPUT_BUNDLE_KEY,
            defaultValue = BuilderUserInput(),
        ),
    ) {
    init {
        savedStateHandle.setSavedStateProvider(INPUT_PROVIDER_KEY) {
            bundleOf(INPUT_BUNDLE_KEY to value)
        }
    }

    companion object {
        private const val INPUT_PROVIDER_KEY = "INPUT_PROVIDER_KEY"
        private const val INPUT_BUNDLE_KEY = "INPUT_BUNDLE_KEY"
    }
}

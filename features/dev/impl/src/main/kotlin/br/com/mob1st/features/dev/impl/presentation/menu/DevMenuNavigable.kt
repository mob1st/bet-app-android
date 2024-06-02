package br.com.mob1st.features.dev.impl.presentation.menu

import br.com.mob1st.core.design.atoms.properties.navigations.Navigable
import br.com.mob1st.features.dev.impl.domain.DevMenuEntry
import br.com.mob1st.features.dev.impl.presentation.gallery.GalleryNavTarget
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget

internal data class DevMenuNavigable(
    private val entry: DevMenuEntry,
) : Navigable<DevSettingsNavTarget> {
    override val navTarget: DevSettingsNavTarget =
        when (entry) {
            DevMenuEntry.Environment -> DevSettingsNavTarget.BackendEnvironment
            DevMenuEntry.Gallery -> GalleryNavTarget.Root
            DevMenuEntry.FeatureFlags -> DevSettingsNavTarget.FeatureFlags
            DevMenuEntry.EntryPoint -> DevSettingsNavTarget.EntryPoints
        }
}

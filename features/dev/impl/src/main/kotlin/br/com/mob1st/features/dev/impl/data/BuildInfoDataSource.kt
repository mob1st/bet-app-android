package br.com.mob1st.features.dev.impl.data

import br.com.mob1st.features.dev.publicapi.domain.BuildInfo

/**
 * Provides the build info from app's module.
 * The implementation of this interface should be found in app modules type, once it should correspond to the app build
 * config, not the libraries.
 */
interface BuildInfoDataSource {

    /**
     * The build info data.
     */
    val data: BuildInfo
}

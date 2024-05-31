package br.com.mob1st.bet.features.dev

import br.com.mob1st.bet.BuildConfig
import br.com.mob1st.features.dev.impl.infra.BuildInfoDataSource
import br.com.mob1st.features.dev.publicapi.domain.AppVersion
import br.com.mob1st.features.dev.publicapi.domain.BuildInfo

/**
 * Default implementation for [BuildInfoDataSource].
 */
internal object AppBuildInfoDataSource : BuildInfoDataSource {
    override val data: BuildInfo =
        BuildInfo(
            version =
                AppVersion(
                    name = BuildConfig.VERSION_NAME,
                    code = BuildConfig.VERSION_CODE,
                ),
            isReleaseBuild = !BuildConfig.DEBUG,
        )
}

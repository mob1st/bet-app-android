package br.com.mob1st.features.finances.impl.infra.data.system

import br.com.mob1st.core.kotlinx.structures.Uri

/**
 * Provides the absolute path of a file in the app's private assets folder.
 */
interface AssetsGetter {
    /**
     * Given a [fileName], returns the absolute path of the file in the app's private assets folder.
     */
    operator fun get(fileName: String): Uri
}

/**
 * Implementation of [AssetsGetter] for Android.
 * It returns a hardcoded URI with the file path in the assets folder.
 */
internal object AndroidAssetsGetter : AssetsGetter {
    override operator fun get(fileName: String): Uri {
        return Uri("file:///android_asset/$fileName")
    }
}

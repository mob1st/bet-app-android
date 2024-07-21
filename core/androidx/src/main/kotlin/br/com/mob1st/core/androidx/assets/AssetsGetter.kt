package br.com.mob1st.core.androidx.assets

import java.io.File

/**
 * Provides the absolute path of a file in the app's private assets folder.
 */
interface AssetsGetter {
    /**
     * Given a [fileName], returns the absolute path of the file in the app's private assets folder.
     */
    operator fun get(fileName: String): File
}

/**
 * Implementation of [AssetsGetter] for Android.
 * It returns a hardcoded URI with the file path in the assets folder.
 */
object AndroidAssetsGetter : AssetsGetter {
    override operator fun get(fileName: String): File {
        return File("file:///android_asset/$fileName")
    }
}

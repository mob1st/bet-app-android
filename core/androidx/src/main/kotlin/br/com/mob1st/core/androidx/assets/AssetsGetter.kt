package br.com.mob1st.core.androidx.assets

import android.content.Context
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.CoroutineContext

/**
 * Provides the absolute path of a file in the app's private assets folder.
 */
interface AssetsGetter {
    /**
     * Given a [fileName], returns the absolute path of the file in the app's private assets folder.
     */
    suspend fun get(fileName: String): String?
}

/**
 * Implementation of [AssetsGetter] for Android.
 * It returns a hardcoded URI with the file path in the assets folder.
 */
class AndroidAssetsGetter(
    context: Context,
    private val io: CoroutineContext,
) : AssetsGetter {
    private val assetManager = context.assets

    override suspend fun get(fileName: String): String? = withContext(io) {
        try {
            // tries to open the file to check if it exists
            assetManager.open(fileName).close()
            "file:///android_asset/$fileName"
        } catch (e: IOException) {
            Timber.w(e, "Error getting asset file $fileName from assets")
            null
        }
    }
}

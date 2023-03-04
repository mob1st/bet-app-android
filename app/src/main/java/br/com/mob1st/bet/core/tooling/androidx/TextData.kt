package br.com.mob1st.bet.core.tooling.androidx

import android.content.res.Resources
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import br.com.mob1st.bet.R
import kotlinx.parcelize.Parcelize

/**
 * A wrapper for a text can be displayed on UI like a String or a id resource
 */
sealed interface TextData : Parcelable {

    fun resolve(resources: Resources): String

    @Parcelize
    data class ActualText(val text: String) : TextData {
        override fun resolve(resources: Resources): String {
            return text
        }
    }

    @Parcelize
    data class IdRes(@StringRes val id: Int) : TextData {
        override fun resolve(resources: Resources): String {
            return resources.getString(id)
        }
    }

    companion object {
        val Retry = TextData(R.string.retry)
    }
}

fun TextData(text: String): TextData = TextData.ActualText(text)
fun TextData(@StringRes id: Int): TextData = TextData.IdRes(id)

@Composable
fun TextData.resolve(): String {
    val context = LocalContext.current
    return resolve(resources = context.resources)
}

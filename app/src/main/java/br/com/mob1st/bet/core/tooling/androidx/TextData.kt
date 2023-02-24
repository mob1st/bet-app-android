package br.com.mob1st.bet.core.tooling.androidx

import android.content.res.Resources
import androidx.annotation.StringRes
import br.com.mob1st.bet.R

/**
 * A wrapper for a text can be displayed on UI like a String or a id resource
 */
sealed interface TextData {

    fun resolve(resources: Resources): String

    data class ActualText(val text: String) : TextData {
        override fun resolve(resources: Resources): String {
            return text
        }
    }

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
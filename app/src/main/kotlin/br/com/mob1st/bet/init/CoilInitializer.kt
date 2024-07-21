package br.com.mob1st.bet.init

import android.content.Context
import androidx.startup.Initializer
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger

class CoilInitializer : Initializer<Coil> {
    override fun create(context: Context): Coil {
        Coil.setImageLoader(
            ImageLoader.Builder(context)
                .components {
                    add(SvgDecoder.Factory())
                }
                .logger(DebugLogger())
                .crossfade(true)
                .build(),
        )
        return Coil
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}

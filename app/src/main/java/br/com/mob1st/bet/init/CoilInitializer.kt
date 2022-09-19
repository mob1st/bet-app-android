package br.com.mob1st.bet.init

import android.content.Context
import androidx.startup.Initializer
import coil.Coil
import coil.ImageLoader

class CoilInitializer : Initializer<Coil> {
    override fun create(context: Context): Coil {
        Coil.setImageLoader(
            ImageLoader.Builder(context)
                .crossfade(true)
                .build()
        )
        return Coil
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
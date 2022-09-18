package br.com.mob1st.bet

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory

class BetApp : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .build()
    }
}
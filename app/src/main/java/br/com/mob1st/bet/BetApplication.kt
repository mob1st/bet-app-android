package br.com.mob1st.bet

import android.app.Application
import br.com.mob1st.bet.di.Injector

class BetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }

}
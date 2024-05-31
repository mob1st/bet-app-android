package br.com.mob1st.bet.features.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import br.com.mob1st.bet.core.tooling.androidx.getParcelableNotNull
import br.com.mob1st.bet.core.ui.compose.setThemedContent
import br.com.mob1st.bet.features.profile.data.Subscription

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val entry = intent.getParcelableNotNull<Subscription>(SUBSCRIPTION_ENTRY_KEY)
        setThemedContent {
            HomeScreen(entry)
        }
    }

    companion object {
        private const val SUBSCRIPTION_ENTRY_KEY = "SUBSCRIPTION_ENTRY_KEY"

        context(Intent)
        fun put(entry: Subscription) = putExtra(SUBSCRIPTION_ENTRY_KEY, entry)
    }
}

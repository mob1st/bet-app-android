package br.com.mob1st.bet.features.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import br.com.mob1st.bet.core.ui.compose.setThemedContent
import br.com.mob1st.bet.core.utils.extensions.getParcelableNotNull
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val entry = intent.getParcelableNotNull<CompetitionEntry>(COMPETITION_ENTRY_KEY)
        setThemedContent {
            HomeScreen(entry)
        }
    }

    companion object {
        private const val COMPETITION_ENTRY_KEY = "COMPETITION_ENTRY_KEY"
        context(Intent)
        fun put(entry: CompetitionEntry) = putExtra(COMPETITION_ENTRY_KEY, entry)
    }
}
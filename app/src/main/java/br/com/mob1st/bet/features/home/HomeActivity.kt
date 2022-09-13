package br.com.mob1st.bet.features.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import br.com.mob1st.bet.core.ui.compose.setThemedContent

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemedContent {
            HomeScreen()
        }
    }
}
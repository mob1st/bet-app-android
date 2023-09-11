package br.com.mob1st.features.dev.impl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.mob1st.features.dev.impl.menu.presentation.MenuViewModel
import org.koin.androidx.compose.koinViewModel

class DevSettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            koinViewModel<MenuViewModel>()
        }
    }
}

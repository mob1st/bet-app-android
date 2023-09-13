package br.com.mob1st.features.dev.impl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.coroutineScope
import br.com.mob1st.features.dev.impl.menu.presentation.DevMenuViewModel
import org.koin.androidx.compose.koinViewModel

class DevSettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.coroutineScope
        setContent {
            koinViewModel<DevMenuViewModel>()
        }
    }
}

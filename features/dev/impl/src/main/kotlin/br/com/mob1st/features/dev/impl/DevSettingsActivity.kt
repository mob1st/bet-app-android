package br.com.mob1st.features.dev.impl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.mob1st.features.dev.impl.presentation.MenuViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DevSettingsActivity : ComponentActivity() {

    private val menuViewModel by viewModel<MenuViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm = koinViewModel<MenuViewModel>()
        }
    }
}

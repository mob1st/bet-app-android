package br.com.mob1st.bet.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import br.com.mob1st.bet.Greeting
import br.com.mob1st.bet.features.auth.SplashViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<SplashViewModel>()
    val state by viewModel.uiState.collectAsState()

    Scaffold {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Greeting("Android ${state.data}")
            if (state.singleShotEvents.isNotEmpty()) {
                val firstEvent = state.singleShotEvents.first()
                Greeting("Show First Event ${firstEvent.data}")
                viewModel.consumeEvent(firstEvent)
            }
        }
    }
}
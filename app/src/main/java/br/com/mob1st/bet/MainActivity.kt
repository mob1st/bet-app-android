package br.com.mob1st.bet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import br.com.mob1st.bet.features.auth.SplashViewModel
import br.com.mob1st.bet.ui.theme.BetTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BetTheme {
                val viewModel = koinViewModel<SplashViewModel>()
                val state by viewModel.uiState.collectAsState()
                // A surface container using the 'background' color from the theme
                Scaffold{
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
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BetTheme {
        Greeting("Android")
    }
}
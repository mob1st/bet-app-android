package br.com.mob1st.bet.core.ui.ds.molecule

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import br.com.mob1st.bet.core.ui.compose.LocalDarkTime
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Set the default system bar colors of the app
 *
 * Provide custom [statusBarProperties] or [navigationBarProperties] if some custom color should be
 * customized.
 *
 * @see [SystemBarsDefaults]
 */
@Composable
fun SystemBars(
    statusBarProperties: SystemBarProperties = SystemBarsDefaults.statusBar,
    navigationBarProperties: SystemBarProperties = SystemBarsDefaults.navigationBar,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(
                color = statusBarProperties.color,
                darkIcons = statusBarProperties.darkIcons
            )
            systemUiController.setNavigationBarColor(
                color = navigationBarProperties.color,
                darkIcons = navigationBarProperties.darkIcons
            )
        }
    }
}

/**
 * Properties available for customization of the system bars
 */
data class SystemBarProperties(
    val color: Color,
    val darkIcons: Boolean,
)

/**
 * Default values for [SystemBarProperties]
 */
object SystemBarsDefaults {

    val statusBar @Composable get() = SystemBarProperties(
        color = MaterialTheme.colorScheme.primary,
        darkIcons = LocalDarkTime.current
    )

    val navigationBar @Composable get() = SystemBarProperties(
        color = MaterialTheme.colorScheme.surface,
        darkIcons = !LocalDarkTime.current
    )
}

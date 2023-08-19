package br.com.mob1st.core.design

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.atoms.theme.LocalDisplayFeatures
import br.com.mob1st.core.design.atoms.theme.LocalWindowWidthSizeClass
import com.google.accompanist.adaptive.calculateDisplayFeatures

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun DesignSystem(activity: Activity, content: @Composable () -> Unit) {
    val displayFeatures = calculateDisplayFeatures(activity = activity)
    val windowSizeClass = calculateWindowSizeClass(activity = activity)
    CompositionLocalProvider(
        LocalDisplayFeatures provides displayFeatures,
        LocalWindowWidthSizeClass provides windowSizeClass.widthSizeClass
    ) {
        BetTheme(content)
    }
}

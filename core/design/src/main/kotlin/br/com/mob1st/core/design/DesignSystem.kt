package br.com.mob1st.core.design

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.atoms.theme.LocalDisplayFeatures
import br.com.mob1st.core.design.atoms.theme.LocalLayoutSpec
import br.com.mob1st.core.design.templates.LayoutSpec
import com.google.accompanist.adaptive.calculateDisplayFeatures

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun DesignSystem(activity: Activity, content: @Composable () -> Unit) {
    val displayFeatures = calculateDisplayFeatures(activity = activity)
    val windowSizeClass = calculateWindowSizeClass(activity = activity)
    val layoutSpec = remember(windowSizeClass.widthSizeClass) {
        LayoutSpec.of(windowSizeClass.widthSizeClass)
    }
    CompositionLocalProvider(
        LocalDisplayFeatures provides displayFeatures,
        LocalLayoutSpec provides layoutSpec
    ) {
        BetTheme(content)
    }
}

package br.com.mob1st.bet.features.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarData(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarData(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Overview : BottomBarData(
        route = "overview",
        title = "Overview",
        icon = Icons.Default.Star
    )

    object Profile : BottomBarData(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
}

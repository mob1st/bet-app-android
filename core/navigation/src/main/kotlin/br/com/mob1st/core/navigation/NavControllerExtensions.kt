package br.com.mob1st.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.navigate(navTarget: NavTarget, builder: NavOptionsBuilder.() -> Unit) {
    navigate(navTarget.screenName, builder)
}
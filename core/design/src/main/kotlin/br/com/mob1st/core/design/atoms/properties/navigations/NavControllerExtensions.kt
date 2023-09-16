package br.com.mob1st.core.design.atoms.properties.navigations

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.navigate(navTarget: NavTarget, builder: NavOptionsBuilder.() -> Unit) {
    navigate(navTarget.screenName, builder)
}

fun NavController.navigate(navTarget: NavTarget) {
    navigate(navTarget.screenName)
}

package br.com.mob1st.features.onboarding.impl.ui

import androidx.navigation.NavHostController
import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget

fun NavHostController.navigate(route: NavTarget) = navigate(route.screenName)

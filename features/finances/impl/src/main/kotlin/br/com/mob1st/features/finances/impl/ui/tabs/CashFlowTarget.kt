package br.com.mob1st.features.finances.impl.ui.tabs

import br.com.mob1st.core.design.motion.transition.NavTarget
import br.com.mob1st.core.design.motion.transition.TransitionPattern
import kotlinx.serialization.Serializable

sealed interface CashFlowTarget : NavTarget {
    @Serializable
    data object MainTab : CashFlowTarget {
        override fun pattern(): TransitionPattern = TransitionPattern.Slide
    }
}

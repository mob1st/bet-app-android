package br.com.mob1st.features.finances.impl.ui

import br.com.mob1st.core.design.motion.transition.NavTarget
import br.com.mob1st.core.design.motion.transition.TransitionPattern
import kotlinx.serialization.Serializable

sealed interface CashFlowTarget : NavTarget {
    @Serializable
    data object MainTab : CashFlowTarget {
        override val pattern: TransitionPattern = TransitionPattern.Slide
    }
}

package br.com.mob1st.features.twocents.builder.impl.ui

import br.com.mob1st.core.design.motion.transition.NavTarget
import br.com.mob1st.core.design.motion.transition.TransitionPattern
import kotlinx.serialization.Serializable

sealed interface BuilderNavTarget : NavTarget {
    @Serializable
    data object Summary : BuilderNavTarget {
        override val pattern: TransitionPattern = TransitionPattern.TopLevel
    }

    @Serializable
    data object FixedCosts : BuilderNavTarget {
        override val pattern: TransitionPattern = TransitionPattern.Slide
    }

    @Serializable
    data object VariableCosts : BuilderNavTarget {
        override val pattern: TransitionPattern = TransitionPattern.Slide
    }

    @Serializable
    data object Income : BuilderNavTarget {
        override val pattern: TransitionPattern = TransitionPattern.Slide
    }
}

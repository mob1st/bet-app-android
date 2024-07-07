package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.core.design.motion.transition.NavTarget
import br.com.mob1st.core.design.motion.transition.TransitionPattern
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import kotlinx.serialization.Serializable

sealed interface CategoryBuilderRoute : NavTarget {
    @Serializable
    data object Intro : CategoryBuilderRoute {
        override val pattern: TransitionPattern = TransitionPattern.TopLevel
    }

    @Serializable
    data class Step(
        val args: BuilderNextAction.Step,
    ) : CategoryBuilderRoute {
        override val pattern: TransitionPattern = TransitionPattern.Slide
    }

    @Serializable
    data object BuilderCompletion : CategoryBuilderRoute {
        override val pattern: TransitionPattern = TransitionPattern.Slide
    }

    companion object {
        fun from(nextAction: BuilderNextAction): CategoryBuilderRoute {
            return when (nextAction) {
                is BuilderNextAction.Step -> Step(nextAction)
                is BuilderNextAction.Complete -> BuilderCompletion
            }
        }
    }
}

package br.com.mob1st.core.design.molecules.avatar

import br.com.mob1st.core.design.atoms.properties.icons.Icon

sealed interface AvatarState {
    @JvmInline
    value class Static(val icon: Icon) : AvatarState

    @JvmInline
    value class Dynamic(val url: String) : AvatarState

    data class Initials(val initials: String, val background: BackgroundType) : AvatarState {
        init {
            require(initials.length <= 2) {
                "Initials must be 2 characters or less. Current is $initials"
            }
        }

        enum class BackgroundType {
            FIRST,
            SECOND,
            THIRD,
            FOURTH,
        }
    }
}

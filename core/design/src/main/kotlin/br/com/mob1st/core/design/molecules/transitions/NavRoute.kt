package br.com.mob1st.core.design.molecules.transitions

interface NavRoute {
    fun enteringPattern(): PatternKey
}

sealed interface PatternKey {
    data object BackAndForward : PatternKey

    data object TopLevel : PatternKey
}

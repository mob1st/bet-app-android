package br.com.mob1st.core.design.molecules.transitions

interface NavRoute {
    val enteringPatternKey: PatternKey
}

enum class PatternKey {
    BackAndForward,
    TopLevel,
}

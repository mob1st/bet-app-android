package br.com.mob1st.bet.features.profile

data class User(
    val id: String,
    val name: String = "",
    val email: String? = null,
)

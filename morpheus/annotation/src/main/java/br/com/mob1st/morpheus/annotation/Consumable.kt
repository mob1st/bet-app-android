package br.com.mob1st.morpheus.annotation

data class Consumable<K, T>(
    val key: K,
    val effect: T,
)

package br.com.mob1st.morpheus.annotation.strategy

interface ConsumptionStrategy<T> {

    fun consume(value: T): T
}

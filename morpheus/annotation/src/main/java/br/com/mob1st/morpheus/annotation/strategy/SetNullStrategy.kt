package br.com.mob1st.morpheus.annotation.strategy

object SetNullStrategy : ConsumptionStrategy<Any?> {
    override fun consume(value: Any?): Any? {
        return null
    }
}

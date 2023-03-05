package br.com.mob1st.morpheus.annotation.strategy

import br.com.mob1st.morpheus.annotation.utils.poll

class QueueStrategy<T> : ConsumptionStrategy<List<T>> {
    override fun consume(value: List<T>): List<T> {
        return value.poll()
    }
}

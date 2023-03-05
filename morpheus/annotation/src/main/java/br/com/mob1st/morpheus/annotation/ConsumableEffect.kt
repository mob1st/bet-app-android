package br.com.mob1st.morpheus.annotation

import br.com.mob1st.morpheus.annotation.strategy.ConsumptionStrategy
import br.com.mob1st.morpheus.annotation.strategy.SetNullStrategy
import kotlin.reflect.KClass

/**
 * Annotates a property that should have its value changed
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ConsumableEffect(
    val value: KClass<out ConsumptionStrategy<*>> = SetNullStrategy::class
)

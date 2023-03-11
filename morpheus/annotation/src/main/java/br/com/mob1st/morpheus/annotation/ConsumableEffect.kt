package br.com.mob1st.morpheus.annotation

/**
 * Annotates a property that should have its value changed
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ConsumableEffect

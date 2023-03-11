package br.com.mob1st.morpheus.annotation

/**
 * Annotates a class that can produce side-effects that should be consumed and cleaned by the ui
 * This annotation will only work on data classes
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Morpheus

package br.com.mob1st.morpheus.annotation

/**
 * Annotates a class that can produce side-effects that should be consumed and cleaned by the ui
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Morpheus

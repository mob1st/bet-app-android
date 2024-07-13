package br.com.mob1st.core.design.atoms.colors.material.families

/**
 * Can be implemented by companion objects of colors families to create light and dark instances of the family.
 * @param T the family type
 */
interface FamilyThemeVariator<T> {
    /**
     * Creates a light instance of the family.
     */
    fun light(): T

    /**
     * Creates a dark instance of the family.
     */
    fun night(): T
}

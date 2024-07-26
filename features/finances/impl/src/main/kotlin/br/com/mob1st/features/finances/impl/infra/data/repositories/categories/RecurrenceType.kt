package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

/**
 * Represents the recurrence type of a category that is saved in the database as a TEXT.
 * @property value The value of the recurrence type. Do not use the Enum#name property to get the value, since it can
 * break older versions of the app if changed.
 */
internal enum class RecurrenceType(val value: String) {
    Fixed("fixed"),
    Variable("variable"),
    Seasonal("seasonal"),
    ;

    companion object {
        private val valueToEnum = mapOf(
            "fixed" to Fixed,
            "variable" to Variable,
            "seasonal" to Seasonal,
        )

        /**
         * Returns the [RecurrenceType] from the given [value].
         * @param value the value to convert to [RecurrenceType].
         * @return the [RecurrenceType] from the given [value]
         * @throws IllegalArgumentException if the given [value] is unknown.
         */
        fun fromValue(value: String): RecurrenceType {
            return requireNotNull(valueToEnum[value]) {
                "Unknown recurrence type: $value"
            }
        }
    }
}

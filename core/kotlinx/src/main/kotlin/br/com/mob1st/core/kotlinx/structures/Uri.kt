package br.com.mob1st.core.kotlinx.structures

/**
 * Universal Resource Identifier.
 * It can be an URL pointing to a web resource or a URI pointing to a local file.
 */
@JvmInline
value class Uri(val value: String) {
    init {
        require(value.isNotBlank()) {
            "The URI must not be blank."
        }
    }

    override fun toString(): String {
        return value
    }
}

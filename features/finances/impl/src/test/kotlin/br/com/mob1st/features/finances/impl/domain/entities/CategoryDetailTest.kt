package br.com.mob1st.features.finances.impl.domain.entities

import org.junit.jupiter.api.Test

class CategoryDetailTest {
    @Test
    fun test() {
        assert(CategoryDetail::category.name == "peido")
    }
}

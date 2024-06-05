package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.features.finances.publicapi.domain.entities.Transaction

data class TransactionList(
    val transactions: List<Transaction>,
)

package br.com.mob1st.bet.features.bet

import br.com.mob1st.bet.core.ui.BaseViewModel
import br.com.mob1st.bet.features.auth.OpenAppUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BetViewModel(
    private val openAppUseCase: OpenAppUseCase,
    private val getBetListUseCase: GetBetListUseCase
) : BaseViewModel<List<Guess>>(emptyList()) {

    init {
        fetchData()
    }

    override fun dataFlow(): Flow<List<Guess>> = emptyFlow()
}
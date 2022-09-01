package br.com.mob1st.bet.features.auth

import br.com.mob1st.bet.core.BaseViewModel
import kotlinx.coroutines.flow.flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SplashViewModel(
    private val openAppUseCase: OpenAppUseCase
) : BaseViewModel<SplashData>(SplashData.Waiting) {

    init {
        fetchData()
    }

    override fun dataFlow() = flow {
        openAppUseCase()
        emit(SplashData.SignedIn)
    }

}

sealed class SplashData {
    object Waiting : SplashData() {
        override fun toString(): String {
            return "Waiting"
        }
    }
    object SignedIn : SplashData() {
        override fun toString(): String {
            return "SignedIn"
        }
    }
}


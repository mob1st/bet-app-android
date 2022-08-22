package br.com.mob1st.bet.features.auth

import br.com.mob1st.bet.core.BaseViewModel
import kotlinx.coroutines.flow.flow

class SplashViewModel(
    private val openAppUseCase: OpenAppUseCase
) : BaseViewModel<SplashData>(SplashData.Waiting) {



    override fun fetch() = flow {
        openAppUseCase()
        emit(SplashData.SignedIn)
    }

}

sealed class SplashData {
    object Waiting : SplashData() {
        override fun toString(): String {
            return "Waitinh"
        }
    }
    object SignedIn : SplashData() {
        override fun toString(): String {
            return "SignedIn"
        }
    }
}


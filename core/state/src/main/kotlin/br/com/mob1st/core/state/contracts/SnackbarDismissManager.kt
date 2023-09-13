package br.com.mob1st.core.state.contracts

fun interface SnackbarDismissManager {

    fun dismissSnack()
}

interface SnackbarPerformManager : SnackbarDismissManager {
    fun performSnackAction()
}

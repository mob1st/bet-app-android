package br.com.mob1st.tests.featuresutils

import br.com.mob1st.core.state.managers.ErrorHandler

class FakeErrorHandler : ErrorHandler() {
    var error: Throwable? = null

    override fun catch(throwable: Throwable) {
        this.error = throwable
    }
}

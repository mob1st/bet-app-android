package br.com.mob1st.tests.unit

import io.kotest.common.ExperimentalKotest
import io.kotest.core.annotation.AutoScan
import io.kotest.core.config.AbstractProjectConfig

@AutoScan
object CommonProjectSetup : AbstractProjectConfig() {

    override val coroutineTestScope: Boolean = true

    @ExperimentalKotest
    override var testCoroutineDispatcher: Boolean = true
}

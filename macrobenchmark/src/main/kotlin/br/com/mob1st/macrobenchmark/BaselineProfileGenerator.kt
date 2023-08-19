package br.com.mob1st.macrobenchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import org.junit.Rule
import org.junit.Test

class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() = baselineProfileRule.collect(
        packageName = "br.com.mob1st.bet",
        profileBlock = {
            startActivityAndWait()
        }
    )
}

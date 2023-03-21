package br.com.mob1st.bet.macrobenchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import org.junit.Rule
import org.junit.Test

class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() = baselineProfileRule.collectBaselineProfile(
        packageName = "br.com.mob1st.bet",
        profileBlock = {
            startActivityAndWait()
        }
    )
}

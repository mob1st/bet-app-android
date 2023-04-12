package br.com.mob1st.libs.firebase

import br.com.mob1st.core.observability.debug.Debuggable
import br.com.mob1st.libs.firebase.crashlytics.FirebaseCrashlyticsReportingTool
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk

class DebuggableToCrashlyticsTest : BehaviorSpec({

    Given("a debuggable exception") {
        val firebaseCrashlyticsReportingTool = FirebaseCrashlyticsReportingTool(mockk())
        When("convert to crashlytics") {

            Then("it should log the custom info") {
                // TODO
            }
        }
    }
})

private val ALLOWED_DEBUGGABLE = object : Debuggable {
    override val logInfo: Map<String, Any?>
        get() = TODO("Not yet implemented")
}

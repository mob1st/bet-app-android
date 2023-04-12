package br.com.mob1st.libs.firebase

import br.com.mob1st.libs.firebase.crashlytics.FirebaseCrashlyticsReportingTool
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

class CrashlyticsUserSessionTest : FunSpec({

    lateinit var crashlytics: FirebaseCrashlytics
    lateinit var reportingTool: FirebaseCrashlyticsReportingTool

    beforeAny {
        crashlytics = mockk()
        every { crashlytics.setUserId(any()) } just runs
        reportingTool = FirebaseCrashlyticsReportingTool(crashlytics)
    }

    test("register user should call setUserId") {
        reportingTool shouldBe reportingTool
    }
})

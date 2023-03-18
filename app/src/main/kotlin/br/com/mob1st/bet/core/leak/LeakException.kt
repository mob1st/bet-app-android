package br.com.mob1st.bet.core.leak

import br.com.mob1st.bet.core.logs.Debuggable
import shark.HeapAnalysisSuccess
import shark.Leak
import shark.LeakTrace
import shark.LeakTraceReference

internal class LeakException(
    private val heapAnalysis: HeapAnalysisSuccess,
    private val leak: Leak,
    private val leakTrace: LeakTrace,
) : Exception("Memory leak: ${leak.shortDescription}. See LEAK tab."), Debuggable {

    private val fakeStackTrace: Array<StackTraceElement>

    init {
        val stackTrace = mutableListOf<StackTraceElement>()
        stackTrace.add(
            StackTraceElement(
                "GcRoot",
                leakTrace.gcRootType.name,
                "GcRoot.kt",
                42
            )
        )
        for (cause in leakTrace.referencePath) {
            stackTrace.add(buildStackTraceElement(cause))
        }
        fakeStackTrace = stackTrace.toTypedArray()
    }

    private fun buildStackTraceElement(reference: LeakTraceReference): StackTraceElement {
        val file = reference.owningClassName.substringAfterLast(".") + ".kt"
        return StackTraceElement(
            reference.owningClassName,
            reference.referenceDisplayName,
            file,
            GC_ROOT_STACKTRACE_LINE
        )
    }

    override fun getStackTrace(): Array<StackTraceElement> {
        return fakeStackTrace
    }

    override fun logProperties(): Map<String, Any?> {
        return heapAnalysis.toProperties() +
            leak.toProperties() +
            leakTrace.toProperties()
    }

    companion object {
        private const val GC_ROOT_STACKTRACE_LINE = 42
    }
}

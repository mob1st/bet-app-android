package br.com.mob1st.bet.core.leak

import br.com.mob1st.bet.core.logs.Debuggable
import shark.HeapAnalysisSuccess

internal class NoLeakException(
    private val heapAnalysisSuccess: HeapAnalysisSuccess,
) : Exception("This is just a way to indicate has no leak in the current release "), Debuggable {

    override fun logProperties(): Map<String, Any?> {
        return heapAnalysisSuccess.toProperties()
    }
}

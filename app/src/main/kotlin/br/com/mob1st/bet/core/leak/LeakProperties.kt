package br.com.mob1st.bet.core.leak

import shark.HeapAnalysisSuccess
import shark.Leak
import shark.LeakTrace
import shark.LibraryLeak

internal fun HeapAnalysisSuccess.toProperties() = mapOf(
    "heapDumpFile" to heapDumpFile.absolutePath,
    "analysisDurationMs" to analysisDurationMillis
) + metadata

internal fun Leak.toProperties() = buildMap<String, Any> {
    this["libraryLeak"] = this@toProperties is LibraryLeak
    if (this@toProperties is LibraryLeak) {
        this["libraryLeakPattern"] = pattern.toString()
        this["libraryLeakDescription"] = description
    }
}

internal fun LeakTrace.toProperties() = mapOf<String, Any>(
    "retainedHeapByteSize" to (retainedHeapByteSize ?: 0),
    "signature" to signature,
    "leakTrace" to toString()
)

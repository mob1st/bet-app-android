package br.com.mob1st.bet.core.leak

import leakcanary.EventListener
import timber.log.Timber

object LeakLoggerListener : EventListener {

    override fun onEvent(event: EventListener.Event) {
        if (event is EventListener.Event.HeapAnalysisDone<*>) {
            when (event) {
                is EventListener.Event.HeapAnalysisDone.HeapAnalysisSucceeded -> {
                    event.log()
                }
                is EventListener.Event.HeapAnalysisDone.HeapAnalysisFailed -> {
                    Timber.e(event.heapAnalysis.exception)
                }
            }
        }
    }

    private fun EventListener.Event.HeapAnalysisDone.HeapAnalysisSucceeded.log() {
        val allLeakTraces = heapAnalysis.allLeaks.toList().flatMap { leak ->
            leak.leakTraces.map { leakTrace ->
                leak to leakTrace
            }
        }
        if (allLeakTraces.isEmpty()) {
            Timber.e(NoLeakException(heapAnalysis))
        } else {
            allLeakTraces.forEach { (leak, leakTrace) ->
                Timber.e(LeakException(heapAnalysis, leak, leakTrace))
            }
        }
    }
}

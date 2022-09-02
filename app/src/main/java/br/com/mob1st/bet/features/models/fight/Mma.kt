package br.com.mob1st.bet.features.models.fight

import br.com.mob1st.bet.features.models.Node
import br.com.mob1st.bet.features.models.commons.Bet

sealed interface MmaWinMethod
sealed interface BoxWinMethod
sealed interface JiuJitsuWinMethod

data class MmaScore(
    val int: Bet<Int>
)

object KnockOut : MmaWinMethod, BoxWinMethod
object Submission : MmaWinMethod, JiuJitsuWinMethod
data class Decision(
    val current: MmaScore,
    val next: Node<String>?,
) : MmaWinMethod, BoxWinMethod, JiuJitsuWinMethod
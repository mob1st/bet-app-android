package br.com.mob1st.bet.features.competitions.domain

interface BetSelector<T> {
    fun select(index: T): Bet<*>
}

/**
 * When two contenders face each other in a 1x1 confrontation, we have a Duel
 */
interface Duel<T> : BetSelector<Duel.Selection>{
    val contender1: Bet<T>
    val contender2: Bet<T>
    val draw: Bet<String>

    enum class Selection {
        CONTENDER_1,
        CONTENDER_2,
        DRAW
    }


    override fun select(index: Selection): Bet<*> {
        return when(index) {
            Selection.CONTENDER_1 -> contender1
            Selection.CONTENDER_2 -> contender2
            Selection.DRAW -> draw
        }
    }
}

/**
 * When many possibilities can happen during a confrontation,
 */
interface MultiChoice<T> : BetSelector<Int> {
    val contenders: List<Bet<T>>

    override fun select(index: Int): Bet<*> {
        return contenders[index]
    }
}
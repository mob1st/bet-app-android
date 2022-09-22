package br.com.mob1st.bet.features.competitions.domain

/**
 * When two contenders face each other in a 1x1 confrontation, we have a Duel
 */
interface Duel<T> {
    val contender1: Bet<T>
    val contender2: Bet<T>
    val draw: Bet<String>

    /**
     * The result of duel. In some cases [DRAW] is optional
     */
    enum class Selection(val pathIndex: Int) {
        CONTENDER_1(0),
        CONTENDER_2(1),
        DRAW(2)
    }
}

/**
 * When many possibilities can happen during a confrontation,
 */
interface MultiChoice<T> {
    val contenders: List<Bet<T>>
}
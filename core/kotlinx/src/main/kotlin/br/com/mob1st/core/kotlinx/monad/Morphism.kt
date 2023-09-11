package br.com.mob1st.core.kotlinx.monad

/**
 * Morphism is a function that maps elements of one set to another set.
 * @param L the left set.
 * @param R the right set.
 */
fun interface Morphism<L, R> {

    /**
     * Maps an element of the left set to an element of the right set.
     */
    operator fun get(l: L): R
}

/**
 * Isomorphism is a pair of morphisms that map elements of one set to another set in opposite directions.
 * @param L the left set.
 * @param R the right set.
 */
fun interface Isomorphism<L, R> {

    /**
     * Returns the pair of morphisms.
     */
    operator fun invoke(): Pair<Morphism<L, R>, Morphism<R, L>>

    /**
     * Maps an element of the left set to an element of the right set.
     */
    operator fun get(left: L): R = invoke().first[left]

    /**
     * Maps an element of the right set to an element of the left set.
     */
    operator fun get(right: R): L = invoke().second[right]
}

/**
 * Chain the given [Morphism] to the current one and generate a longer path of mapping.
 * ```
 * this: (A) -> B
 * other: (B) -> C
 * result: (A) -> C = this + other
 * ```
 */
operator fun <A, B, C> Morphism<A, B>.plus(other: Morphism<B, C>): Morphism<A, C> = Morphism { a ->
    other[get(a)]
}

/**
 * Combine all possibilities between two [Morphism] to generate an [Isomorphism].
 * ```
 * this: (A) -> B
 * other: (B) -> A
 * result: (A) -> B to (B) -> A = this * other
 * ```
 */
operator fun <A, B> Morphism<A, B>.times(other: Morphism<B, A>): Isomorphism<A, B> = Isomorphism {
    this to other
}

/**
 * Chain the given [Isomorphism] to the current one and generate a longer path of mapping.
 * ```
 * this: (A) -> B to (B) -> A
 * other: (B) -> C to (C) -> B
 * result: (A) -> C to (C) -> A = this + other
 * ```
 */
operator fun <A, B, C> Isomorphism<A, B>.plus(other: Isomorphism<B, C>): Isomorphism<A, C> = Isomorphism {
    val (aToB, bToA) = invoke()
    val (bToC, cToB) = other()
    aToB + bToC to cToB + bToA
}

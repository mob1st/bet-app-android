package br.com.mob1st.tests.unit

import org.jeasy.random.EasyRandom

/**
 * Returns a random enum value.
 */
inline fun <reified T : Enum<T>> randomEnum(): T {
    val values = enumValues<T>()
    return values.random()
}

/**
 * Returns a random object of type [T].
 */
inline fun <reified T> randomObj(): T {
    val easyRandom = EasyRandom()
    return easyRandom.nextObject(T::class.java)
}

fun randomBoolean() = (0..1).random() == 1

@Suppress("MagicNumber")
fun randomString(length: IntRange = 10..20, exclude: List<String> = emptyList()): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val randomStr = (1..length.random())
        .map { allowedChars.random() }
        .joinToString("")
    return if (exclude.contains(randomStr)) {
        randomString(length, exclude)
    } else {
        randomStr
    }
}

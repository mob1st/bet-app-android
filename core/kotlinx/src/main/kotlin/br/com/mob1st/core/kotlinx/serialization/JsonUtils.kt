package br.com.mob1st.core.kotlinx.serialization

import kotlinx.serialization.json.Json

/**
 * Default [Json] instance used in the app
 */
fun defaultJson() =
    Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = true
        allowSpecialFloatingPointValues = true
        useArrayPolymorphism = true
    }

package br.com.mob1st.bet.core.firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromMap

suspend inline fun <reified T> Query.all(): List<T> {
    return get()
        .await()
        .documents
        .map { documentSnapshot -> documentSnapshot.to() }
}

suspend inline fun <reified T> DocumentReference.to(): T {
    return get().await().to()
}

inline fun <reified T> DocumentSnapshot.to(): T {
    val properties = checkNotNull(data){
        "data should not be null"
    }.toMap()
    @OptIn(ExperimentalSerializationApi::class)
    return Properties.decodeFromMap(properties)
}

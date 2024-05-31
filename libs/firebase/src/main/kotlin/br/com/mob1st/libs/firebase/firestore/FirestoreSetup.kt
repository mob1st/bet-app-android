package br.com.mob1st.libs.firebase.firestore

import com.google.firebase.firestore.ktx.firestoreSettings

/**
 * Firestore settings.
 */
fun firestoreSettings() =
    firestoreSettings {
        isPersistenceEnabled = false
    }

package br.com.mob1st.core.androidx.datastore

/**
 * Enum that represents the file name of the [androidx.datastore.core.DataStore] to be used.
 * Use the [Enum.name] as the file name to avoid duplicated files.
 */
enum class DataStoreFile {
    /**
     * File name for the [androidx.datastore.core.DataStore] that persists the backend environment.
     */
    BE_ENV,

    /**
     * The current step in the builder for recurrences to present to user.
     */
    RECURRENCE_BUILDER_STEP_COMPLETION,

    /**
     * Indicates if the initial builder was completed or not
     */
    CATEGORY_BUILDER_COMPLETION,
}

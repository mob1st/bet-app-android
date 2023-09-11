package br.com.mob1st.core.kotlinx.errors

import java.io.IOException

/**
 * Exception to throw when the internet connection is not available.
 * Can be handled by a specific message on UI asking to check the internet settings.
 */
class NoConnectivityException(
    message: String,
    cause: Throwable,
) : IOException(message, cause)

/**
 * Non expected exception that has no clear cause.
 * Can be handled by a generic error message on UI.
 */
class UnknownException(
    message: String,
    cause: Throwable?,
) : Exception(message, cause)

/**
 * Thrown when a task was not finished in a predefined time.
 * Can be handled doing retries
 */
class TimeoutException(
    limitInMilliseconds: Long,
) : IOException("Timeout of $limitInMilliseconds milliseconds reached")

/**
 * Thrown when a remote server has no more capacity to handle requests or a specific quota was reached.
 */
class ServerOutOfLimitException(
    message: String,
    cause: Throwable,
) : IOException(message, cause)

/**
 * Thrown when the user is not authorized to access a specific resource.
 */
class UnauthorizedException(
    message: String,
    cause: Throwable,
) : Exception(message, cause)

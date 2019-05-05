package studio.forface.fluentnotifications.utils

/*
 * A file containing utils for Kotlin language
 *
 * Author: Davide Giuseppe Farella
 */

/**
 * Function that wraps a try/catch block for return a [default] value in case of Error
 *
 * @param default [T] default value to be returned in case of Error
 * @param catchBlock an optional lambda that will be executed in case of Error
 * @param tryBlock a lambda that will run inside the try block
 */
internal inline fun <T> handle( default: T, catchBlock: (Throwable) -> Unit = {}, tryBlock: () -> T ): T {
    return try {
        tryBlock()
    } catch (t: Throwable) {
        catchBlock( t )
        default
    }
}
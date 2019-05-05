package studio.forface.fluentnotifications.utils

import android.content.Context
import android.content.Intent

/*
 * A file containing utils for [Intent]
 *
 * Author: Davide Giuseppe Farella
 */

/** @return [Intent] created with the given [T] type and the optional [flags] */
@Suppress("FunctionName")
@PublishedApi
internal inline fun <reified T: Any> Intent( context: Context, flags: Int? = null ) = Intent( context, T::class.java )
    .apply { flags?.let { this.flags = it } }
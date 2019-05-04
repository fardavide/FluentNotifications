package studio.forface.fluentnotifications.builder

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IntegerRes
import androidx.fragment.app.FragmentActivity
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.*
import studio.forface.fluentnotifications.utils.ResourcedBuilder
import kotlin.reflect.KClass

/**
 * A Builder for create a [PendingIntent]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [NotificationBuilder].
 * E.g. [NotificationBuilder.onContentAction]
 *
 * @param context [Context] required for [ResourcedBuilder] delegation and for create the [PendingIntent]
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
class PendingIntentBuilder internal constructor(
    @PublishedApi internal val context: Context
) : ResourcedBuilder by context() {

    internal var pendingIntent : PendingIntent? by optionalOnce()

    inline fun <reified A: FragmentActivity> startActivity(
        requestCode: Int? = null,
        flags: Int = PendingIntent.FLAG_UPDATE_CURRENT,
        intentFlags: Int? = null,
        options: Bundle? = null
    ) {
        val activityKClass = A::class
        val intent = Intent( context, activityKClass.java )
        intentFlags?.let { intent.flags = it }

        startActivity( intent, requestCode, flags, options )
    }

    fun startActivity(
        intent: Intent,
        requestCode: Int? = null,
        flags: Int = PendingIntent.FLAG_UPDATE_CURRENT,
        options: Bundle? = null
    ) {
        // If requestCode is not null, try to get the Int from Resource, else use itself. If null use the hashCode of
        // the intent
        val finalRequestCode = requestCode
            ?.let { handle( requestCode ) { resources[requestCode] } }
            ?: intent.hashCode()

        pendingIntent = PendingIntent.getActivity( context, finalRequestCode, intent, flags, options )
    }
}

/** Typealias for a lambda that takes [PendingIntentBuilder] as receiver and returns [Unit] */
typealias PendingIntentBlock = PendingIntentBuilder.() -> Unit
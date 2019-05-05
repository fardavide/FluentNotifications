@file:Suppress("unused")

package studio.forface.fluentnotifications.builder

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IntegerRes
import androidx.fragment.app.FragmentActivity
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.*

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

    @PublishedApi
    internal companion object {
        /** Default [Int] flags for generated [PendingIntent]s */
        const val DEFAULT_FLAGS = PendingIntent.FLAG_UPDATE_CURRENT
    }

    /** REQUIRED [PendingIntent] */
    internal var pendingIntent : PendingIntent by requiredOnce()

    /**
     * Create a [PendingIntent] for start a Component of type [T]
     *
     * @param T generic type of the Component to start, bound types are the followings:
     * * [FragmentActivity]
     * * [Service]
     *
     * @param requestCode OPTIONAL [Int] or [IntegerRes] for the [PendingIntent]
     * Default is `null`
     *
     * @param flags OPTIONAL [Int] flags for the [PendingIntent]
     * Default is [DEFAULT_FLAGS]
     *
     * @param intentFlags OPTIONAL [Int] flags for the [Intent] created with the given [T] type
     * Default is `null`
     *
     * @param options OPTIONAL [Bundle] for the [PendingIntent]
     * NOTE: This param will be used only if [T] is subtype of [FragmentActivity], otherwise it will be ignored
     * Default is `null`
     *
     * @param isForeground OPTIONAL [Boolean] whether the [Service] must be started in foreground.
     * @see PendingIntent.getForegroundService
     * NOTE: This param will be used only if [T] is subtype of [Service], otherwise it will be ignored
     * NOTE: This will be ignored on versions prior [Android.OREO]
     * Default is `false`
     */
    inline fun <reified T : Any> start(
        requestCode: Int? = null,
        flags: Int = DEFAULT_FLAGS,
        intentFlags: Int? = null,
        options: Bundle? = null,
        isForeground: Boolean = false
    ) {
        val intent = context.createIntent<T>( intentFlags )

        when( T::class ) {
            FragmentActivity::class -> startActivity( intent, requestCode, flags, options )
            BroadcastReceiver:: class -> startBroadcastReceiver( intent, requestCode, flags )
            Service::class -> startService( intent, requestCode, flags, isForeground )

            else -> throw IllegalArgumentException(
                "Generic type '${T::class.qualifiedName}' not bound, check DOC for supported types"
            )
        }
    }

    /**
     * Create a [PendingIntent] for start a [FragmentActivity] by an [Intent]
     *
     * @param intent [Intent] for [PendingIntent]
     *
     * @param requestCode OPTIONAL [Int] or [IntegerRes] for the [PendingIntent]
     * Default is `null`
     *
     * @param flags OPTIONAL [Int] flags for the [PendingIntent]
     * Default is [DEFAULT_FLAGS]
     *
     * @param options OPTIONAL [Bundle] for the [PendingIntent]
     * Default is `null`
     */
    fun startActivity(
        intent: Intent,
        requestCode: Int? = null,
        flags: Int = DEFAULT_FLAGS,
        options: Bundle? = null
    ) {
        // If requestCode is not null, try to get the Int from Resource, else use itself. If null use the hashCode of
        // the intent
        val finalRequestCode = requestCode.resourceOrSelf( resources ) ?: intent.hashCode()
        pendingIntent = PendingIntent.getActivity( context, finalRequestCode, intent, flags, options )
    }

    /**
     * Create a [PendingIntent] for start more that one [FragmentActivity] by a set of [Intent]s
     *
     * @param intents vararg of [Intent] for [PendingIntent]
     *
     * @param requestCode OPTIONAL [Int] or [IntegerRes] for the [PendingIntent]
     * Default is `null`
     *
     * @param flags OPTIONAL [Int] flags for the [PendingIntent]
     * Default is [DEFAULT_FLAGS]
     *
     * @param options OPTIONAL [Bundle] for the [PendingIntent]
     * Default is `null`
     */
    fun startActivities(
        vararg intents: Intent,
        requestCode: Int? = null,
        flags: Int = DEFAULT_FLAGS,
        options: Bundle? = null
    ) {
        // If requestCode is not null, try to get the Int from Resource, else use itself. If null use the hashCode of
        // the intents
        val finalRequestCode = requestCode.resourceOrSelf( resources ) ?: intents.hashCode()
        pendingIntent = PendingIntent.getActivities( context, finalRequestCode, intents, flags, options )
    }

    /**
     * Create a [PendingIntent] for start a [BroadcastReceiver] by an [Intent]
     *
     * @param intent [Intent] for [PendingIntent]
     *
     * @param requestCode OPTIONAL [Int] or [IntegerRes] for the [PendingIntent]
     * Default is `null`
     *
     * @param flags OPTIONAL [Int] flags for the [PendingIntent]
     * Default is [DEFAULT_FLAGS]
     */
    fun startBroadcastReceiver(
        intent: Intent,
        requestCode: Int? = null,
        flags: Int = DEFAULT_FLAGS
    ) {
        // If requestCode is not null, try to get the Int from Resource, else use itself. If null use the hashCode of
        // the intent
        val finalRequestCode = requestCode.resourceOrSelf( resources ) ?: intent.hashCode()
        pendingIntent = PendingIntent.getBroadcast( context, finalRequestCode, intent, flags )
    }

    /**
     * Create a [PendingIntent] for start a [Service] by an [Intent]
     *
     * @param intent [Intent] for [PendingIntent]
     *
     * @param requestCode OPTIONAL [Int] or [IntegerRes] for the [PendingIntent]
     * Default is `null`
     *
     * @param flags OPTIONAL [Int] flags for the [PendingIntent]
     * Default is [DEFAULT_FLAGS]
     *
     * @param isForeground OPTIONAL [Boolean] whether the [Service] must be started in foreground.
     * @see PendingIntent.getForegroundService
     * NOTE: This will be ignored on versions prior [Android.OREO]
     * Default is `false`
     */
    fun startService(
        intent: Intent,
        requestCode: Int? = null,
        flags: Int = DEFAULT_FLAGS,
        isForeground: Boolean = false
    ) {
        // If requestCode is not null, try to get the Int from Resource, else use itself. If null use the hashCode of
        // the intent
        val finalRequestCode = requestCode.resourceOrSelf( resources ) ?: intent.hashCode()

        pendingIntent = if ( isForeground && Android.OREO )
            PendingIntent.getForegroundService( context, finalRequestCode, intent, flags )
        else
            PendingIntent.getService( context, finalRequestCode, intent, flags )
    }
}

/** Typealias for a lambda that takes [PendingIntentBuilder] as receiver and returns [Unit] */
typealias PendingIntentBlock = PendingIntentBuilder.() -> Unit
@file:Suppress("unused")

package studio.forface.fluentnotifications.builder

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.*
import kotlin.reflect.full.isSubclassOf
import studio.forface.fluentnotifications.utils.Intent as newIntent

/**
 * A Builder for create a [PendingIntent]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [NotificationBuilder].
 * E.g. [NotificationBuilder.onContentAction]
 *
 * @param coreParams [CoreParams]
 *
 * @param autoCancel [Boolean] whether the Notification need to be cancel on Action
 * Default is `false`
 *
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
class PendingIntentBuilder @PublishedApi /* Required for inline */ internal constructor(
    private val coreParams: CoreParams, private val autoCancel: Boolean = false,
    private val buildActivityPendingIntent: ActivityPendingIntentCreator = PendingIntent::getActivity,
    private val buildActivitiesPendingIntent: ActivitiesPendingIntentCreator = PendingIntent::getActivities,
    private val buildBroadcastReceiverPendingIntent: BroadcastReceiverPendingIntentCreator = PendingIntent::getBroadcast,
    private val buildServicePendingIntent: ServicePendingIntentCreator = PendingIntent::getService,
    private val buildForegroundServicePendingIntent: ForegroundServicePendingIntentCreator =
            if ( Android.OREO ) PendingIntent::getForegroundService else PendingIntent::getService
) : ResourcedBuilder by coreParams.context() {

    @PublishedApi // Required for inline
    internal val context get()= coreParams.context

    @PublishedApi
    internal companion object {
        /** Default [Int] flags for generated [PendingIntent]s */
        const val DEFAULT_FLAGS = PendingIntent.FLAG_UPDATE_CURRENT
    }

    /** REQUIRED [PendingIntent] */
    @PublishedApi // Required for inline
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

        when {
            T::class.isSubclassOf( FragmentActivity::class ) -> startActivity( intent, requestCode, flags, options )
            T::class.isSubclassOf( BroadcastReceiver:: class ) -> startBroadcastReceiver( intent, requestCode, flags )
            T::class.isSubclassOf( Service::class ) -> startService( intent, requestCode, flags, isForeground )

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
        val basePendingIntent = buildActivityPendingIntent( context, finalRequestCode, intent, flags, options )

        // If autoCancel is true, wrap the basePendingIntent into another PendingIntent that will cancel the Notification
        pendingIntent = if ( autoCancel ) {
            with( coreParams ) {
                basePendingIntent.wrapInAutoCancel( context, notificationId, notificationTag, finalRequestCode, flags )
            }
        } else basePendingIntent
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
        val basePendingIntent = buildActivitiesPendingIntent( context, finalRequestCode, intents, flags, options )

        // If autoCancel is true, wrap the basePendingIntent into another PendingIntent that will cancel the Notification
        pendingIntent = if ( autoCancel ) {
            with( coreParams ) {
                basePendingIntent.wrapInAutoCancel( context, notificationId, notificationTag, finalRequestCode, flags )
            }
        } else basePendingIntent
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
        val basePendingIntent = buildBroadcastReceiverPendingIntent( context, finalRequestCode, intent, flags )

        // If autoCancel is true, wrap the basePendingIntent into another PendingIntent that will cancel the Notification
        pendingIntent = if ( autoCancel ) {
            with( coreParams ) {
                basePendingIntent.wrapInAutoCancel( context, notificationId, notificationTag, finalRequestCode, flags )
            }
        } else basePendingIntent
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

        val basePendingIntent = if ( isForeground )
            buildForegroundServicePendingIntent( context, finalRequestCode, intent, flags )
        else
            buildServicePendingIntent( context, finalRequestCode, intent, flags )

        // If autoCancel is true, wrap the basePendingIntent into another PendingIntent that will cancel the Notification
        pendingIntent = if ( autoCancel ) {
            with( coreParams ) {
                basePendingIntent.wrapInAutoCancel( context, notificationId, notificationTag, finalRequestCode, flags )
            }
        } else basePendingIntent
    }
}

/** Typealias for a lambda that takes [PendingIntentBuilder] as receiver and returns [Unit] */
typealias PendingIntentBlock = PendingIntentBuilder.() -> Unit

// region Creators

/** Typealias for a lambda that creates a [PendingIntent] for [FragmentActivity] */
internal typealias ActivityPendingIntentCreator =
            ( context: Context, requestCode: Int, intent: Intent, flags: Int, options: Bundle? ) -> PendingIntent

/** Typealias for a lambda that creates a [PendingIntent] for a set of [FragmentActivity]s */
internal typealias ActivitiesPendingIntentCreator =
            ( context: Context, requestCode: Int, intents: Array<out Intent>, flags: Int, options: Bundle? ) -> PendingIntent

/** Typealias for a lambda that creates a [PendingIntent] for [BroadcastReceiver] */
internal typealias BroadcastReceiverPendingIntentCreator =
            ( context: Context, requestCode: Int, intent: Intent, flags: Int ) -> PendingIntent

/** Typealias for a lambda that creates a [PendingIntent] for [Service] */
internal typealias ServicePendingIntentCreator =
            ( context: Context, requestCode: Int, intent: Intent, flags: Int ) -> PendingIntent

/** Typealias for a lambda that creates a [PendingIntent] for foreground [Service] */
internal typealias ForegroundServicePendingIntentCreator =
            ( context: Context, requestCode: Int, intent: Intent, flags: Int ) -> PendingIntent
// endregion

/**
 * @return [PendingIntent] that will start [AutoCancelActivity] for cancel the notification and launch the receiver
 * [PendingIntent]
 *
 * @see AutoCancelActivity.wrapPendingIntent
 */
internal fun PendingIntent.wrapInAutoCancel(
    context: Context,
    notificationId: Int,
    notificationTag: String?,
    requestCode: Int,
    flags: Int
) = AutoCancelActivity
    .wrapPendingIntent( context, notificationId, notificationTag, this, requestCode, flags )

/** An `Activity` that will cancel the current Notification and then start the original [PendingIntent] */
internal class AutoCancelActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_PENDING_INTENT = "pending_intent"
        private const val EXTRA_NOTIFICATION_ID = "notification_id"
        private const val EXTRA_NOTIFICATION_TAG = "notification_tag"

        /**
         * @return [PendingIntent] that will start [AutoCancelActivity] for cancel the notification and launch the
         * [originalPendingIntent]
         */
        fun wrapPendingIntent(
            context: Context,
            notificationId: Int,
            notificationTag: String?,
            originalPendingIntent: PendingIntent,
            requestCode: Int,
            flags: Int
        ) : PendingIntent {
            val intent = context.createIntent<AutoCancelActivity>( Intent.FLAG_ACTIVITY_NO_HISTORY )
                .putExtra( EXTRA_NOTIFICATION_ID, notificationId )
                .putExtra( EXTRA_NOTIFICATION_TAG, notificationTag)
                .putExtra( EXTRA_PENDING_INTENT, originalPendingIntent )
            return PendingIntent.getActivity( context, requestCode, intent, flags )
        }
    }

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        // Re-launch the original PendingIntent
        intent.getParcelableExtra<PendingIntent>( EXTRA_PENDING_INTENT ).send()
        // Cancel the notification
        with( notificationManager ) {
            cancel(
                intent.getStringExtra( EXTRA_NOTIFICATION_TAG ),
                intent.getIntExtra( EXTRA_NOTIFICATION_ID, 0 )
            )
        }

        finish()
    }
}
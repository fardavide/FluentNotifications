package studio.forface.fluentnotifications

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import studio.forface.fluentnotifications.enum.NotificationImportance
import studio.forface.fluentnotifications.utils.Android

/**
 * A Core Builder for create a Notification with its companions ( channel or other optionals )
 * @constructor is internal. Use [Context.showNotification]
 * @param context [Context] required for create [Notification] and for retrieve [String]s from `Resources`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
class NotificationCoreBuilder internal constructor( context: Context ) {

    /**
     * REQUIRED [NotificationImportance] importance for the [NotificationChannel] on [Android.OREO] or priority on for
     * [NotificationCompat] on pre-Oreo
     *
     * Default is [NotificationImportance.DEFAULT]
     */
    var importance: NotificationImportance = NotificationImportance.DEFAULT


    /* = = = = = NOTIFICATION = = = = = */

    /** [NotificationBuilder] for create a [Notification] */
    @PublishedApi // Needed for inline
    internal val notificationBuilder = NotificationBuilder( context ) {
        notificationParams(
            channelId =     channelBuilder.id,
            importance =    importance
        )
    }

    /** REQUIRED block for set params for [Notification] within a [NotificationBuilder] receiver */
    @Suppress("MemberVisibilityCanBePrivate")
    inline fun notification( block: NotificationBuilder.() -> Unit ) {
        notificationBuilder.apply( block )
    }

    /** @return [Notification] created with params set in [notification]s block */
    internal fun buildNotification() = notificationBuilder.build()


    /* = = = = = CHANNEL = = = = = */

    /** [ChannelBuilder] for create a [NotificationChannel] */
    @PublishedApi // Needed for inline
    internal val channelBuilder = ChannelBuilder( context ) {
        channelParams(
            importance =    importance
        )
    }

    /** REQUIRED block for set params for [NotificationChannel] within a [ChannelBuilder] receiver */
    @Suppress("MemberVisibilityCanBePrivate")
    inline fun channel( block: ChannelBuilder.() -> Unit ) {
        channelBuilder.apply( block )
    }

    /** @return [NotificationChannel] created with params set in [channel]s block */
    @RequiresApi(Build.VERSION_CODES.O)
    internal fun buildChannel() = channelBuilder.build()
}
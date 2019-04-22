@file:Suppress("unused")

package studio.forface.fluentnotifications

import android.content.Context
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import studio.forface.fluentnotifications.builder.*
import studio.forface.fluentnotifications.enum.NotificationImportance
import studio.forface.fluentnotifications.utils.Android
import studio.forface.fluentnotifications.utils.notificationManager

/**
 * Show a Notification created with Dsl from a [Context]
 * @param idRes a REQUIRED [IntegerRes] id for create the Notification
 * @param tagRes an OPTIONAL [StringRes] tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
fun Context.showNotification(
    @IntegerRes idRes: Int,
    @StringRes tagRes: Int? = null,
    block: NotificationCoreBuilder.() -> Unit
) {
    showNotification( resources.getInteger( idRes ), tagRes?.let( ::getString ), block  )
}

/**
 * Show a Notification created with Dsl from a [Context]
 * @param id a REQUIRED id for create the Notification
 * @param tag an OPTIONAL tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
fun Context.showNotification(
    id: Int,
    tag: String? = null,
    block: NotificationCoreBuilder.() -> Unit
) {
    val builder = NotificationCoreBuilder( this ).apply( block )

    with( notificationManager ) {
        if ( Android.OREO ) createNotificationChannel( builder.buildChannel() )
        notify( tag, id, builder.buildNotification() )
    }
}

/**
 * Cancel a Notification form a [Context]
 * @param idRes REQUIRED [IntegerRes] id for find the Notification to cancel
 * @param tagRes OPTIONAL [StringRes] tag for find the Notification. If `null` if will be ignored
 * Default is `null`
 */
fun Context.cancelNotification( @IntegerRes idRes: Int, @StringRes tagRes: Int? = null ) {
    cancelNotification( resources.getInteger( idRes ), tagRes?.let( ::getString ) )
}

/**
 * Cancel a Notification form a [Context]
 * @param id REQUIRED id for find the Notification to cancel
 * @param tag OPTIONAL tag for find the Notification. If `null` if will be ignored
 * Default is `null`
 */
fun Context.cancelNotification( id: Int, tag: String? = null ) {
    notificationManager.cancel( tag, id )
}


/** API test purpose only. TODO remove */
private fun Context.test() {
    showNotification( 123, "someTag" ) {

        behaviour {
            importance = NotificationImportance.HIGH
            this + defaultVibration
        }

        channel( "channelId", "channelName" ) {
            description = "No description"
        }

        notification {
            smallIconRes = 0
            title = "Title"
            contentText = "Content"
        }
    }
}
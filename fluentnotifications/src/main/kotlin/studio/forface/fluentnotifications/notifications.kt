@file:Suppress("unused")

package studio.forface.fluentnotifications

import android.content.Context
import studio.forface.fluentnotifications.enum.NotificationImportance
import studio.forface.fluentnotifications.utils.Android
import studio.forface.fluentnotifications.utils.notificationManager

/**
 * Show a Notification created with Dsl from a [Context]
 * @param id a required id for create the Notification
 * @param tag an OPTIONAL tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
fun Context.showNotification( id: Int, tag: String? = null, block: NotificationCoreBuilder.() -> Unit ) {
    val builder = NotificationCoreBuilder( this ).apply( block )

    with( notificationManager ) {
        if ( Android.OREO ) createNotificationChannel( builder.buildChannel() )
        notify( tag, id, builder.buildNotification() )
    }
}

/**
 * Cancel a Notification form a [Context]
 * @param id required id for find the Notification to cancel
 * @param tag OPTIONAL tag for find the Notification. If `null` if will be ignored
 * Default is `null`
 */
fun Context.cancelNotification( id: Int, tag: String? = null ) {
    notificationManager.cancel( tag, id )
}


/** API test purpose only. TODO remove */
fun Context.test() {
    showNotification( 123, "someTag" ) {

        importance = NotificationImportance.HIGH
        channel( "channelId", "channelName" )

        notification {
            smallIconRes = 0
            title = "Title"
            contentText = "Content"
        }
    }
}
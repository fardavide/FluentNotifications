@file:Suppress("unused")

package studio.forface.fluentnotifications

import android.app.Service
import android.content.Context
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import studio.forface.fluentnotifications.builder.CoreParams
import studio.forface.fluentnotifications.builder.NotificationCoreBlock
import studio.forface.fluentnotifications.builder.NotificationCoreBuilder
import studio.forface.fluentnotifications.utils.Android
import studio.forface.fluentnotifications.utils.notificationManager

/**
 * A reference to the [Context.getPackageName] of the calling app.
 * This will be needed for resolve some element from resource.
 */
internal var appPackageName = String()

/**
 * Show a Notification created with Dsl from a [Context]
 * @param idRes REQUIRED [IntegerRes] id for create the Notification
 * @param tagRes OPTIONAL [StringRes] tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
fun Context.showNotification(
    @IntegerRes idRes: Int,
    @StringRes tagRes: Int? = null,
    block: NotificationCoreBlock
) {
    showNotification( resources.getInteger( idRes ), tagRes?.let( ::getString ), block  )
}

/**
 * Show a Notification created with Dsl from a [Context]
 * @param id REQUIRED [Int] id for create the Notification
 * @param tag OPTIONAL [CharSequence] tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
fun Context.showNotification(
    id: Int,
    tag: CharSequence? = null,
    block: NotificationCoreBlock
) {
    appPackageName = packageName
    val coreParams = CoreParams( this, id, tag.toString() )

    with( notificationManager ) {
        with( NotificationCoreBuilder( coreParams ).apply( block ) ) {

            // Create Channel
            if ( Android.OREO ) createNotificationChannel( buildChannel() )

            // Show Group, if any
            buildNotificationGroup()?.let { notificationGroup ->
                notify( notificationGroupTag.toString(), notificationGroupId, notificationGroup )
            }

            // Show Notification
            notify( tag.toString(), id, buildNotification() )
        }
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

/**
 * Start the receiver [Service] in foreground with the Notification created from [NotificationCoreBlock]
 * @see Service.startForeground
 *
 * @param id REQUIRED [Int] id for create the Notification
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
fun Service.startForeground(
    id: Int,
    block: NotificationCoreBlock
) {
    appPackageName = packageName
    val coreParams = CoreParams( this, id )

    with( notificationManager ) {
        with( NotificationCoreBuilder( coreParams ).apply( block ) ) {

            // Create Channel
            if ( Android.OREO ) createNotificationChannel( buildChannel() )

            // Show Group, if any
            buildNotificationGroup()?.let { notificationGroup ->
                notify( notificationGroupTag.toString(), notificationGroupId, notificationGroup )
            }

            // Start the service in foreground
            startForeground( id, buildNotification() )
        }
    }
}
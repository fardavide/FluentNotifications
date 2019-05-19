package studio.forface.fluentnotifications.utils

import android.app.NotificationChannel
import androidx.core.app.NotificationCompat
import studio.forface.fluentnotifications.enum.DefaultBehaviour

/*
 * Utilities for `DefaultBehaviour`
 * Author: Davide Giuseppe Farella
 */

/** Apply a [Set] of [DefaultBehaviour] to the receiver [NotificationChannel] */
internal fun NotificationChannel.setDefaults( defaults: Set<DefaultBehaviour> ) {
    defaults.forEach { it.channelSetter( this ) }
}

/** Apply a [Set] of [DefaultBehaviour] to the receiver [NotificationCompat.Builder] */
internal fun NotificationCompat.Builder.setDefaults( defaults: Set<DefaultBehaviour> ) {
    if ( defaults.isEmpty() ) return
    setDefaults( defaults.map { it.notificationPlatform }.reduce { acc, i -> acc or i } )
}
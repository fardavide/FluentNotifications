package studio.forface.fluentnotifications.enum

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * A set of default values for `Behaviour` for Notifications / Channels
 *
 * @param notificationPlatform ( Notification pre-Oreo ) [Int] flag to add to the `Notification` via
 * NotificationCompat.Builder.setDefault`
 *
 *
 * @author Davide Giuseppe Farella
 */
enum class DefaultBehaviour( internal val notificationPlatform: Int, internal val channelSetter: ChannelSetter ) {

    /**
     * @see NotificationCompat.Builder.setDefaults
     * @see NotificationChannel.setVibrationPattern
     */
    VIBRATION(
        NotificationCompat.DEFAULT_VIBRATE,
        @TargetApi(Build.VERSION_CODES.O) { it.vibrationPattern = DEFAULT_VIBRATION_PATTERN }
    )
}

/** Typealias for a lambda that takes [NotificationChannel] as argument ang returns [Unit] */
private typealias ChannelSetter = (NotificationChannel) -> Unit

/** [LongArray] of the default pattern for vibration */
private val DEFAULT_VIBRATION_PATTERN = longArrayOf( 100, 200, 300, 400, 500, 400, 300, 200, 400 )
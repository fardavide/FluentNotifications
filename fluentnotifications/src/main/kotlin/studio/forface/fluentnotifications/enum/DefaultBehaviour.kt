package studio.forface.fluentnotifications.enum

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.os.Build
import androidx.core.app.NotificationCompat
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.annotation.RequiresApi

/**
 * A set of default values for `Behaviour` for Notifications / Channels
 *
 * @param notificationPlatform ( Notification pre-Oreo ) [Int] flag to add to the `Notification` via
 * NotificationCompat.Builder.setDefault`
 *
 * @param channelSetter ( Notification on Android Ore ) lambda [ChannelSetter] that will se the parameters on the
 * appropriate [NotificationChannel]
 *
 *
 * @author Davide Giuseppe Farella
 */
enum class DefaultBehaviour( internal val notificationPlatform: Int, internal val channelSetter: ChannelSetter ) {

    /**
     * @see NotificationCompat.Builder.setDefaults
     * @see NotificationChannel.setSound
     */
    SOUND(
        NotificationCompat.DEFAULT_SOUND,
        @TargetApi(Build.VERSION_CODES.O) {
            val audioAttributes = DEFAULT_SOUND_ATTRIBUTES
            it.setSound( RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION ), audioAttributes )
        }
    ),

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

internal val DEFAULT_SOUND_ATTRIBUTES @RequiresApi(Build.VERSION_CODES.LOLLIPOP) get() = AudioAttributes.Builder()
    .setContentType( AudioAttributes.CONTENT_TYPE_SONIFICATION )
    .setUsage( AudioAttributes.USAGE_NOTIFICATION_RINGTONE )
    .build()

/** [LongArray] of the default pattern for vibration */
private val DEFAULT_VIBRATION_PATTERN = longArrayOf( 100, 200, 300, 400, 500, 400, 300, 200, 400 )
package studio.forface.fluentnotifications.enum

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.*
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.*
import studio.forface.fluentnotifications.utils.Android

/**
 * The importance of the Notification.
 *
 * @param importancePlatform ( Channel on [Android.OREO] ) this controls how interruptive notifications posted to this
 * channel are
 *
 * @param priorityPlatform ( Notification pre-Oreo ) set the relative priority for this notification.
 * Priority is an indication of how much of the user's valuable attention should be consumed by this notification.
 * Low-priority notifications may be hidden from the user in certain situations, while the user might be interrupted for
 * a higher-priority notification.
 * The system sets a notification's priority based on various factors including the setPriority value. The effect may
 * differ slightly on different platforms.
 *
 *
 * @author Davide Giuseppe Farella
 */
enum class NotificationImportance( internal val importancePlatform: Int, internal val priorityPlatform: Int ) {

    /**
     * @see NotificationManagerCompat.IMPORTANCE_NONE
     * @see NotificationCompat.PRIORITY_MIN
     */
    NONE( importancePlatform = IMPORTANCE_NONE, priorityPlatform = PRIORITY_MIN ),

    /**
     * @see NotificationManagerCompat.IMPORTANCE_MIN
     * @see NotificationCompat.PRIORITY_MIN
     */
    MIN( importancePlatform = IMPORTANCE_MIN, priorityPlatform = PRIORITY_MIN ),

    /**
     * @see NotificationManagerCompat.IMPORTANCE_LOW
     * @see NotificationCompat.PRIORITY_LOW
     */
    LOW( importancePlatform = IMPORTANCE_LOW, priorityPlatform = PRIORITY_LOW ),

    /**
     * @see NotificationManagerCompat.IMPORTANCE_NONE
     * @see NotificationCompat.PRIORITY_MIN
     */
    DEFAULT( importancePlatform = IMPORTANCE_DEFAULT, priorityPlatform = PRIORITY_DEFAULT ),

    /**
     * @see NotificationManagerCompat.IMPORTANCE_NONE
     * @see NotificationCompat.PRIORITY_MIN
     */
    HIGH( importancePlatform = IMPORTANCE_HIGH, priorityPlatform = PRIORITY_HIGH ),

    /**
     * @see NotificationManagerCompat.IMPORTANCE_NONE
     * @see NotificationCompat.PRIORITY_MIN
     */
    MAX( importancePlatform = IMPORTANCE_MAX, priorityPlatform = PRIORITY_MAX );
}
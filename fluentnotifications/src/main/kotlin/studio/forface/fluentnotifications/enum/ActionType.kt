package studio.forface.fluentnotifications.enum

import android.app.PendingIntent

/**
 * A set of types for Notification's Action's [PendingIntent]
 *
 * @author Davide Giuseppe Farella
 */
enum class ActionType {

    /**
     * The Action will start more that one `Activity`s
     * @see PendingIntent.getActivities
     */
    ACTIVITIES,

    /**
     * The Action will start a single `Activity`
     * @see PendingIntent.getActivity
     */
    ACTIVITY,

    /**
     * The Action will trigger a `BroadcastReceiver`
     * @see PendingIntent.getBroadcast
     */
    BROADCAST_RECEIVER,

    /**
     * The Action will start a `ForegroundService`
     * @see PendingIntent.getForegroundService
     */
    FOREGROUND_SERVICE,

    /**
     * The Action will start a `Service`
     * @see PendingIntent.getService
     */
    SERVICE
}
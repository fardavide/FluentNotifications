package studio.forface.fluentnotifications.enum

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.*

/**
 * A set of values of Behaviour for Notification Group
 * @param platform [Int] value that represent the [GroupAlertBehavior] of the Android platform
 *
 * @author Davide Giuseppe Farella
 */
enum class GroupBehaviour( @GroupAlertBehavior internal val platform: Int ) {

    /**
     * Alert for all the Notifications: Group and Children
     * @see NotificationCompat.GROUP_ALERT_ALL
     */
    ALERT_ALL( GROUP_ALERT_ALL ),

    /**
     * Alert only for the Group Notification
     * @see NotificationCompat.GROUP_ALERT_SUMMARY
     */
    ALERT_GROUP( GROUP_ALERT_SUMMARY ),

    /**
     * Alert only for the Children of the Group
     * @see NotificationCompat.GROUP_ALERT_CHILDREN
     */
    ALERT_CHILDREN( GROUP_ALERT_CHILDREN )
}
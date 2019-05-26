package studio.forface.fluentnotifications.enum

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.*

/**
 * A set of values of Category for Notification
 * @param platform [String] value that represent the value in the Android platform
 *
 * @author Davide Giuseppe Farella
 */
enum class NotificationCategory( internal val platform: String ) {


    /**
     * Category for incoming call (voice or video) or similar synchronous communication request.
     * @see NotificationCompat.CATEGORY_CALL
     */
    CALL( CATEGORY_CALL ),

    /**
     * Category for map turn-by-turn navigation.
     * @see NotificationCompat.CATEGORY_NAVIGATION
     */
    NAVIGATION( CATEGORY_NAVIGATION ),

    /**
     * Category for incoming direct message (SMS, instant message, etc.).
     * @see NotificationCompat.CATEGORY_MESSAGE
     */
    MESSAGE( CATEGORY_MESSAGE ),

    /**
     * Category for asynchronous bulk message (email).
     * @see NotificationCompat.CATEGORY_EMAIL
     */
    EMAIL( CATEGORY_EMAIL ),

    /**
     * Category for calendar event.
     * @see NotificationCompat.CATEGORY_EVENT
     */
    EVENT( CATEGORY_EVENT ),

    /**
     * Category for promotion or advertisement.
     * @see NotificationCompat.CATEGORY_PROMO
     */
    PROMO( CATEGORY_PROMO ),

    /**
     * Category for alarm or timer.
     * @see NotificationCompat.CATEGORY_ALARM
     */
    ALARM( CATEGORY_ALARM ),

    /**
     * Category for progress of a long-running background operation.
     * @see NotificationCompat.CATEGORY_PROGRESS
     */
    PROGRESS( CATEGORY_PROGRESS ),

    /**
     * Category for social network or sharing update.
     * @see NotificationCompat.CATEGORY_SOCIAL
     */
    SOCIAL( CATEGORY_SOCIAL ),

    /**
     * Category for error in background operation or authentication status.
     * @see NotificationCompat.CATEGORY_ERROR
     */
    ERROR( CATEGORY_ERROR ),

    /**
     * Category for media transport control for playback.
     * @see NotificationCompat.CATEGORY_TRANSPORT
     */
    TRANSPORT( CATEGORY_TRANSPORT ),

    /**
     * Category for indication of running background service.
     * @see NotificationCompat.CATEGORY_SERVICE
     */
    SERVICE( CATEGORY_SERVICE ),

    /**
     * Category for user-scheduled reminder.
     * @see NotificationCompat.CATEGORY_REMINDER
     */
    REMINDER( CATEGORY_REMINDER ),

    /**
     * Category for a specific, timely recommendation for a single thing.
     * @see NotificationCompat.CATEGORY_RECOMMENDATION
     */
    RECOMMENDATION( CATEGORY_RECOMMENDATION ),

    /**
     * Category for ongoing information about device or contextual status.
     * @see NotificationCompat.CATEGORY_STATUS
     */
    STATUS( CATEGORY_STATUS )
}
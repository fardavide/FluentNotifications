package studio.forface.fluentnotifications

import android.app.Service
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import studio.forface.fluentnotifications.builder.NotificationCoreBlock
import studio.forface.fluentnotifications.enum.NotificationCategory.SERVICE

/**
 * Start the receiver [Service] in foreground with the Notification created from [NotificationCoreBlock]
 * @see Service.startForeground
 * The notification is initialized with Category [SERVICE]
 *
 * @param idRes REQUIRED [IntegerRes] id for create the Notification
 * @param tagRes OPTIONAL [StringRes] tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
fun Service.startForeground(
    @IntegerRes idRes: Int,
    @StringRes tagRes: Int? = null,
    block: NotificationCoreBlock
) {
    startForeground(resources.getInteger(idRes), tagRes?.let(::getString), block)
}

/**
 * Start the receiver [Service] in foreground with the Notification created from [NotificationCoreBlock]
 * @see Service.startForeground
 * The notification is initialized with Category [SERVICE]
 *
 * @param id REQUIRED [Int] id for create the Notification
 * @param tag OPTIONAL [CharSequence] tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
fun Service.startForeground(
    id: Int,
    tag: CharSequence? = null,
    block: NotificationCoreBlock
) {
    startForeground(id, createNotification(id, tag, block))
}

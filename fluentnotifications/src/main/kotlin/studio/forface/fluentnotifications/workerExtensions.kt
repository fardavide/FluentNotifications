package studio.forface.fluentnotifications

import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.ListenableWorker
import studio.forface.fluentnotifications.builder.NotificationCoreBlock
import studio.forface.fluentnotifications.enum.NotificationCategory.SERVICE

/**
 * Start the receiver [ListenableWorker] in foreground with the Notification created from [NotificationCoreBlock]
 * @see ListenableWorker.setForegroundAsync
 * The notification is initialized with Category [SERVICE]
 *
 * @param idRes REQUIRED [IntegerRes] id for create the Notification
 * @param tagRes OPTIONAL [StringRes] tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
fun ListenableWorker.setForegroundAsync(
    @IntegerRes idRes: Int,
    @StringRes tagRes: Int? = null,
    block: NotificationCoreBlock
) {
    setForegroundAsync(applicationContext.resources.getInteger(idRes), tagRes?.let(applicationContext::getString), block)
}

/**
 * Start the receiver [ListenableWorker] in foreground with the Notification created from [NotificationCoreBlock]
 * @see ListenableWorker.setForegroundAsync
 * The notification is initialized with Category [SERVICE]
 *
 * @param id REQUIRED [Int] id for create the Notification
 * @param tag OPTIONAL [CharSequence] tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
fun ListenableWorker.setForegroundAsync(
    id: Int,
    tag: CharSequence? = null,
    block: NotificationCoreBlock
) {
    setForegroundAsync(ForegroundInfo(id, applicationContext.createNotification(id, tag, block)))
}


/**
 * Start the receiver [CoroutineWorker] in foreground with the Notification created from [NotificationCoreBlock]
 * @see CoroutineWorker.setForeground
 * The notification is initialized with Category [SERVICE]
 *
 * @param idRes REQUIRED [IntegerRes] id for create the Notification
 * @param tagRes OPTIONAL [StringRes] tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
suspend fun CoroutineWorker.setForeground(
    @IntegerRes idRes: Int,
    @StringRes tagRes: Int? = null,
    block: NotificationCoreBlock
) {
    setForeground(applicationContext.resources.getInteger(idRes), tagRes?.let(applicationContext::getString), block)
}

/**
 * Start the receiver [CoroutineWorker] in foreground with the Notification created from [NotificationCoreBlock]
 * @see CoroutineWorker.setForeground
 * The notification is initialized with Category [SERVICE]
 *
 * @param id REQUIRED [Int] id for create the Notification
 * @param tag OPTIONAL [CharSequence] tag for create the Notification. If `null` if will be ignored
 * Default is `null`
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
suspend fun CoroutineWorker.setForeground(
    id: Int,
    tag: CharSequence? = null,
    block: NotificationCoreBlock
) {
    setForeground(ForegroundInfo(id, applicationContext.createNotification(id, tag, block)))
}

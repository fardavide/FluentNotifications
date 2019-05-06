@file:Suppress("MemberVisibilityCanBePrivate")

package studio.forface.fluentnotifications.builder

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.*

/**
 * A Builder for create a [Notification]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [NotificationCoreBuilder] via `Context.showNotification`
 * @param context [Context] required for [ResourcedBuilder] delegation
 * @param getParams lambda that returns [NotificationParams]
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
class NotificationBuilder internal constructor(
    private val context: Context,
    private val getParams: () -> NotificationParams
) : ResourcedBuilder by context() {

    /**
     * OPTIONAL [CharSequence] content text for the Notification
     * Backed by [contentTextRes]
     *
     * @see NotificationCompat.Builder.setContentText
     */
    var contentText: CharSequence? by optional { contentTextRes }

    /**
     * OPTIONAL [StringRes] content text for the Notification
     * Backing value of [contentText]
     *
     * @see NotificationCompat.Builder.setContentText
     */
    @StringRes var contentTextRes: Int? = null

    /**
     * REQUIRED [DrawableRes] of the small icon for the Notification
     * @see NotificationCompat.Builder.setSmallIcon
     */
    @get:DrawableRes var smallIconRes: Int by required()

    /**
     * REQUIRED [CharSequence] content title for the Notification
     * Backed by [titleRes]
     *
     * @see NotificationCompat.Builder.setContentTitle
     */
    var title: CharSequence by required { titleRes }

    /**
     * REQUIRED [StringRes] content title for the Notification
     * Backing value of [title]
     *
     * @see NotificationCompat.Builder.setContentTitle
     */
    @StringRes var titleRes: Int? = null

    /**
     * OPTIONAL block [PendingIntentBlock] for add an action on Content
     * @see NotificationCompat.Builder.setContentIntent
     *
     * @param autoCancel [Boolean] whether the Notification need to be cancel on Content Action
     * Default is `true`
     * @see NotificationCompat.Builder.setAutoCancel
     */
    fun onContentAction( autoCancel: Boolean = true, block: PendingIntentBlock ) {
        contentIntent = PendingIntentBuilder( context ).apply( block ).pendingIntent
        autoCancelOnContentAction = autoCancel
    }

    /**
     * OPTIONAL [PendingIntent] or add an action on Content
     * @see NotificationCompat.Builder.setContentIntent
     *
     * @param autoCancel [Boolean] whether the Notification need to be cancel on Content Action
     * Default is `true`
     * @see NotificationCompat.Builder.setAutoCancel
     */
    fun onContentAction( pendingIntent: PendingIntent, autoCancel: Boolean = true ) {
        contentIntent = pendingIntent
        autoCancelOnContentAction = autoCancel
    }

    /** A [NotificationCompat.Builder] for create a [Notification] */
    @Suppress( "DEPRECATION" ) /* NotificationCompat.Builder constructor without a Channel's id is deprecated. But we
    will add the id later */
    private val builder = NotificationCompat.Builder( context )

    /** An OPTIONAL [PendingIntent] for [NotificationCompat.Builder.setContentIntent] */
    private var contentIntent by optional<PendingIntent?>()

    /** Whether the Notification need to be cancel on Content Action ( [NotificationCompat.Builder.setAutoCancel] ) */
    private var autoCancelOnContentAction = false

    /** @return [Notification] with the params previously set */
    internal fun build(): Notification = with( getParams() ) {
        return builder.run {
            setChannelId( channelId )

            /* Behaviour */
            priority = behaviour.importance.priorityPlatform
            setLights( behaviour.lightColor ?: Color.BLACK, 300, 1000 )
            setSoun
            setVibrate( behaviour.vibrationPattern )

            /* Style */
            setSmallIcon( smallIconRes )
            setContentTitle( title )
            contentText?.let { setContentText( it ) }

            /* Actions */
            contentIntent?.let { setContentIntent( it ) }
            setAutoCancel( autoCancelOnContentAction )

            /* Defaults */
            setDefaults( behaviour.defaults )

            build()
        }
    }
}

/**
 * Parameters for [NotificationBuilder]
 *
 * @property channelId [String] id of the `NotificationChannel`
 * @see NotificationCompat.Builder.setChannelId
 *
 * @property behaviour [Behaviour] for [Notification]
 */
internal data class NotificationParams(
    val channelId: String,
    val behaviour: Behaviour
)

/** Typealias for call the constructor of [NotificationParams] like it's a function */
internal typealias notificationParams = NotificationParams

/** Typealias for a lambda that takes [NotificationBuilder] as receiver and returns [Unit] */
typealias NotificationBlock = NotificationBuilder.() -> Unit
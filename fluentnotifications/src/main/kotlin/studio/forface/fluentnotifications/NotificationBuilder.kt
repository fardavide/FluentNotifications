@file:Suppress("MemberVisibilityCanBePrivate")

package studio.forface.fluentnotifications

import android.app.Notification
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import studio.forface.fluentnotifications.enum.NotificationImportance
import studio.forface.fluentnotifications.utils.ResourcedBuilder
import studio.forface.fluentnotifications.utils.invoke
import studio.forface.fluentnotifications.utils.optional
import studio.forface.fluentnotifications.utils.required

/**
 * A Builder for create a [Notification]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [NotificationCoreBuilder] via [Context.showNotification]
 * @param context [Context] required for create [Notification] and for retrieve [String]s from `Resources`
 * @param getParams lambda that returns [NotificationParams]
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
class NotificationBuilder internal constructor(
    context: Context,
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
    @get:StringRes var titleRes: Int? = null

    /** A [NotificationCompat.Builder] for create a [Notification] */
    @Suppress( "DEPRECATION" ) /* NotificationCompat.Builder constructor without a Channel's id is deprecated. But we
    will add the id later */
    private val builder = NotificationCompat.Builder( context )

    /** @return [Notification] with the params previously set */
    internal fun build(): Notification = with( getParams() ) {
        return builder

            /* Behaviour */
            .setChannelId( channelId )
            .setPriority( importance.priorityPlatform )

            /* Style */
            .setSmallIcon( smallIconRes )
            .setContentTitle( title )
            .apply { contentText?.let { setContentText( it ) } }

            .build()
    }
}

/**
 * Parameters for [NotificationBuilder]
 *
 * @property channelId [String] id of the `NotificationChannel`
 * @see NotificationCompat.Builder.setChannelId
 *
 * @property importance [NotificationBuilder] that contains priority for the [NotificationCompat]
 * @see NotificationCompat.Builder.setPriority
 */
internal data class NotificationParams(
    val channelId: String,
    val importance: NotificationImportance
)

/** Typealias for call the constructor of [NotificationParams] like it's a function */
internal typealias notificationParams = NotificationParams
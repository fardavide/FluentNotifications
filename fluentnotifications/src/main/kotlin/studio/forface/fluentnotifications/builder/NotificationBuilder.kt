@file:Suppress("MemberVisibilityCanBePrivate")

package studio.forface.fluentnotifications.builder

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BADGE_ICON_NONE
import androidx.core.app.NotificationCompat.BADGE_ICON_SMALL
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.*
import kotlin.reflect.full.primaryConstructor

/**
 * A Builder for create a [Notification]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [NotificationCoreBuilder] via `Context.showNotification`
 * @param context [Context] required for [ResourcedBuilder] delegation and for children Builders
 * @param getParams lambda that returns [NotificationParams]
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
class NotificationBuilder internal constructor(
    @PublishedApi internal val context: Context,
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
     * OPTIONAL block [ActionBlock] for add an Action to the Notification
     * @see NotificationCompat.Builder.addAction
     */
    fun addAction( block: ActionBlock ) {
        actions += ActionBuilder( context ).apply( block ).build()
    }

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

    /**
     * OPTIONAL block that take a sub-type of [NotificationStyle] [S] for build a [NotificationCompat.Style] for the
     * Notification
     *
     * @param S a sub-type of [NotificationStyle]
     */
    inline fun <reified S : NotificationStyle> withStyle( block: S.() -> Unit = {} ) {
        val builder = S::class.primaryConstructor!!.call( context )
        styleBuilder = builder.apply( block )
    }

    /** A [NotificationCompat.Builder] for create a [Notification] */
    @Suppress( "DEPRECATION" ) /* NotificationCompat.Builder constructor without a Channel's id is deprecated. But we
    will add the id later */
    private val builder = NotificationCompat.Builder( context )

    /** A Mutable List of [NotificationCompat.Action] */
    private val actions = mutableListOf<NotificationCompat.Action>()

    /** An OPTIONAL [PendingIntent] for [NotificationCompat.Builder.setContentIntent] */
    private var contentIntent : PendingIntent? by optional()

    /** Whether the Notification need to be cancel on Content Action ( [NotificationCompat.Builder.setAutoCancel] ) */
    private var autoCancelOnContentAction = false

    /** An OPTIONAL instance of [StyleBuilder] for set the Style of the Notification */
    @PublishedApi // Needed for inline
    internal var styleBuilder: StyleBuilder? = null

    /** @return [Notification] with the params previously set */
    internal fun build(): Notification = with( getParams() ) {
        return builder.run {
            setChannelId( channelId )

            /* Behaviour */
            priority = behaviour.importance.priorityPlatform
            setLights( behaviour.lightColor ?: Color.BLACK, 300, 1000 )
            setSound( behaviour.soundUri )
            setVibrate( behaviour.vibrationPattern )

            /* Style */
            setSmallIcon( smallIconRes )
            setContentTitle( title )
            contentText?.let { setContentText( it ) }
            setBadgeIconType( if ( behaviour.showBadge ) BADGE_ICON_SMALL else BADGE_ICON_NONE )
            styleBuilder?.let { setStyle( it.build( title ) ) }

            /* Actions */
            contentIntent?.let { setContentIntent( it ) }
            setAutoCancel( autoCancelOnContentAction )
            actions.forEach { addAction( it ) }

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
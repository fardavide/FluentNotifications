@file:Suppress("MemberVisibilityCanBePrivate")

package studio.forface.fluentnotifications.builder

import android.app.Notification
import android.app.PendingIntent
import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BADGE_ICON_NONE
import androidx.core.app.NotificationCompat.BADGE_ICON_SMALL
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.enum.GroupBehaviour
import studio.forface.fluentnotifications.enum.NotificationCategory
import studio.forface.fluentnotifications.utils.EMPTY_STRING
import studio.forface.fluentnotifications.utils.ResourcedBuilder
import studio.forface.fluentnotifications.utils.invoke
import studio.forface.fluentnotifications.utils.optional
import studio.forface.fluentnotifications.utils.required
import studio.forface.fluentnotifications.utils.setDefaults
import kotlin.reflect.full.primaryConstructor

/**
 * A Builder for create a [Notification]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [NotificationCoreBuilder] via `Context.showNotification`
 * @param coreParams [CoreParams]
 * @param getParams lambda that returns [NotificationParams]
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
open class NotificationBuilder @PublishedApi /* Needed for inline */ internal constructor(
    private val coreParams: CoreParams,
    private val getParams: () -> NotificationParams
) : ResourcedBuilder by coreParams.context() {

    @PublishedApi // Required for inline
    internal val context get()= coreParams.context

    /**
     * OPTIONAL [NotificationCategory] category for the Notification
     * @see NotificationCompat.Builder.setCategory
     */
    var category: NotificationCategory? by optional()

    /**
     * OPTIONAL [Boolean] whether the Notification can be cleared by user
     * @see NotificationCompat.Builder.setOngoing ( cleanable != ongoing )
     */
    var cleanable: Boolean = true

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
     * OPTIONAL [GroupBehaviour] for the Notification
     * If value is not set, will inherit from the parent Group.
     * Default value is [GroupBehaviour.ALERT_CHILDREN]
     *
     * @see NotificationCompat.Builder.setGroupAlertBehavior
     */
    var groupAlertBehaviour: GroupBehaviour? = null

    /**
     * Whether the Notification is a group of other Notifications
     * Default is `false`, but it will be forced to `true` when [NotificationBlock] is called by
     * [NotificationCoreBuilder.groupBy]
     *
     * @see NotificationCompat.Builder.setGroupSummary
     */
    var isGroup = false

    /**
     * REQUIRED [DrawableRes] of the small icon for the Notification
     * @see NotificationCompat.Builder.setSmallIcon
     */
    @get:DrawableRes open var smallIconRes: Int by required()

    /**
     * REQUIRED [CharSequence] content title for the Notification
     * Backed by [titleRes]
     *
     * @see NotificationCompat.Builder.setContentTitle
     */
    open var title: CharSequence by required { titleRes }

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
        actions += ActionBuilder( coreParams ).apply( block ).build()
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
        contentIntent = PendingIntentBuilder( coreParams ).apply( block ).pendingIntent
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
     * OPTIONAL progress for the Notification
     * @see NotificationCompat.Builder.setProgress
     */
    fun setProgress(progress: Int, max: Int, indeterminate: Boolean = false) {
        progressHolder = ProgressHolder(progress, max, indeterminate)
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

    @Suppress( "DEPRECATION" ) /* NotificationCompat.Builder constructor without a Channel's id is deprecated. But we
    will add the id later */
    private val builder = NotificationCompat.Builder( context )

    /** A Mutable List of [NotificationCompat.Action] */
    private val actions = mutableListOf<NotificationCompat.Action>()

    /** An OPTIONAL [PendingIntent] for [NotificationCompat.Builder.setContentIntent] */
    private var contentIntent : PendingIntent? by optional()

    /** Whether the Notification need to be cancel on Content Action ( [NotificationCompat.Builder.setAutoCancel] ) */
    private var autoCancelOnContentAction = false

    private var progressHolder: ProgressHolder? = null

    /** An OPTIONAL instance of [StyleBuilder] for set the Style of the Notification */
    @PublishedApi // Needed for inline
    internal var styleBuilder: StyleBuilder? = null

    /** @return [Notification] with the params previously set */
    internal fun build(): Notification = with( getParams() ) {
        return builder.run {
            setChannelId( channelId )

            /* Group */
            setGroupSummary( isGroup )
            setGroup( groupKey )
            setGroupAlertBehavior( ( this@NotificationBuilder.groupAlertBehaviour ?: groupAlertBehavior ).platform )

            /* Behaviour */
            priority = behaviour.importance.priorityPlatform
            setLights( behaviour.lightColor ?: Color.BLACK, 300, 1000 )
            setSound( behaviour.soundUri )
            setVibrate( behaviour.vibrationPattern )
            setOngoing(!cleanable)

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

            /* Others */
            category?.let{ setCategory( it.platform ) }
            progressHolder?.let { setProgress(it.max, it.progress, it.indeterminate) }

            build()
        }
    }
}


private data class ProgressHolder(
    val progress: Int,
    val max: Int,
    val indeterminate: Boolean
)


/**
 * Parameters for [NotificationBuilder]
 *
 * @property channelId [String] id of the `NotificationChannel`
 * @see NotificationCompat.Builder.setChannelId
 *
 * @property behaviour [Behaviour] for [Notification]
 *
 * @property groupKey [String] key for grouping the [Notification
 * @see NotificationCompat.Builder.setGroup
 */
@PublishedApi // Needed for inline
internal data class NotificationParams(
    val channelId: String,
    val behaviour: Behaviour,
    val groupKey: String,
    val groupAlertBehavior: GroupBehaviour
)

/** Typealias for a lambda that takes [NotificationBuilder] as receiver and returns [Unit] */
typealias NotificationBlock = NotificationBuilder.() -> Unit

/**
 * A Builder for create a [Notification] Group
 * Inherit from [NotificationBuilder]
 *
 * @constructor is internal. Instances will be created from [NotificationCoreBuilder.groupBy]
 *
 * @see NotificationBuilder
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
class NotificationGroupBuilder @PublishedApi /* Needed for inline */ internal constructor(
    coreParams: CoreParams,
    getParams: () -> NotificationParams,
    private val getChildNotificationBuilder: () -> NotificationBuilder
) : NotificationBuilder( coreParams, getParams ) {

    /**
     * OPTIONAL [DrawableRes] of the small icon for the Notification
     * Backed by [getChildNotificationBuilder] [smallIconRes]
     *
     * @see NotificationCompat.Builder.setSmallIcon
     */
    @get:DrawableRes override var smallIconRes: Int = 0
        get() = if ( field != 0 ) field else getChildNotificationBuilder().smallIconRes

    /**
     * OPTIONAL [CharSequence] of the Title for the Notification
     * Backed by [getChildNotificationBuilder] [title]
     *
     * @see NotificationCompat.Builder.setContentTitle
     */
    override var title: CharSequence = EMPTY_STRING
        get() = if ( field != EMPTY_STRING ) field else getChildNotificationBuilder().title
}

/** Typealias for a lambda that takes [NotificationGroupBuilder] as receiver and returns [Unit] */
typealias NotificationGroupBlock = NotificationGroupBuilder.() -> Unit

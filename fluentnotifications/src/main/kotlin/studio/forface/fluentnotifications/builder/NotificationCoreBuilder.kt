package studio.forface.fluentnotifications.builder

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.IntegerRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.enum.GroupBehaviour

/**
 * A Core Builder for create a Notification with its companions ( channel or other optionals )
 * @constructor is internal. Use `Context.showNotification`
 * @param coreParams [CoreParams]
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
class NotificationCoreBuilder internal constructor(
    @PublishedApi // Required for inline
    internal val coreParams: CoreParams
) {

    @PublishedApi // Required for inline
    internal val context get()= coreParams.context

    /** @return [NotificationParams] for a Builder */
    @PublishedApi // Needed for inline
    internal inline val notificationParams get() = NotificationParams(
        channelId =             channelBuilder.id.toString(),
        behaviour =             behaviour,
        groupKey =              notificationGroupTag?.toString() ?: notificationGroupId.toString(),
        groupAlertBehavior =    notificationGroupBuilder?.groupAlertBehaviour ?: GroupBehaviour.ALERT_CHILDREN
    )


    // region BEHAVIOUR

    /** A lazy [Behaviour] by [buildBehaviour] */
    @PublishedApi // Needed for inline
    internal val behaviour by lazy { buildBehaviour() }

    /** [BehaviourBuilder] for define a behaviour for Notification / Channel */
    @PublishedApi // Needed for inline
    internal val behaviourBuilder = BehaviourBuilder( context.resources )

    /** REQUIRED block [BehaviourBlock] for define behaviour for [Notification] / [NotificationChannel] */
    @Suppress("MemberVisibilityCanBePrivate") // Part of public API
    inline fun behaviour( block: BehaviourBlock ) {
        behaviourBuilder.apply( block )
    }

    /** @return [Behaviour] created with params defined in [BehaviourBlock] [behaviour] */
    private fun buildBehaviour() = behaviourBuilder.build()
    // endregion


    // region CHANNEL

    /** [ChannelBuilder] for create a [NotificationChannel] */
    @PublishedApi // Needed for inline
    internal val channelBuilder = ChannelBuilder( context ) {
        channelParams(
            behaviour = behaviour
        )
    }

    /** REQUIRED block [ChannelBlock] for crete the [NotificationChannel] */
    @Suppress("MemberVisibilityCanBePrivate") // Part of public API
    inline fun channel( block: ChannelBlock) {
        channelBuilder.apply( block )
    }

    /** @return [NotificationChannel] created with params defined in [ChannelBlock] [channel] */
    @RequiresApi(Build.VERSION_CODES.O)
    internal fun buildChannel() = channelBuilder.build()
    // endregion


    // region GROUP

    /** [NotificationGroupBuilder] for create a [Notification] that works as group */
    @PublishedApi // Needed for inline
    internal var notificationGroupBuilder : NotificationGroupBuilder? = null

    /** [Int] id for create the Notification Group */
    @PublishedApi // Needed for inline
    internal var notificationGroupId : Int = 0

    /** [CharSequence] tag for create the Notification */
    @PublishedApi // Needed for inline
    internal var notificationGroupTag : CharSequence? = null

    /**
     * OPTIONAL block [NotificationGroupBlock] for crete the [Notification] Group
     * @param idRes REQUIRED [IntegerRes] id for create the Notification Group
     * @param tagRes OPTIONAL [StringRes] tag for create the Notification Group. If `null` if will be ignored
     * Default is `null`
     */
    @Suppress("MemberVisibilityCanBePrivate") // Part of public API
    inline fun groupBy( @IntegerRes idRes: Int, @StringRes tagRes: Int? = null, block: NotificationGroupBlock ) {
        groupBy( context.resources.getInteger( idRes ), tagRes?.let( context::getString ), block  )
    }

    /**
     * OPTIONAL block [NotificationGroupBlock] for crete the [Notification] Group
     * @param id REQUIRED [Int] id for create the Notification Group
     * @param tag OPTIONAL [CharSequence] tag for create the Notification Group. If `null` if will be ignored
     * Default is `null`
     */
    @Suppress("MemberVisibilityCanBePrivate") // Part of public API
    inline fun groupBy( id: Int, tag: CharSequence? = null, block: NotificationGroupBlock = {} ) {
        notificationGroupId = id
        notificationGroupTag = tag
        notificationGroupBuilder = NotificationGroupBuilder( coreParams, { notificationParams }, { notificationBuilder } )
        notificationGroupBuilder!!.apply( block ).apply {
            isGroup = true
        }
    }

    /** @return [Notification] created with params defined in [NotificationBlock] [groupBy] */
    internal fun buildNotificationGroup() = notificationGroupBuilder?.build()
    // endregion


    // region NOTIFICATION

    /** [NotificationBuilder] for create a [Notification] */
    @PublishedApi // Needed for inline
    internal val notificationBuilder = NotificationBuilder( coreParams ) { notificationParams }

    /** REQUIRED block [NotificationBlock] for crete the [Notification] */
    @Suppress("MemberVisibilityCanBePrivate") // Part of public API
    inline fun notification( block: NotificationBlock ) {
        notificationBuilder.apply( block )
    }

    /** @return [Notification] created with params defined in [NotificationBlock] [notification] */
    internal fun buildNotification() = notificationBuilder.build()
    // endregion
}

/**
 * Params for [NotificationCoreBuilder]
 *
 * @param context [Context] required for create [Notification] and for provide `Resources` for Builders
 * @param notificationId [Int] required for propagate notification id to Builders
 * @param notificationTag OPTIONAL [String] required for propagate notification tag to Builders
 */
internal data class CoreParams(
    val context: Context,
    val notificationId: Int,
    val notificationTag: String? = null
)

/** Typealias for a lambda that takes [NotificationCoreBuilder] as receiver and returns [Unit] */
typealias NotificationCoreBlock = NotificationCoreBuilder.() -> Unit
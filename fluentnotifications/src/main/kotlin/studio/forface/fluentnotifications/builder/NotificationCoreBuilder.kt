package studio.forface.fluentnotifications.builder

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import studio.forface.fluentnotifications.NotificationDsl

/**
 * A Core Builder for create a Notification with its companions ( channel or other optionals )
 * @constructor is internal. Use `Context.showNotification`
 * @param context [Context] required for create [Notification] and for provide `Resources` for Builders
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
class NotificationCoreBuilder internal constructor( context: Context ) {

    /* = = = = = BEHAVIOUR = = = = = */

    /** A lazy [Behaviour] by [buildBehaviour] */
    private val behaviour by lazy { buildBehaviour() }

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


    /* = = = = = CHANNEL = = = = = */

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


    /* = = = = = NOTIFICATION = = = = = */

    /** [NotificationBuilder] for create a [Notification] */
    @PublishedApi // Needed for inline
    internal val notificationBuilder = NotificationBuilder( context ) {
        notificationParams(
            channelId = channelBuilder.id.toString(),
            behaviour = behaviour
        )
    }

    /** REQUIRED block [NotificationBlock] for crete the [Notification] */
    @Suppress("MemberVisibilityCanBePrivate") // Part of public API
    inline fun notification( block: NotificationBlock ) {
        notificationBuilder.apply( block )
    }

    /** @return [Notification] created with params defined in [NotificationBlock] [notification] */
    internal fun buildNotification() = notificationBuilder.build()
}

/** Typealias for a lambda that takes [NotificationCoreBuilder] as receiver and returns [Unit] */
typealias NotificationCoreBlock = NotificationCoreBuilder.() -> Unit
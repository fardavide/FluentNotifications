@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package studio.forface.fluentnotifications.builder

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.core.app.Person
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.ResourcedBuilder
import studio.forface.fluentnotifications.utils.invoke
import studio.forface.fluentnotifications.utils.optional
import studio.forface.fluentnotifications.utils.required
import kotlin.Boolean
import kotlin.CharSequence
import kotlin.DslMarker
import kotlin.Int
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.TODO
import kotlin.collections.forEach
import kotlin.collections.mutableListOf
import kotlin.collections.plus
import kotlin.collections.plusAssign
import kotlin.let
import kotlin.plus
import kotlin.apply as kotlinApply

/**
 * An abstract Builder for create [NotificationCompat.Style]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Cannot be extended outside of the library.
 * @param context [Context] required for [ResourcedBuilder] delegation.
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
abstract class StyleBuilder internal constructor(
    context: Context
) : ResourcedBuilder by context() {

    /**
     * OPTIONAL [CharSequence] content title for the [NotificationCompat.Style]
     * It is optional since if this value is not provided, `notificationTitle` ( Title of the Notification that owns
     * this Style ) will be used
     * @see build
     *
     * Backed by [titleRes]
     *
     * This will be ignored for [DecoratedCustomViewStyleBuilder]
     */
    var title: CharSequence? by optional { titleRes }

    /**
     * OPTIONAL [StringRes] content title for the [NotificationCompat.Style]
     * It is optional since if this value is not provided, `notificationTitle` ( Title of the Notification that owns
     * this Style ) will be used
     * @see build
     *
     * Backing value of [title]
     *
     * This will be ignored for [DecoratedCustomViewStyleBuilder]
     */
    @StringRes
    var titleRes: Int? = null

    /**
     * @return a subtype of [NotificationCompat.Style]
     * @param notificationTitle [CharSequence] Title of the Notification that owns the created style.
     */
    internal abstract fun build( notificationTitle: CharSequence ) : NotificationCompat.Style
}

/** Typealias for [StyleBuilder] */
typealias NotificationStyle = StyleBuilder

/**
 * A Builder for create [NotificationCompat.BigPictureStyle]
 * Inherit from [StyleBuilder]
 *
 * @constructor in internal. Instances will be created from [NotificationBuilder.withStyle]
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
class BigPictureStyleBuilder internal constructor( context: Context ) : StyleBuilder( context ) {

    /**
     * REQUIRED [Bitmap] big picture for [NotificationCompat.BigPictureStyle]
     * This value doesn't have a backing resource because it won't make any sense to show a resource here
     *
     * @see NotificationCompat.BigPictureStyle.bigPicture
     */
    var bigPicture: Bitmap by required()

    /**
     * OPTIONAL [Bitmap] large icon for [NotificationCompat.BigPictureStyle]
     * Backed from [largeIconRes]
     *
     * @see NotificationCompat.BigPictureStyle.bigLargeIcon
     */
    var largeIcon: Bitmap? by optional { largeIconRes }

    /**
     * OPTIONAL [DrawableRes] large icon for [NotificationCompat.BigPictureStyle]
     * Backing resource of [largeIconRes]
     *
     * @see NotificationCompat.BigPictureStyle.bigLargeIcon
     */
    @DrawableRes var largeIconRes: Int? = null

    /**
     * OPTIONAL [CharSequence] summary for [NotificationCompat.BigPictureStyle]
     * Backed from [summaryRes]
     *
     * @see NotificationCompat.BigPictureStyle.setSummaryText
     */
    var summary: CharSequence? by optional { summaryRes }

    /**
     * OPTIONAL [StringRes] summary for [NotificationCompat.BigPictureStyle]
     * Backing resource of [summary]
     *
     * @see NotificationCompat.BigPictureStyle.setSummaryText
     */
    @StringRes var summaryRes: Int? = null

    /**
     * @return a subtype of [NotificationCompat.BigPictureStyle]
     * @param notificationTitle [CharSequence] Title of the Notification that owns the created style.
     */
    override fun build( notificationTitle: CharSequence ): NotificationCompat.Style {
        return NotificationCompat.BigPictureStyle().kotlinApply {
            setBigContentTitle( title ?: notificationTitle )
            bigPicture( bigPicture )
            summary?.let { setSummaryText( it ) }
            largeIcon?.let { bigLargeIcon( it ) }
        }
    }
}

/** Typealias for [BigPictureStyleBuilder] */
typealias BigPictureStyle = BigPictureStyleBuilder

/**
 * A Builder for create [NotificationCompat.BigTextStyle]
 * Inherit from [StyleBuilder]
 *
 * @constructor in internal. Instances will be created from [NotificationBuilder.withStyle]
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
class BigTextStyleBuilder internal constructor( context: Context ) : StyleBuilder( context ) {

    /**
     * OPTIONAL [CharSequence] big text for [NotificationCompat.BigTextStyle]
     * Backed from [bigTextRes]
     *
     * @see NotificationCompat.BigTextStyle.bigText
     */
    var bigText: CharSequence by required { bigTextRes }

    /**
     * OPTIONAL [StringRes] big text for [NotificationCompat.BigTextStyle]
     * Backing resource of [bigText]
     *
     * @see NotificationCompat.BigTextStyle.bigText
     */
    @StringRes var bigTextRes: Int? = null

    /**
     * OPTIONAL [CharSequence] summary for [NotificationCompat.BigTextStyle]
     * Backed from [summaryRes]
     *
     * @see NotificationCompat.BigTextStyle.setSummaryText
     */
    var summary: CharSequence? by optional { summaryRes }

    /**
     * OPTIONAL [StringRes] summary for [NotificationCompat.BigTextStyle]
     * Backing resource of [summary]
     *
     * @see NotificationCompat.BigTextStyle.setSummaryText
     */
    @StringRes var summaryRes: Int? = null

    /**
     * @return a subtype of [NotificationCompat.BigTextStyle]
     * @param notificationTitle [CharSequence] Title of the Notification that owns the created style.
     */
    override fun build( notificationTitle: CharSequence ): NotificationCompat.Style {
        return NotificationCompat.BigTextStyle().kotlinApply {
            setBigContentTitle( title ?: notificationTitle )
            bigText( bigText )
            summary?.let { setSummaryText( it ) }
        }
    }
}

/** Typealias for [BigTextStyleBuilder] */
typealias BigTextStyle = BigTextStyleBuilder

/**
 * A Builder for create [NotificationCompat.DecoratedCustomViewStyle]
 * Inherit from [StyleBuilder]
 *
 * @constructor in internal. Instances will be created from [NotificationBuilder.withStyle]
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
class DecoratedCustomViewStyleBuilder internal constructor( context: Context ) : StyleBuilder( context ) {

    /**
     * @return a subtype of [NotificationCompat.BigTextStyle]
     * @param notificationTitle [CharSequence] Title of the Notification that owns the created style.
     */
    override fun build( notificationTitle: CharSequence ): NotificationCompat.Style {
        return NotificationCompat.DecoratedCustomViewStyle().kotlinApply {
            TODO("Not implemented")
        }
    }
}

/** Typealias for [DecoratedCustomViewStyleBuilder] */
typealias DecoratedCustomViewStyle = DecoratedCustomViewStyleBuilder

/**
 * A Builder for create [NotificationCompat.InboxStyle]
 * Inherit from [StyleBuilder]
 *
 * @constructor in internal. Instances will be created from [NotificationBuilder.withStyle]
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
class InboxStyleBuilder internal constructor( context: Context ) : StyleBuilder( context ) {

    /**
     * OPTIONAL [CharSequence] summary for [NotificationCompat.InboxStyle]
     * Backed from [summaryRes]
     *
     * @see NotificationCompat.InboxStyle.setSummaryText
     */
    var summary: CharSequence? by optional { summaryRes }

    /**
     * OPTIONAL [StringRes] summary for [NotificationCompat.InboxStyle]
     * Backing resource of [summary]
     *
     * @see NotificationCompat.InboxStyle.setSummaryText
     */
    @StringRes var summaryRes: Int? = null

    /**
     * Add a [CharSequence] Line to [NotificationCompat.InboxStyle]
     * @see NotificationCompat.InboxStyle.addLine
     *
     * @return [LineConcatenation]
     */
    operator fun plus( line: CharSequence ) : LineConcatenation {
        lines += line
        return LineConcatenation
    }

    /**
     * Add a [CharSequence] Line to [NotificationCompat.InboxStyle] from another [CharSequence]
     * E.g. `this + "Line #1" + "Line #2"`
     * @see NotificationCompat.InboxStyle.addLine
     *
     * @return [LineConcatenation]
     */
    operator fun LineConcatenation.plus( line: CharSequence ) : LineConcatenation {
        lines += line
        return LineConcatenation
    }

    /** A Mutable List of [CharSequence] for [NotificationCompat.InboxStyle] */
    private val lines = mutableListOf<CharSequence>()

    /**
     * @return a subtype of [NotificationCompat.InboxStyle]
     * @param notificationTitle [CharSequence] Title of the Notification that owns the created style.
     */
    override fun build( notificationTitle: CharSequence ): NotificationCompat.Style {
        return NotificationCompat.InboxStyle().kotlinApply {
            setBigContentTitle( title ?: notificationTitle )
            summary?.let { setSummaryText( it ) }
            lines.forEach { addLine ( it ) }
        }
    }

    /**
     * An object for concatenate [plus] operator for [lines]
     * @see LineConcatenation.plus
     */
    object LineConcatenation
}

/** Typealias for [InboxStyleBuilder] */
typealias InboxStyle = InboxStyleBuilder

/**
 * A Builder for create [NotificationCompat.MessagingStyle]
 * Inherit from [StyleBuilder]
 *
 * @constructor in internal. Instances will be created from [NotificationBuilder.withStyle]
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
class MessagingStyleBuilder internal constructor( context: Context ) : StyleBuilder( context ) {

    /**
     * OPTIONAL [Bundle] extras for the [NotificationCompat.MessagingStyle]
     * @see NotificationCompat.MessagingStyle.addCompatExtras
     */
    var extras: Bundle? by optional()

    /**
     * Whether the Message is a group conversation
     * Default is `false`
     *
     * @see NotificationCompat.MessagingStyle.setGroupConversation
     */
    var groupConversation: Boolean = false

    /** REQUIRED [PersonBlock] for create [Person] for [NotificationCompat.MessagingStyle] */
    inline fun person( block: PersonBlock ) {
        personBuilder.kotlinApply( block )
    }

    /**
     * @return [Person] created by given [PersonBlock]
     * It would be useful for create a [Person] to assign to multiple [Message] s
     */
    inline fun createPerson( block: PersonBlock ) : Person =
        PersonBuilder( resources ).kotlinApply( block ).build()

    /**
     * Add a [Message] to [NotificationCompat.MessagingStyle]
     * @param block [MessageBlock] for create a [Message]
     *
     * @see NotificationCompat.MessagingStyle.addMessage
     *
     * @return [MessageConcatenation]
     */
    operator fun plus( block: MessageBlock ) : MessageConcatenation {
        messageBuilders += MessageBuilder( resources ) { personBuilder }.kotlinApply( block )
        return MessageConcatenation
    }

    /**
     * Add a [Message] to [NotificationCompat.MessagingStyle] from another [Message]
     * E.g. `this + { ... <build message> ... } + { ... <build message> ... }`
     *
     * @param block [MessageBlock] for create a [Message]
     *
     * @see NotificationCompat.MessagingStyle.addMessage
     *
     * @return [MessageConcatenation]
     */
    operator fun MessageConcatenation.plus( block: MessageBlock ) : MessageConcatenation {
        messageBuilders += MessageBuilder( resources ) { personBuilder }.kotlinApply( block )
        return MessageConcatenation
    }

    /** A Mutable List of [MessageBuilder] for [NotificationCompat.MessagingStyle] */
    private val messageBuilders = mutableListOf<MessageBuilder>()

    /** An instance of [PersonBuilder] for generate a [Person] for [NotificationCompat.MessagingStyle] */
    @PublishedApi // Needed for inline
    internal val personBuilder = PersonBuilder( resources )

    /**
     * @return a subtype of [NotificationCompat.MessagingStyle]
     * @param notificationTitle [CharSequence] Title of the Notification that owns the created style.
     */
    override fun build( notificationTitle: CharSequence ): NotificationCompat.Style {
        return NotificationCompat.MessagingStyle( personBuilder.build() ).kotlinApply {
            conversationTitle = title ?: notificationTitle
            isGroupConversation = groupConversation
            messageBuilders.forEach { addMessage( it.build() ) }
            extras?.let { addCompatExtras( it ) }
        }
    }

    /**
     * An object for concatenate [plus] operator for [messageBuilders]
     * @see MessageConcatenation.plus
     */
    object MessageConcatenation
}

/** Typealias for [MessagingStyleBuilder] */
typealias MessagingStyle = MessagingStyleBuilder
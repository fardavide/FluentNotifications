@file:Suppress("MemberVisibilityCanBePrivate")

package studio.forface.fluentnotifications.builder

import android.content.Context
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import kotlin.apply as kotlinApply // Needed since some NotificationCompat.Style's also have `apply` method
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.ResourcedBuilder
import studio.forface.fluentnotifications.utils.invoke
import studio.forface.fluentnotifications.utils.optional

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
     */
    var title: CharSequence? by optional { titleRes }

    /**
     * OPTIONAL [StringRes] content title for the [NotificationCompat.Style]
     * It is optional since if this value is not provided, `notificationTitle` ( Title of the Notification that owns
     * this Style ) will be used
     * @see build
     *
     * Backing value of [title]
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
 * A Builder fom create [NotificationCompat.InboxStyle]
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
     * OPTIONAL [CharSequence] summary for [NotificationCompat.InboxStyle]
     * Backing value of [summary]
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
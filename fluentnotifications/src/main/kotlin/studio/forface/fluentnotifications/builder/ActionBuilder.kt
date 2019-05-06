@file:Suppress("MemberVisibilityCanBePrivate")

package studio.forface.fluentnotifications.builder

import android.app.PendingIntent
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.ResourcedBuilder
import studio.forface.fluentnotifications.utils.invoke
import studio.forface.fluentnotifications.utils.required
import studio.forface.fluentnotifications.utils.requiredOnce

/**
 * A Builder for create a [NotificationCompat.Action]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [NotificationBuilder.addAction]
 * @param context [Context] required for [ResourcedBuilder] delegation and [PendingIntentBuilder]
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
class ActionBuilder internal constructor(
    private val context: Context
) : ResourcedBuilder by context() {

    /** REQUIRED [DrawableRes] Icon for [NotificationCompat.Action] */
    @get:DrawableRes var iconRes: Int by required()

    /**
     * REQUIRED [CharSequence] Text for [NotificationCompat.Action]
     * Backed by [textRes]
     */
    var text: CharSequence by required { textRes }

    /**
     * REQUIRED [StringRes] Text for [NotificationCompat.Action]
     * Backing value of [text]
     */
    @get:StringRes var textRes: Int? = null

    /** REQUIRED block [PendingIntentBlock] for [NotificationCompat.Action] */
    fun onAction( block: PendingIntentBlock ) {
        intent = PendingIntentBuilder( context ).apply( block ).pendingIntent
    }

    /** REQUIRED [PendingIntent] */
    private var intent : PendingIntent by requiredOnce()

    /** @return [NotificationCompat.Action] */
    internal fun build() = NotificationCompat.Action( iconRes, text, intent )
}

/** Typealias for a lambda that takes [ActionBuilder] as receiver and returns [Unit] */
typealias ActionBlock = ActionBuilder.() -> Unit
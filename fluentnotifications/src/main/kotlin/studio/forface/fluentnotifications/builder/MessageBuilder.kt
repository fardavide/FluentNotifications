@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package studio.forface.fluentnotifications.builder

import android.content.res.Resources
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat.MessagingStyle
import androidx.core.app.Person
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.ResourcedBuilder
import studio.forface.fluentnotifications.utils.invoke
import studio.forface.fluentnotifications.utils.optional
import studio.forface.fluentnotifications.utils.required
import java.lang.System.currentTimeMillis

/**
 * A Builder for create a [MessagingStyle.Message]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [MessagingStyleBuilder.plus]
 * @param resources [Resources] required for [ResourcedBuilder] delegation
 * @param getStylePersonBuilder a lambda that returns [PersonBuilder] for inherit person from [MessagingStyle]
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
class MessageBuilder internal constructor (
    resources: Resources,
    private val getStylePersonBuilder: () -> PersonBuilder
) : ResourcedBuilder by resources() {

    /**
     * OPTIONAL [Person] to be assigned to [MessagingStyle.Message]
     * Use this for assign a [Person] created outside of this block
     *
     * @see MessagingStyle.Message.getPerson
     */
    var person: Person? by optional()

    /**
     * REQUIRED [CharSequence] text for the [MessagingStyle.Message]
     * Backed by [textRes]
     *
     * @see MessagingStyle.Message.mText
     */
    var text: CharSequence by required { textRes }

    /**
     * REQUIRED [StringRes] text for the [MessagingStyle.Message]
     * Backing value of [text]
     *
     * @see MessagingStyle.Message.getText
     */
    @StringRes
    var textRes: Int? = null

    /**
     * [Long] timestamp for the [MessagingStyle.Message]
     * Default is [currentTimeMillis]
     *
     * @see MessagingStyle.Message.getTimestamp
     */
    var timestamp: Long = currentTimeMillis()

    /**
     * OPTIONAL [PersonBlock] for create [Person] for the [MessagingStyle.Message]
     * Default person will be inherited from [MessagingStyle]
     *
     * @see MessagingStyle.Message.getPerson
     */
    inline fun person( block: PersonBlock ) {
        personBuilder = PersonBuilder( resources )
        personBuilder!!.apply( block )
    }

    /**
     * Set data for the [MessagingStyle.Message]
     * @see MessagingStyle.Message.setData
     */
    fun setData( dataMimeType: String, dataUri: Uri ) {
        dataHolder = DataHolder( dataMimeType, dataUri )
    }

    /** An OPTIONAL [DataHolder] for the [MessagingStyle.Message] */
    private var dataHolder: DataHolder? = null

    /** An OPTIONAL instance of [PersonBuilder] for generate a [Person] for [MessagingStyle.Message] */
    @PublishedApi // Needed for inline
    internal var personBuilder: PersonBuilder? = null

    /** @return [MessagingStyle.Message] */
    internal fun build() : MessagingStyle.Message {
        // Get from direct person assignment, personBuilder or getStylePersonBuilder
        val finalPerson = person ?: personBuilder?.build() ?: getStylePersonBuilder().build()
        return MessagingStyle.Message( text, timestamp, finalPerson ).apply {
            dataHolder?.let { setData( it.dataMimeType, it.dataUri ) }
        }
    }

    /** A class for hold [dataMimeType] and [dataUri] together */
    private data class DataHolder( val dataMimeType: String, val dataUri: Uri )
}

/** Typealias for a lambda that takes [MessageBuilder] as receiver and returns [Unit] */
typealias MessageBlock = MessageBuilder.() -> Unit
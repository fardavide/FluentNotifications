@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package studio.forface.fluentnotifications.builder

import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.ResourcedBuilder
import studio.forface.fluentnotifications.utils.invoke
import studio.forface.fluentnotifications.utils.optional
import studio.forface.fluentnotifications.utils.required

/**
 * A Builder for create a [Person]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [MessagingStyleBuilder.person]
 * @param resources [Resources] required for [ResourcedBuilder] delegation
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
class PersonBuilder @PublishedApi internal constructor( // Constructor needs to be invoked from inline function
    resources: Resources
) : ResourcedBuilder by resources() {

    /**
     * Defines whether the [Person] is a bot.
     * Default is `false`
     *
     * @see Person.Builder.setBot
     */
    var bot: Boolean = false

    /**
     * OPTIONAL [IconCompat] icon for the [Person]
     * Backed by [iconRes]
     *
     * @see Person.Builder.setIcon
     */
    var icon: IconCompat by optional { iconRes }

    /**
     * OPTIONAL [IconCompat] icon for the [Person]
     * This will set [icon]. Getter will always return `null`
     *
     * @see Person.Builder.setIcon
     */
    var iconBitmap: Bitmap? = null
        set( value ) { icon = IconCompat.createWithBitmap( value ) }

    /**
     * OPTIONAL [IconCompat] icon for the [Person]
     * Backing resource of [icon]
     *
     * @see Person.Builder.setIcon
     */
    @DrawableRes var iconRes: Int? = null

    /**
     * Defines whether the [Person] is important.
     * Default is `false`
     *
     * @see Person.Builder.setImportant
     */
    var important: Boolean = false

    /**
     * OPTIONAL [String] key for the [Person]
     * Backed by [keyRes]
     *
     * @see Person.Builder.setKey
     */
    var key: String? by optional { keyRes }

    /**
     * OPTIONAL [StringRes] key for the [Person]
     * Backing resource of [key]
     *
     * @see Person.Builder.setKey
     */
    @StringRes var keyRes: Int? = null

    /**
     * REQUIRED [CharSequence] name for the [Person]
     * Backed by [nameRes]
     *
     * @see Person.Builder.setName
     */
    var name: CharSequence by required { nameRes }

    /**
     * REQUIRED [StringRes] name for the [Person]
     * Backing resource of [name]
     *
     * @see Person.Builder.setName
     */
    @StringRes var nameRes: Int? = null

    /**
     * OPTIONAL [Uri] for [Person]
     * @see Person.Builder.setUri
     */
    var uri: Uri? = null

    /**
     * OPTIONAL [String] representation of Uri for [Person]
     * @see Person.Builder.setUri
     */
    var uriString: String? = null

    /** @return [Person] */
    @PublishedApi  // Needed for inline
    internal fun build() : Person {
        return Person.Builder()
            .setBot( bot )
            .setIcon( icon )
            .setImportant( important )
            .setKey( key )
            .setName( name )
            .setUri( uriString ?: uri?.toString() )
            .build()
    }
}

/** Typealias for a lambada that takes a [PersonBuilder] as receiver and returns [Unit] */
typealias PersonBlock = PersonBuilder.() -> Unit
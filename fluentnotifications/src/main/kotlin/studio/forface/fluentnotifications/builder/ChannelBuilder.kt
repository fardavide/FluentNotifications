@file:Suppress("MemberVisibilityCanBePrivate")

package studio.forface.fluentnotifications.builder

import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import studio.forface.fluentnotifications.NotificationDsl
import studio.forface.fluentnotifications.utils.*

/**
 * A Builder for create a [NotificationChannel]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [NotificationCoreBuilder] via `Context.showNotification`
 * @param context [Context] required for [ResourcedBuilder] delegation
 * @param getParams lambda that returns [ChannelParams]
 *
 * @see NotificationDsl as [DslMarker]
 *
 *
 * @author Davide Giuseppe Farella
 */
@NotificationDsl
class ChannelBuilder internal constructor(
    context: Context,
    private val getParams: () -> ChannelParams
) : ResourcedBuilder by context() {

    /**
     * OPTIONAL [String] description for the [NotificationChannel]
     * Backed by [descriptionRes]
     *
     * @see NotificationChannel.setDescription
     */
    var description: String? by optional { descriptionRes }

    /**
     * OPTIONAL [StringRes] description for the [NotificationChannel]
     * Backing value of [description]
     *
     * @see NotificationChannel.setDescription
     */
    @StringRes var descriptionRes: Int? = null

    /**
     * REQUIRED [String] id for the [NotificationChannel]
     * Backed by [idRes]
     *
     * @see NotificationChannel.getId
     */
    var id: String by required { idRes }

    /**
     * REQUIRED [StringRes] id for the [NotificationChannel]
     * Backing value of [id]
     *
     * @see NotificationChannel.getId
     */
    @StringRes var idRes: Int? = null

    /**
     * REQUIRED [CharSequence] name for the [NotificationChannel]
     * Backed by [nameRes]
     *
     * @see NotificationChannel.setName
     */
    var name: CharSequence by required { nameRes }

    /**
     * REQUIRED [StringRes] name for the [NotificationChannel]
     * Backing value of [name]
     *
     * @see NotificationChannel.setName
     */
    @StringRes var nameRes: Int? = null

    /** @return [NotificationChannel] with the params previously set */
    @RequiresApi(Build.VERSION_CODES.O)
    internal fun build() = getParams().behaviour.let { behaviour ->

        NotificationChannel( id, name, behaviour.importance.importancePlatform ).apply {

            // Description
            description = this@ChannelBuilder.description

            // Light
            enableLights( behaviour.lightColor != null )
            behaviour.lightColor?.let { lightColor = behaviour.lightColor }
            setSound()

            // Vibration
            enableVibration( behaviour.vibrationPattern.isNotEmpty() )
            vibrationPattern = behaviour.vibrationPattern

            // Default
            setDefaults( behaviour.defaults )
        }
    }
}

/** Add a `channel` block to [NotificationCoreBuilder] with the given [String] id and [CharSequence] name */
fun NotificationCoreBuilder.channel( id: String, name: CharSequence, block: ChannelBlock = {} ) =
    channel {
        this.id = id
        this.name = name
        block()
    }

/** Add a `channel` block to [NotificationCoreBuilder] with the given [StringRes] id and [StringRes] name */
fun NotificationCoreBuilder.channel( @StringRes idRes: Int, @StringRes nameRes: Int, block: ChannelBlock = {} ) =
    channel {
        this.idRes = idRes
        this.nameRes = nameRes
        block()
    }

/**
 * Parameters for [ChannelBuilder]
 *
  * @property behaviour [Behaviour] for [NotificationChannel]
 */
internal data class ChannelParams(
    val behaviour: Behaviour
)

/** Typealias for call the constructor of [ChannelParams] like it's a function */
internal typealias channelParams = ChannelParams

/** Typealias for a lambda that takes [ChannelBuilder] as receiver and return [Unit] */
typealias ChannelBlock = ChannelBuilder.() -> Unit
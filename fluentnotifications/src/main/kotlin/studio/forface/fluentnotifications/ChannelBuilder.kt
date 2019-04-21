@file:Suppress("MemberVisibilityCanBePrivate")

package studio.forface.fluentnotifications

import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import studio.forface.fluentnotifications.enum.NotificationImportance
import studio.forface.fluentnotifications.utils.ResourcedBuilder
import studio.forface.fluentnotifications.utils.invoke
import studio.forface.fluentnotifications.utils.required

/**
 * A Builder for create a [NotificationChannel]
 * Inherit from [ResourcedBuilder] for provide `Resources`
 *
 * @constructor is internal. Instances will be created from [NotificationCoreBuilder] via [Context.showNotification]
 * @param context [Context] required for retrieve [String]s from `Resources`
 * @param getParams lambda that returns [ChannelParams]
 *
 * @see NotificationDsl as [DslMarker]
 */
@NotificationDsl
class ChannelBuilder internal constructor(
    context: Context,
    private val getParams: () -> ChannelParams
) : ResourcedBuilder by context() {

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
    @get:StringRes var idRes: Int? = null

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
    @get:StringRes var nameRes: Int? = null

    /** @return [NotificationChannel] with the params previously set */
    @RequiresApi(Build.VERSION_CODES.O)
    internal fun build() = with( getParams() ) {
        NotificationChannel( id, name, importance.importancePlatform )
    }
}

/** Add a `channel` block to [NotificationCoreBuilder] with the given [String] id and [CharSequence] name */
fun NotificationCoreBuilder.channel( id: String, name: CharSequence ) =
    channel {
        this.id = id
        this.name = name
    }

/** Add a `channel` block to [NotificationCoreBuilder] with the given [StringRes] id and [StringRes] name */
fun NotificationCoreBuilder.channel( @StringRes idRes: Int, @StringRes nameRes: Int ) =
    channel {
        this.idRes = idRes
        this.nameRes = nameRes
    }

/**
 * Parameters for [ChannelBuilder]
 *
 * @property importance [NotificationBuilder] that contains importance for the [NotificationChannel]
 * @see NotificationChannel.setImportance
 */
internal data class ChannelParams(
    val importance: NotificationImportance
)

/** Typealias for call the constructor of [ChannelParams] like it's a function */
internal typealias channelParams = ChannelParams
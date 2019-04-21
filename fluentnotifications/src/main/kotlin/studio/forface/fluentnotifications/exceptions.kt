package studio.forface.fluentnotifications

import android.app.NotificationManager
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import studio.forface.fluentnotifications.utils.required

/** An [Exception] that will be thrown if [NotificationManager] can't be retrieved on the device */
internal class NotificationManagerNotFoundException : Exception(
    "Impossible to get 'NotificationManager' service. What went wrong? :S"
)

/** An [Exception] that will be thrown when both a property [required] and its backing resource has not been set */
internal class RequiredPropertyNotSetException( parentClass: KClass<*>, property: KProperty<*> ) : Exception(
    "Required property '${parentClass.simpleName}.${property.name}' is null"
)

/**
 * An [IllegalArgumentException] that will be thrown when its impossible to resolve a resource for the required [KClass]
 * type
 */
internal class IllegalResourceException( resourceClass: KClass<*> ) : IllegalArgumentException(
    "Impossible to resolve a resource for type '${resourceClass.qualifiedName}'"
)
package studio.forface.fluentnotifications

import android.app.NotificationManager
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import studio.forface.fluentnotifications.utils.required

/**
 * An [IllegalArgumentException] that will be thrown when its impossible to resolve a resource for the required [KClass]
 * type
 */
internal class IllegalResourceException( resourceClass: KClass<*> ) : IllegalArgumentException(
    "Impossible to resolve a resource for type '${resourceClass.qualifiedName}'"
)

/** An [Exception] that will be thrown if [NotificationManager] can't be retrieved on the device */
internal class NotificationManagerNotFoundException : Exception(
    "Impossible to get 'NotificationManager' service. What went wrong? :S"
)

/**
 * An [IllegalStateException] that will be thrown when a try to set `OptionalOnceProperty` or `RequiredOnceProperty`
 * occurs when the property already set once
 */
internal class PropertyAlreadySetException( parentClass: KClass<*>, property: KProperty<*> ) : IllegalStateException(
    "Property '${parentClass.simpleName}.${property.name}' has already been set and cannot be set more than once"
)

/**
 * An [IllegalStateException] that will be thrown when both a property [required] and its backing resource has not been
 * set
 */
internal class RequiredPropertyNotSetException( parentClass: KClass<*>, property: KProperty<*> ) : IllegalStateException(
    "Required property '${parentClass.simpleName}.${property.name}' is null"
)

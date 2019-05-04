package studio.forface.fluentnotifications.utils

import android.content.res.Resources
import studio.forface.fluentnotifications.PropertyAlreadySetException
import studio.forface.fluentnotifications.RequiredPropertyNotSetException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Delegation function for [OptionalProperty]
 * E.g. `private var someValue: String? by optional()` or `... by optional { someBackingResourceId }`
 * @see OptionalProperty
 *
 * @param T is [Nullable]
 * @param getBackingResourceId a lambda that returns a NULLABLE [Int] id of the Resource that will be resolved for be
 * used in case the Property has not been set
 * E.g. >
    @get: StringRes var channelIdRes: Int? = null
    var channelId: String by optional { channelIdRes }`
 * Default is a lambda that returns `null`
 *
 * @author Davide Giuseppe Farella
 */
internal inline fun <reified T : Nullable> optional( noinline getBackingResourceId: () -> Int? = { null } ) =
    OptionalProperty<T>( T::class, getBackingResourceId )

/**
 * Delegation function for [RequiredProperty]
 * E.g. `private var someValue: String? by required()` or `... by required { someBackingResourceId }`
 * @see RequiredProperty
 *
 * @param T is [NotNullable]
 * @param getBackingResourceId a lambda that returns a NULLABLE [Int] id of the Resource that will be resolved for be
 * used in case the Property has not been set
 * E.g. >
@get: StringRes var channelIdRes: Int? = null
var channelId: String by required { channelIdRes }`
 * Default is a lambda that returns `null`
 *
 * @author Davide Giuseppe Farella
 */
internal inline fun <reified T : NotNullable> required( noinline getBackingResourceId: () -> Int? = { null } ) =
    RequiredProperty<T>( T::class, getBackingResourceId )

/**
 * Delegation function for [OptionalProperty]
 * E.g. `private var someValue: String? by optionalOnce()` or `... by optionalOnce { someBackingResourceId }`
 * @see OptionalOnceProperty
 *
 * @param T is [Nullable]
 * @param getBackingResourceId a lambda that returns a NULLABLE [Int] id of the Resource that will be resolved for be
 * used in case the Property has not been set
 * E.g. >
@get: StringRes var channelIdRes: Int? = null
var channelId: String by optional { channelIdRes }`
 * Default is a lambda that returns `null`
 *
 * @author Davide Giuseppe Farella
 */
internal inline fun <reified T : Nullable> optionalOnce( noinline getBackingResourceId: () -> Int? = { null } ) =
    OptionalOnceProperty<T>( T::class, getBackingResourceId )

/**
 * Delegation function for [RequiredOnceProperty]
 * E.g. `private var someValue: String? by requiredOnce()` or `... by requiredOnce { someBackingResourceId }`
 * @see RequiredOnceProperty
 *
 * @param T is [NotNullable]
 * @param getBackingResourceId a lambda that returns a NULLABLE [Int] id of the Resource that will be resolved for be
 * used in case the Property has not been set
 * E.g. >
@get: StringRes var channelIdRes: Int? = null
var channelId: String by required { channelIdRes }`
 * Default is a lambda that returns `null`
 *
 * @author Davide Giuseppe Farella
 */
internal inline fun <reified T : NotNullable> requiredOnce( noinline getBackingResourceId: () -> Int? = { null } ) =
    RequiredOnceProperty<T>( T::class, getBackingResourceId )


/** A typealias of a NOT Nullable variant of [Any]. For readability purpose only */
private typealias NotNullable = Any

/** A typealias of a Nullable variant of [Any]. For readability purpose only */
private typealias Nullable = Any?

/**
 * An abstraction on [ReadWriteProperty] that defines a common way of [setValue]
 *
 * @param tClass [KClass] for [T] it is needed for call [Resources.get], since [T] cannot be inlined
 *
 * @param getBackingResourceId a lambda that returns a NULLABLE [Int] id of the Resource that will be resolved for be
 * used in case the Property has not been set
 * @see optional
 * @see required
 */
internal abstract class BaseProperty<T>(
    private val tClass: KClass<*>,
    private val getBackingResourceId: () -> Int?
) : ReadWriteProperty<ResourcedBuilder, T> {

    /** A backing field for the [ReadWriteProperty] */
    private var field: T? = null

    /** @return [Boolean] whether [field] has been set */
    protected fun isFieldSet() = field != null

    /** @return [field] if not `null`, else resolve the value of [getBackingResourceId] */
    protected fun getField( resourcedBuilder: ResourcedBuilder ) =
        field ?: getBackingResourceId()?.let { resourcedBuilder.resources[tClass, it] }

    /** Set the given [value] [T] to [field] */
    override fun setValue( thisRef: ResourcedBuilder, property: KProperty<*>, value: T ) {
        field = value
    }
}

/**
 * A [ReadWriteProperty] that allows the value to be `null` on [getValue]
 * Inherit from [BaseProperty]
 * @param T is [Nullable]
 */
internal open class OptionalProperty<T : Nullable>(
    tClass: KClass<*>,
    getBackingResourceId: () -> Int?
) : BaseProperty<T>( tClass, getBackingResourceId ) {

    /** @return [BaseProperty.getField] */
    @Suppress("UNCHECKED_CAST") // T == T?, since T is Nullable itself
    override fun getValue( thisRef: ResourcedBuilder, property: KProperty<*> ) = getField( thisRef ) as T
}

/**
 * A [ReadWriteProperty] that allows the value to be `null` on [getValue] and to call [setValue] more that once
 * Inherit from [OptionalProperty]
 * @param T is [Nullable]
 *
 * @throws PropertyAlreadySetException
 */
internal class OptionalOnceProperty<T : Nullable>(
    tClass: KClass<*>,
    getBackingResourceId: () -> Int?
) : OptionalProperty<T>( tClass, getBackingResourceId ) {

    /**
     * Set the value if [isFieldSet] is `false`
     * @throws PropertyAlreadySetException if [isFieldSet]
     */
    override fun setValue( thisRef: ResourcedBuilder, property: KProperty<*>, value: T ) {
        if ( isFieldSet() ) throw PropertyAlreadySetException( thisRef::class, property )
        else super.setValue( thisRef, property, value )
    }
}

/**
 * A [ReadWriteProperty] that DOESN'T allows the value to be `null` on [getValue]
 * Inherit from [BaseProperty]
 * @param T is [NotNullable]
 *
 * @throws RequiredPropertyNotSetException
 */
internal open class RequiredProperty<T : NotNullable>(
    tClass: KClass<*>,
    getBackingResourceId: () -> Int?
) : BaseProperty<T>( tClass, getBackingResourceId ) {

    /**
     * @return [BaseProperty.getField] if not `null`, else
     * @throws RequiredPropertyNotSetException
     */
    @Suppress("UNCHECKED_CAST") // Not really unchecked, since we are using safe-cast ( `as?` )
    override fun getValue( thisRef: ResourcedBuilder, property: KProperty<*> ) =
        ( getField( thisRef ) as? T ) ?: throw RequiredPropertyNotSetException( thisRef::class, property )
}

/**
 * A [ReadWriteProperty] that DOESN'T allows the value to be `null` on [getValue] and to call [setValue] more that once
 * Inherit from [RequiredProperty]
 * @param T is [NotNullable]
 *
 * @throws PropertyAlreadySetException
 * @throws RequiredPropertyNotSetException
 */
internal class RequiredOnceProperty<T : NotNullable> (
    tClass: KClass<*>,
    getBackingResourceId: () -> Int?
) : RequiredProperty<T>( tClass, getBackingResourceId ) {

    /**
     * Set the value if [isFieldSet] is `false`
     * @throws PropertyAlreadySetException if [isFieldSet]
     */
    override fun setValue( thisRef: ResourcedBuilder, property: KProperty<*>, value: T ) {
        if ( isFieldSet() ) throw PropertyAlreadySetException( thisRef::class, property )
        else super.setValue( thisRef, property, value )
    }
}


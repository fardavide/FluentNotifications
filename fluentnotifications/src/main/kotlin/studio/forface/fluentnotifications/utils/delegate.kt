package studio.forface.fluentnotifications.utils

import studio.forface.fluentnotifications.RequiredPropertyNotSetException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Delegation function for [OptionalProperty]
 * E.g. `private var someValue: String? by optional()`
 *
 * @param T is [Nullable]
 * @param getBackingResourceId a lambda that returns the id of the Resource that will be resolved for be used in case
 * the Property has not been set
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
 * Delegation function for [RequiredProperty].
 *
 * @param T is [NotNullable]
 * @param getBackingResourceId a lambda that returns the id of the Resource that will be resolved for be used in case
 * the Property has not been set
 * E.g. >
    @get: StringRes var channelIdRes: Int? = null
    var channelId: String by required { channelIdRes }`
 * Default is a lambda that returns `null`
 *
 * @author Davide Giuseppe Farella
 */
internal inline fun <reified T : NotNullable> required( noinline getBackingResourceId: () -> Int? = { null } ) =
    RequiredProperty<T>( T::class, getBackingResourceId )


/** A typealias of a NOT Nullable variant of [Any]. For readability purpose only */
private typealias NotNullable = Any

/** A typealias of a Nullable variant of [Any]. For readability purpose only */
private typealias Nullable = Any?

/** An abstraction on [ReadWriteProperty] that defines a common way of [setValue] */
internal abstract class BaseProperty<T>(
    private val tClass: KClass<*>,
    private val getBackingResourceId: () -> Int?
) : ReadWriteProperty<ResourcedBuilder, T> {

    /** A backing field for the [ReadWriteProperty] */
    private var field: T? = null

    /** @return [field] if not `null`, else resolve the value of [getBackingResourceId] */
    fun getField( resourcedBuilder: ResourcedBuilder ) =
        field ?: getBackingResourceId()?.let { resourcedBuilder.resources[tClass, it] }

    /** Set the given [value] [T] to [field] */
    override fun setValue( thisRef: ResourcedBuilder, property: KProperty<*>, value: T ) {
        field = value
    }
}

/**
 * A [ReadWriteProperty] that allows the value to be `null` on [getValue]
 * @param T is [Nullable]
 */
internal class OptionalProperty<T : Nullable>(
    tClass: KClass<*>,
    getBackingResourceId: () -> Int?
) : BaseProperty<T>( tClass, getBackingResourceId ) {

    /** @return [BaseProperty.getField] */
    @Suppress("UNCHECKED_CAST") // T == T?, since T is Nullable itself
    override fun getValue( thisRef: ResourcedBuilder, property: KProperty<*> ) = getField( thisRef ) as T
}

/**
 * A [ReadWriteProperty] that DOESN'T allows the value to be `null` on [getValue]
 * @param T is [NotNullable]
 * @throws [RequiredPropertyNotSetException]
 */
internal class RequiredProperty<T : NotNullable>(
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
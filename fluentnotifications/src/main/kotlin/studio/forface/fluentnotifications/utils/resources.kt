package studio.forface.fluentnotifications.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import studio.forface.fluentnotifications.IllegalResourceException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * An interface for Builders that provides [Resources]
 *
 * @author Davide Giuseppe Farella
 */
internal interface ResourcedBuilder {

    /** @return [Resources] */
    val resources: Resources
}

/** A [ReadOnlyProperty] that provides a [Resources] */
internal class ResourcesProperty : ReadOnlyProperty<Any, Resources> {

    /** A reference to [Resources] */
    private val resources: Resources

    /** Constructor that takes a [Context] for provide [Resources] */
    constructor( context: Context ) {
        resources = context.resources
    }

    /** Constructor that takes a [Resources] directly */
    constructor( resources: Resources ) {
        this.resources = resources
    }

    /**
     * @return [Resources]
     * @see ResourcesProperty.resources
     */
    override fun getValue( thisRef: Any, property: KProperty<*> ) = resources
}

/**
 * @return an anonymous [ResourcedBuilder] overriding [ResourcedBuilder.resources] by a [ResourcesProperty] that takes
 * the receiver [Context] as constructor parameter.
 * E.g. `class SomeBuilder( context: Context ): ResourcesBuilder by context()`
 */
internal operator fun Context.invoke() = object : ResourcedBuilder {
    override val resources: Resources by ResourcesProperty( this@invoke )
}

/**
 * If the receiver [Int] is not `null`, try to use it as `IntegerRes` for get an [Int] from [Resources], else return
 * itself.
 *
 * @param resources [Resources] for try to get [Int] from Resources
 */
internal fun Int?.resourceOrSelf( resources: Resources ): Int? {
    return if ( this == null ) null
    else handle( this ) { resources.getInteger( this ) }
}

/**
 * @return an anonymous [ResourcedBuilder] overriding [ResourcedBuilder.resources] by a [ResourcesProperty] that takes
 * the receiver [Resources] as constructor parameter.
 * E.g. `class SomeBuilder( resources ): ResourcesBuilder by resource()`
 */
internal operator fun Resources.invoke() = object : ResourcedBuilder {
    override val resources: Resources by ResourcesProperty( this@invoke )
}

/**
 * @return a value of type [T] with the given [resourceId] from [Resources]
 * @throws IllegalResourceException if it's impossible to resolve a resource for the given [KClass] type
 */
@PublishedApi
internal inline operator fun <reified T : Any> Resources.get( resourceId: Int ) = get( T::class, resourceId )

/** TODO implements other resource's types
 * @return a value of type [T] with the given [resourceId] from [Resources]
 * @throws IllegalResourceException if it's impossible to resolve a resource for the given [KClass] type
 */
@PublishedApi
internal operator fun <T : Any> Resources.get( tClass: KClass<T>, resourceId: Int ) : T {
    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY") // Cannot have a checked cast since T is Any
    return when ( tClass ) {

        /* String and Numbers */
        CharSequence::class -> getText( resourceId )
        Int::class -> getInteger( resourceId )

        /* Images */
        Drawable::class ->
            @Suppress("DEPRECATION") // Theme actually not supported
            getDrawable( resourceId )

        else -> throw IllegalResourceException( tClass )
    } as T
}
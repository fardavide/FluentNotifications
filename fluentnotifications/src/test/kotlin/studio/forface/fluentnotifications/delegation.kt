package studio.forface.fluentnotifications

import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import studio.forface.fluentnotifications.utils.ResourcedBuilder
import studio.forface.fluentnotifications.utils.invoke
import studio.forface.fluentnotifications.utils.optional
import studio.forface.fluentnotifications.utils.required
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class DelegationTest : ResourcedBuilder by mockk() {

    private var optional by optional<Int?>()
    private var required by required<Int>()

    @Test
    fun `optional notNull setGet right value`() {
        optional = 4
        assertEquals( 4, optional )
    }

    @Test
    fun `optional null setGet null`() {
        optional = null
        assertNull( optional )
    }

    @Test
    fun `required notNull setGet right value`() {
        required = 5
        assertEquals( 5, required )
    }

    @Test( expected = RequiredPropertyNotSetException::class )
    fun `required null get_throwException`() {
        required
    }
}

private val mockResources = mockk<Resources> {
    every { getString( any() ) } answers { firstArg<Any>().toString() }
}

internal class DelegationWithBackingResourceTest : ResourcedBuilder by mockResources() {

    private var optionalRes: Int? = null
    private var optional by optional<Int?> { optionalRes }

    private var requiredRes: Int? = null
    private var required by required<Int> { requiredRes }

    @Test
    fun `optional notNull setGet right value`() {
        optional = 4
        optionalRes = 2
        assertEquals( 4, optional )
    }

    @Test
    fun `optional null with notNull backingResource get_return backingResource`() {
        optionalRes = 5
        assertEquals( 5, optional )
    }

    @Test
    fun `optional null with null backingResource get_return null`() {
        assertNull( optional )
    }

    @Test
    fun `required notNull setGet right value`() {
        required = 5
        requiredRes = 3
        assertEquals( 5, required )
    }

    @Test
    fun `required null with notNull backingResource get_return backingResource`() {
        requiredRes = 6
        assertEquals( 6, required )
    }

    @Test( expected = RequiredPropertyNotSetException::class )
    fun `required null with null backingResource get_throwException`() {
        required
    }
}
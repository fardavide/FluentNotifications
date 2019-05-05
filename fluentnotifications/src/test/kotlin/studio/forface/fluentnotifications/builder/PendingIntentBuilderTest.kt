package studio.forface.fluentnotifications.builder

import android.app.Service
import android.content.BroadcastReceiver
import androidx.fragment.app.FragmentActivity
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class PendingIntentBuilderTest {

    class MyTestActivity: FragmentActivity()
    abstract class MyTestReceiver: BroadcastReceiver()
    abstract class MyTestService: Service()

    @Test
    fun `start runs correctly with supported types`() {
        fun newBuilder() = PendingIntentBuilder(
            mockk( relaxed = true ),
            { _, _, _, _, _ -> mockk() },
            { _, _, _, _, _ -> mockk() },
            { _, _, _, _ -> mockk() },
            { _, _, _, _ -> mockk() },
            { _, _, _, _ -> mockk() }
        )
        newBuilder().start<MyTestActivity>()
        newBuilder().start<MyTestReceiver>()
        newBuilder().start<MyTestService>()
    }
}
@file:Suppress("EXPERIMENTAL_API_USAGE")

package studio.forface.fluentnotifications.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.ticker

class DemoActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + Main

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_demo )
        setSupportActionBar( toolbar )

        val validationTicker = ticker( VALIDATE_FORM_INTERVAL_MS )
        launch {
            validationTicker.consumeEach {
                showNotificationButton.isEnabled = validateForm()
            }
        }

        showNotificationButton.setOnClickListener {
            onShowNotification( withDelay = delayCheckBox.isChecked )
        }
    }

    private fun onShowNotification( withDelay: Boolean ) {
        launch {
            if ( withDelay ) delay( NOTIFICATION_DELAY_MS )

            //showNotification()
        }
    }

    private val notificationTitle get() = notificationTitleInput.text as CharSequence

    private suspend fun validateForm() = coroutineScope {
        withContext( Default ) {
            notificationTitle.isNotBlank()
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}

/** Default delay in milliseconds before to show the notification */
const val NOTIFICATION_DELAY_MS = 3000L

/** Interval in milliseconds between the form validation */
const val VALIDATE_FORM_INTERVAL_MS = 100L
@file:Suppress("EXPERIMENTAL_API_USAGE")

package studio.forface.fluentnotifications.demo

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.ticker
import studio.forface.fluentnotifications.builder.channel
import studio.forface.fluentnotifications.showNotification
import kotlin.random.Random
import kotlin.random.nextUInt

class DemoActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + Main

    private val requiredFields get() = listOf(
        notificationIdInput,
        channelIdInput,
        channelNameInput,
        notificationTitleInput
    )

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_demo )
        setSupportActionBar( toolbar )

        // Set a random id for notification
        notificationIdInput.setText( Random.nextUInt().toString() )

        val validationTicker = ticker( VALIDATE_FORM_INTERVAL_MS )
        launch {
            validationTicker.consumeEach {
                showNotificationButton.isEnabled = validateForm()
                //showNotificationButton.isClickable = true
            }
        }

        showNotificationButton.setOnClickListener {
            if ( validateFormOrFocus() ) {
                onShowNotification( withDelay = delayCheckBox.isChecked )
            }
        }
    }

    private fun onShowNotification( withDelay: Boolean ) {
        launch {
            if ( withDelay ) delay( NOTIFICATION_DELAY_MS )

            showNotification( notificationId, notificationTag ) {

                channel( channelId, channelName )

                notification {
                    smallIconRes = R.drawable.ic_notification_chat
                    title = notificationTitle
                    contentText = notificationContent
                }
            }
        }
    }

    private val notificationId get() =      notificationIdInput.text.toString().toInt()
    private val notificationTag get() =     notificationTagInput.text as CharSequence
    private val channelId get() =           channelIdInput.text as CharSequence
    private val channelName get() =         channelNameInput.text as CharSequence
    private val notificationTitle get() =   notificationTitleInput.text as CharSequence
    private val notificationContent get() = notificationContentInput.text as CharSequence

    /** @return `true` if none of [requiredFields] is blank */
    private suspend fun validateForm() = coroutineScope {
        withContext( Default ) {
            requiredFields.find { it.isBlank() } == null
        }
    }

    /** @return `true` if none of [requiredFields] is blank, else focus the first blank field */
    private fun validateFormOrFocus() = requiredFields.find { it.focusIfBlank() } == null

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    /** @return `true` if [EditText.getText] is blank */
    private fun EditText.isBlank() = text.isBlank()

    /** @return `true` if [EditText.getText] is blank. Also [EditText.requestFocus] if blank */
    private fun EditText.focusIfBlank() : Boolean {
        return if ( text.isBlank() ) {
            requestFocus()
            true
        } else false
    }
}

/** Default delay in milliseconds before to show the notification */
const val NOTIFICATION_DELAY_MS = 3000L

/** Interval in milliseconds between the form validation */
const val VALIDATE_FORM_INTERVAL_MS = 100L
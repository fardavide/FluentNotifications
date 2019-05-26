@file:Suppress("EXPERIMENTAL_API_USAGE")

package studio.forface.fluentnotifications.demo

import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.ticker
import studio.forface.fluentnotifications.builder.channel
import studio.forface.fluentnotifications.demo.DemoActivity.ValueWrapper.Constructors.int
import studio.forface.fluentnotifications.demo.DemoActivity.ValueWrapper.Constructors.string
import studio.forface.fluentnotifications.demo.DemoActivity.ValueWrapper.IntValue
import studio.forface.fluentnotifications.demo.DemoActivity.ValueWrapper.StringValue
import studio.forface.fluentnotifications.showNotification
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.reflect.KMutableProperty

class DemoActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + Main

    /** List of required field for show a Notification */
    private val requiredFields get() = listOf(
        notificationIdInput,
        channelIdInput,
        channelNameInput,
        notificationTitleInput
    )

    /** List of required field for create a Group */
    private val groupFields get() = listOf(
        groupIdInput,
        groupTitleInput
    )

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_demo )
        setSupportActionBar( toolbar )

        restoreForm()

        setNewRandomNotificationId()
        // Set a random id for Group just once, since we want to update the same group every time
        groupId = Random.nextInt().absoluteValue

        val validationTicker = ticker( VALIDATE_FORM_INTERVAL_MS )
        launch {
            validationTicker.consumeEach {
                showNotificationButton.isEnabled = validateForm()
            }
        }

        groupLayout.isVisible = useGroupCheckbox.isChecked
        useGroupCheckbox.setOnCheckedChangeListener { _, checked -> groupLayout.isVisible = checked }

        showNotificationButton.setOnClickListener {
            setNewRandomNotificationId()
            onShowNotification( withDelay = delayCheckBox.isChecked )
        }
    }

    private fun onShowNotification( withDelay: Boolean ) {
        launch {
            if ( withDelay ) delay( NOTIFICATION_DELAY_MS )

            /*= = = = = = = = = = = = = = = == = = = = = = = = = = = = = = =
             * = = = = = = = = = = FLUENT NOTIFICATIONS = = = = = = = = = =
             *= = = = = = = = = = = = = = = == = = = = = = = = = = = = = = =*/
            showNotification( notificationId, notificationTag ) {

                channel( channelId, channelName )

                notification {
                    smallIconRes = R.drawable.ic_notification_chat
                    title = notificationTitle
                    contentText = notificationContent
                }

                if ( useGroupCheckbox.isChecked ) {
                    groupBy( groupId, groupTag ) {
                        title = groupTitle
                        contentText = groupContent
                    }
                }
            }
            /*= = = = = = = = = = = = = = = == = = = = = = = = = = = = = = =
              * = = = = = = = = = = FLUENT NOTIFICATIONS = = = = = = = = = =
              *= = = = = = = = = = = = = = = == = = = = = = = = = = = = = = =*/
        }
    }

    // region inputs
    private val form = mapOf(
        "notificationId" to int( ::notificationId ),
        "notificationTag" to string( ::notificationTag ),
        "channelId" to string( ::channelId ),
        "channelName" to string( ::channelName ),
        "notificationTitle" to string( ::notificationTitle ),
        "notificationContent" to string( ::notificationContent ),
        "groupId" to int( ::groupId ),
        "groupTag" to string( ::groupTag ),
        "groupTitle" to string( ::groupTitle ),
        "groupContent" to string( ::groupContent )
    )
    sealed class ValueWrapper<T>( private val prop: KMutableProperty<T> ) {
        /** @return the value [T] from [prop] */
        fun get() = prop.getter.call()
        /** Set the given [value] into [prop] */
        fun set(value: T) {
            prop.setter.call( value )
        }

        companion object Constructors {
            fun string( prop: KMutableProperty<CharSequence> ) = StringValue( prop )
            fun int( prop: KMutableProperty<Int> ) = IntValue( prop )
        }

        class StringValue( prop: KMutableProperty<CharSequence> ) : ValueWrapper<CharSequence>( prop )
        class IntValue( prop: KMutableProperty<Int> ) : ValueWrapper<Int>( prop )
    }

    @Suppress("MemberVisibilityCanBePrivate") var notificationId
        get() = notificationIdInput.text.toString().toInt()
        set( value ) = notificationIdInput.setText( value.toString() )

    @Suppress("MemberVisibilityCanBePrivate") var notificationTag // TODO convert to ReadWriteProperty
        get() = notificationTagInput.text as CharSequence
        set( value ) = notificationTagInput.setText( value.toString() )

    @Suppress("MemberVisibilityCanBePrivate") var channelId // TODO convert to ReadWriteProperty
        get() = channelIdInput.text as CharSequence
        set( value ) = channelIdInput.setText( value.toString() )

    @Suppress("MemberVisibilityCanBePrivate") var channelName // TODO convert to ReadWriteProperty
        get() = channelNameInput.text as CharSequence
        set( value ) = channelNameInput.setText( value.toString() )

    @Suppress("MemberVisibilityCanBePrivate") var notificationTitle // TODO convert to ReadWriteProperty
        get() = notificationTitleInput.text as CharSequence
        set( value ) = notificationTitleInput.setText( value.toString() )

    @Suppress("MemberVisibilityCanBePrivate") var notificationContent // TODO convert to ReadWriteProperty
        get() = notificationContentInput.text as CharSequence
        set( value ) = notificationContentInput.setText( value.toString() )

    @Suppress("MemberVisibilityCanBePrivate") var groupId // TODO convert to ReadWriteProperty
        get() = groupIdInput.text.toString().toInt()
        set( value ) = groupIdInput.setText( value.toString() )

    @Suppress("MemberVisibilityCanBePrivate") var groupTag // TODO convert to ReadWriteProperty
        get() = groupTagInput.text as CharSequence
        set( value ) = groupTagInput.setText( value.toString() )

    @Suppress("MemberVisibilityCanBePrivate") var groupTitle // TODO convert to ReadWriteProperty
        get() = groupTitleInput.text as CharSequence
        set( value ) = groupTitleInput.setText( value.toString() )

    @Suppress("MemberVisibilityCanBePrivate") var groupContent // TODO convert to ReadWriteProperty
        get() = groupContentInput.text as CharSequence
        set( value ) = groupContentInput.setText( value.toString() )
    // endregion

    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences( applicationContext ) }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    /** Set a random id for notification */
    private fun setNewRandomNotificationId() {
        notificationId = Random.nextInt().absoluteValue
    }

    /** @return `true` if none of [requiredFields] is blank */
    private suspend fun validateForm() = coroutineScope {
        withContext( Default ) {
            saveForm()
            requiredFields.noBlank() && ( ! useGroupCheckbox.isChecked || groupFields.noBlank() )
        }
    }

    /** Save value of the form in [preferences] */
    private fun saveForm() {
        val editor = preferences.edit()
        form.forEach { (name, value) ->
            when ( value ) {
                is StringValue -> editor.putString( name, value.get().toString() )
                is IntValue -> editor.putInt( name, value.get())
            }.exhaustive
        }
        editor.apply()
    }

    /** Populate the form from [preferences] */
    private fun restoreForm() {
        form.forEach { (name, value) ->
            when( value ) {
                is StringValue -> value.set(preferences.getString( name, EMPTY_STRING )!!)
                is IntValue -> value.set(preferences.getInt( name, 0 ))
            }
        }
    }

    /** @return `true` if [EditText.getText] is blank */
    private fun EditText.isBlank() = text.isBlank()

    /** @return `true` if none of the [EditText] is blank */
    private fun List<EditText>.noBlank() = none { it.isBlank() }
}

/** Default delay in milliseconds before to show the notification */
const val NOTIFICATION_DELAY_MS = 3000L

/** Interval in milliseconds between the form validation */
const val VALIDATE_FORM_INTERVAL_MS = 100L

const val EMPTY_STRING = ""
private val Any.exhaustive get() = this
import studio.forface.easygradle.dsl.android.Version

/**
 * An object containing params for the Library
 *
 * @author Davide Giuseppe Farella
 */
object Project {

    val version = Version(0, 1, Version.Channel.Alpha, 6, 1)
    const val targetSdk = 28
    const val minSdk = 16
}

object Module {
    const val fluentNotifications = ":fluentnotifications"
}
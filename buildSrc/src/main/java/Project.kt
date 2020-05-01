import studio.forface.easygradle.dsl.android.*
import studio.forface.easygradle.dsl.android.Version.Channel.*

/**
 * An object containing params for the Library
 *
 * @author Davide Giuseppe Farella
 */
object Project {

    val version = Version(0, 2, Alpha, 3, 0)
    const val targetSdk = 28
    const val minSdk = 16
}

object Module {
    const val fluentNotifications = ":fluentnotifications"
}

package studio.forface.fluentnotifications.utils

import android.os.Build

/**
 * An object for run checks on Android versions
 *
 * @author Davide Giuseppe Farella
 */
internal object Android {

    /** @return [Boolean] whether the current SDK if equals of greater that Android Lollipop */
    val LOLLIPOP get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    /** @return [Boolean] whether the current SDK if equals of greater that Android Marshmallow */
    val MARSHMALLOW get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    /** @return [Boolean] whether the current SDK if equals of greater that Android Oreo */
    val OREO get() =  Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /** @return [Boolean] whether the current SDK if equals of greater that Android Pie */
    val PIE get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
}
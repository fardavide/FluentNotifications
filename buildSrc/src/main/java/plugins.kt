import org.gradle.api.artifacts.dsl.DependencyHandler
import studio.forface.easygradle.dsl.`kotlin-gradle-plugin`
import studio.forface.easygradle.dsl.android.`android-gradle-plugin`

val DependencyHandler.`kotlin-gradle-plugin` get() = `kotlin-gradle-plugin`
val DependencyHandler.`android-gradle-plugin` get() = `android-gradle-plugin`
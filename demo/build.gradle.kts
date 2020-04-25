import studio.forface.easygradle.dsl.`coroutines-android`
import studio.forface.easygradle.dsl.`kotlin-jdk7`
import studio.forface.easygradle.dsl.`kotlin-reflect`
import studio.forface.easygradle.dsl.android.`android-ktx`
import studio.forface.easygradle.dsl.android.`constraint-layout`
import studio.forface.easygradle.dsl.android.appcompat
import studio.forface.easygradle.dsl.android.material
import studio.forface.easygradle.dsl.implementation

android("studio.forface.fluentnotifications.demo")

dependencies {
    implementation(project(
        Module.fluentNotifications),

        // Kotlin
        `kotlin-jdk7`,
        `kotlin-reflect`,
        `coroutines-android`,

        // Android
        `appcompat`,
        `constraint-layout`,
        `android-ktx`,
        `material`)
}

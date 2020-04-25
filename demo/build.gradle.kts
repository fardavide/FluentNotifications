import studio.forface.easygradle.dsl.`coroutines-android`
import studio.forface.easygradle.dsl.`kotlin-jdk7`
import studio.forface.easygradle.dsl.`kotlin-reflect`
import studio.forface.easygradle.dsl.android.*
import studio.forface.easygradle.dsl.implementation

plugins {
    id( "com.android.application" )
    id( "kotlin-android" )
    id( "kotlin-android-extensions" )
}

android { applyAndroidConfig( "studio.forface.fluentnotifications.demo" ) }

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

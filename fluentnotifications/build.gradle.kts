import studio.forface.easygradle.dsl.*
import studio.forface.easygradle.dsl.android.`android-ktx`
import studio.forface.easygradle.dsl.android.android
import studio.forface.easygradle.dsl.android.appcompat
import studio.forface.easygradle.dsl.android.publishAndroid

plugins {
    id( "com.android.library" )
    id( "kotlin-android" )
    id( "kotlin-android-extensions" )
}

android { applyAndroidConfig() }

dependencies {
    implementation(

        // Kotlin
        `kotlin-jdk7`,
        `kotlin-reflect`,

        // Android
        `appcompat`,
        `android-ktx`
    )
    testImplementation(
        `kotlin-test`,
        `kotlin-test-junit`,
        `mockk-android`
    )
}

dokka()
publishAndroid()

import studio.forface.easygradle.dsl.*
import studio.forface.easygradle.dsl.android.`android-ktx`
import studio.forface.easygradle.dsl.android.`android-work-runtime`
import studio.forface.easygradle.dsl.android.appcompat
import studio.forface.easygradle.dsl.android.publishAndroid

android()

dependencies {
    implementation(

        // Kotlin
        `kotlin-jdk7`,
        `kotlin-reflect`,

        // Android
        `appcompat`,
        `android-ktx`
    )
    compileOnly(`android-work-runtime`)

    testImplementation(
        `kotlin-test`,
        `kotlin-test-junit`,
        `mockk-android`
    )
}

dokka()
publishAndroid {
    version = Project.version.versionName
}

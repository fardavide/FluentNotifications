@file:Suppress("MayBeConstant")

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.ScriptHandlerScope
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.maven

val repos: RepositoryHandler.() -> Unit get() = {
    google()
    jcenter()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx" )
    // mavenLocal()
}

val ScriptHandlerScope.classpathDependencies: DependencyHandlerScope.() -> Unit get() = {
    classpath( kotlin("gradle-plugin", Versions.kotlin) )
    classpath("com.android.tools.build:gradle:${Versions.android_gradle_plugin}" )
    classpath( Libs.Publishing.dokka_plugin )
    classpath( Libs.Publishing.bintray_plugin )
    classpath( Libs.Publishing.maven_plugin )
}

fun DependencyHandler.applyTests() = Libs.run {
    listOf( test, test_junit, mockk )
            .forEach { add("testImplementation", it ) }
}

fun DependencyHandler.applyAndroidTests() {
    Libs.Android.run {
        listOf( robolectric )
            .forEach { add( "testImplementation", it ) }
    }
    Libs.run {
        listOf( test, test_junit, mockk_android )
            .forEach { add("androidTestImplementation", it ) }
    }
    Libs.Android.run {
        listOf( espresso, test_core, test_rules, test_runner )
            .forEach { add( "androidTestImplementation", it ) }
    }
}

object Versions {

    /* Kotlin */
    val kotlin =                        "1.3.30"
    val coroutines =                    "1.2.0"

    /* Base */
    val mockk =                         "1.9.3"

    /* Android */
    val android_espresso =              "3.1.1"
    val android_gradle_plugin =         "3.3.0"
    val android_ktx =                   "1.1.0-alpha05"
    val android_robolectric =           "4.2.1"
    val android_support =               "1.1.0-alpha02"
    val android_test_core =             "1.1.0"
    val android_test_runner =           "1.1.1"

    /* Publishing */
    val publishing_bintray_plugin =     "1.8.4"
    val publishing_dokka_plugin =       "0.9.18"
    val publishing_maven_plugin =       "2.1"
}

@Suppress("unused")
object Libs {

    /* Kotlin */
    val kotlin =                                "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val coroutines =                            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutines_android =                    "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val reflect =                               "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    val test =                                  "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    val test_junit =                            "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"

    /* Base */
    val mockk =                                 "io.mockk:mockk:${Versions.mockk}"
    val mockk_android =                         "io.mockk:mockk-android:${Versions.mockk}"

    /* Android */
    object Android {
        val appcompat =                         "androidx.appcompat:appcompat:${Versions.android_support}"
        val espresso =                          "androidx.test.espresso:espresso-core:${Versions.android_espresso}"
        val ktx =                               "androidx.core:core-ktx:${Versions.android_ktx}"
        val robolectric =                       "org.robolectric:robolectric:${Versions.android_robolectric}"
        val test_core =                         "androidx.test:core:${Versions.android_test_core}"
        val test_rules =                        "androidx.test:rules:${Versions.android_test_runner}"
        val test_runner =                       "androidx.test:runner:${Versions.android_test_runner}"
    }

    /* Publishing */
    object Publishing {
        val bintray_plugin =                    "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.publishing_bintray_plugin}"
        val dokka_plugin =                      "org.jetbrains.dokka:dokka-android-gradle-plugin:${Versions.publishing_dokka_plugin}"
        val maven_plugin =                      "com.github.dcendents:android-maven-gradle-plugin:${Versions.publishing_maven_plugin}"
    }
}
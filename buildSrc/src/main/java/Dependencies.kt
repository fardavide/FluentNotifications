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
    classpath( kotlin("gradle-plugin", Version.kotlin) )
    classpath("com.android.tools.build:gradle:${Version.android_gradle_plugin}" )
    classpath( Lib.Publishing.dokka_plugin )
    classpath( Lib.Publishing.bintray_plugin )
    classpath( Lib.Publishing.maven_plugin )
}

fun DependencyHandler.applyTests() = Lib.run {
    listOf( test, test_junit, mockk )
            .forEach { add("testImplementation", it ) }
}

fun DependencyHandler.applyAndroidTests() {
    Lib.run {
        listOf( test, test_junit, mockk_android )
            .forEach { add("androidTestImplementation", it ) }
    }
    Lib.Android.run {
        listOf( espresso, test_core, test_rules, test_runner )
            .forEach { add( "androidTestImplementation", it ) }
    }
}

object Version {

    /* Kotlin */
    const val kotlin =                          "1.3.31"        // Updated: Apr 25, 2019
    const val coroutines =                      "1.2.1"         // Updated: Apr 25, 2019

    /* Base */
    const val mockk =                           "1.9.3"         // Updated: Mar 25, 2019

    /* Android */
    const val android_constraintLayout =        "2.0.0-beta1"   // Updated: May 8, 2019
    const val android_espresso =                "3.2.0"         // Updated: May 30, 2019
    const val android_gradle_plugin =           "3.5.0-beta03"  // Updated: May 28, 2019
    const val android_ktx =                     "1.1.0-beta01"  // Updated: May 8, 2019
    const val android_material =                "1.1.0-alpha07" // Updated: May 31, 2019
    const val android_support =                 "1.0.2"         // Updated: Nov 17, 2018
    const val androidx_test =                   "1.2.0"         // Updated: May 31, 2019

    /* Publishing */
    const val publishing_bintray_plugin =       "1.8.4"         // Updated:
    const val publishing_dokka_plugin =         "0.9.18"        // Updated: Mar 19, 2019
    const val publishing_maven_plugin =         "2.1"           // Updated:
}

object Lib {

    /* Kotlin */
    const val kotlin =                              "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlin}"
    const val coroutines_android =                  "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
    const val reflect =                             "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlin}"
    const val test =                                "org.jetbrains.kotlin:kotlin-test:${Version.kotlin}"
    const val test_junit =                          "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}"

    /* Base */
    const val mockk =                               "io.mockk:mockk:${Version.mockk}"
    const val mockk_android =                       "io.mockk:mockk-android:${Version.mockk}"

    /* Android */
    object Android {
        const val appcompat =                       "androidx.appcompat:appcompat:${Version.android_support}"
        const val constraintLayout =                "androidx.constraintlayout:constraintlayout:${Version.android_constraintLayout}"
        const val espresso =                        "androidx.test.espresso:espresso-core:${Version.android_espresso}"
        const val ktx =                             "androidx.core:core-ktx:${Version.android_ktx}"
        const val material =                        "com.google.android.material:material:${Version.android_material}"
        const val test_core =                       "androidx.test:core:${Version.androidx_test}"
        const val test_rules =                      "androidx.test:rules:${Version.androidx_test}"
        const val test_runner =                     "androidx.test:runner:${Version.androidx_test}"
    }

    /* Publishing */
    object Publishing {
        const val bintray_plugin =                  "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Version.publishing_bintray_plugin}"
        const val dokka_plugin =                    "org.jetbrains.dokka:dokka-android-gradle-plugin:${Version.publishing_dokka_plugin}"
        const val maven_plugin =                    "com.github.dcendents:android-maven-gradle-plugin:${Version.publishing_maven_plugin}"
    }
}

object Module {
    const val fluentNotifications = ":fluentnotifications"
}
import com.android.build.gradle.TestedExtension
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.apply
import studio.forface.easygradle.dsl.android.version

fun org.gradle.api.Project.android(

    appId: String? = null
    // appIdSuffix: String? = null,
    // version: Version? = null,
    // minSdk: Int = 23,
    // targetSdk: Int = 29

) {
    if (appId != null) apply(plugin = "com.android.application")
    else apply(plugin = "com.android.library")
    apply(plugin = "kotlin-android")
    apply(plugin = "kotlin-android-extensions")

    extensions.configure<TestedExtension>("android") {
        compileSdkVersion(Project.targetSdk)
        defaultConfig {

            appId?.let { applicationId = appId }
            version = Project.version

            minSdkVersion(Project.minSdk)
            targetSdkVersion(Project.targetSdk)

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
        }
        sourceSets {
            getByName("main").java.srcDirs("src/main/kotlin")
            getByName("test").java.srcDirs("src/test/kotlin")
            getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = sourceCompatibility
        }
        packagingOptions {
            exclude("META-INF/atomicfu.kotlin_module")
        }
    }
}

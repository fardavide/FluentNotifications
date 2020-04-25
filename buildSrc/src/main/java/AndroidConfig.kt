import com.android.build.gradle.TestedExtension
import org.gradle.api.JavaVersion
import studio.forface.easygradle.dsl.android.version

fun TestedExtension.applyAndroidConfig( appId: String? = null ) {
    compileSdkVersion(Project.targetSdk)
    defaultConfig {

        appId?.let { applicationId = it }
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
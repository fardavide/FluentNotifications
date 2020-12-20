plugins {
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
}

dependencies {
    val androidGradlePlugin =   "4.0.0"         // Released: May 23, 2020
    val dokka =                 "0.10.1"        // Released: Feb 04, 2020
    val easyGradle =            "1.3.2"         // Released: May 21, 2020

    implementation("com.android.tools.build:gradle:$androidGradlePlugin")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokka")
    implementation("studio.forface.easygradle:dsl-android:$easyGradle")
}

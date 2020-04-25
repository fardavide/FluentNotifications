plugins {
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
}

dependencies {
    val androidGradlePlugin =   "3.6.3"         // Released: Apr 17, 2020
    val dokka =                 "0.10.1"        // Released: Feb 04, 2020
    val easyGradle =            "1.2.3-beta-4"  // Released: Mar 01, 2020

    implementation("com.android.tools.build:gradle:$androidGradlePlugin")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokka")
    implementation("studio.forface.easygradle:dsl-android:$easyGradle")
}
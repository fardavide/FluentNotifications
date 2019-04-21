plugins {
    id("com.android.library" )
    id("kotlin-android" )
    id("kotlin-android-extensions" )
}

android { applyAndroidConfig() }

dependencies {
    implementation( Libs.kotlin )
    implementation( Libs.reflect )

    implementation( Libs.Android.ktx )

    applyTests()
}

applyDokka()
publish("fluentnotifications" )
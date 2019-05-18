plugins {
    id( "com.android.library" )
    id( "kotlin-android" )
    id( "kotlin-android-extensions" )
}

android { applyAndroidConfig() }

dependencies {
    implementation( Lib.kotlin )
    implementation( Lib.reflect )

    implementation( Lib.Android.appcompat )
    implementation( Lib.Android.ktx )

    applyTests()
}

applyDokka()
publish( "fluentnotifications" )
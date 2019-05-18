plugins {
    id( "com.android.application" )
    id( "kotlin-android" )
    id( "kotlin-android-extensions" )
}

android { applyAndroidConfig( "studio.forface.fluentnotifications.demo" ) }

dependencies {
    implementation( project( Module.fluentNotifications ) )

    implementation( Lib.kotlin )
    implementation( Lib.coroutines_android )
    implementation( Lib.reflect )

    implementation( Lib.Android.appcompat )
    implementation( Lib.Android.constraintLayout )
    implementation( Lib.Android.ktx )
    implementation( Lib.Android.material )
}

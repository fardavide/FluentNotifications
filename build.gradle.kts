
buildscript {
    initVersions()

    repositories(repos)
    dependencies {
        classpath(`kotlin-gradle-plugin`)
        classpath(`android-gradle-plugin`)
    }
}

allprojects {
    repositories(repos)
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    tasks.withType(Javadoc::class).all { enabled = false }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}

buildscript {
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
    tasks.withType(Javadoc::class).all { enabled = false }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
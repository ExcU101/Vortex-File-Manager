plugins {
    id(Plugins.AndroidApplication) version Versions.androidVer apply false
    id(Plugins.AndroidLibrary) version Versions.androidVer apply false
    id(Plugins.KotlinAndroid) version Versions.kotlinVer apply false
}

tasks.create<Delete>(
    name = "clean",
    configuration = {
        delete(rootProject.buildDir)
    }
)

subprojects {
    val args = listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xcontext-receivers",
//        "-Xuse-k2"
    )

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
        kotlinOptions.jvmTarget = BuildConfig.JDK.Ver
        kotlinOptions.freeCompilerArgs = args
    }
}
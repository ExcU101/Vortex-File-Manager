object Deps {

    // AndroidX
    object AndroidX {
        const val Annotation = "androidx.annotation:annotation:1.5.0"
        const val Activity = "androidx.activity:activity-ktx:${Versions.activityVer}"
        const val DataStorePreferences = "androidx.datastore:datastore-preferences:1.0.0"
        const val Multidex = "androidx.multidex:multidex:${Versions.multidexVer}"
        const val Appcompat = "androidx.appcompat:appcompat:${Versions.appcompatVer}"
        const val Core = "androidx.core:core-ktx:${Versions.coreVer}"
        const val Fragment = "androidx.fragment:fragment-ktx:${Versions.fragmentVer}"
        const val Collection = "androidx.collection:collection-ktx:${Versions.collectionVer}"
        const val ViewPager = "androidx.viewpager:viewpager:1.0.0"
        const val ViewPager2 = "androidx.viewpager2:viewpager2:1.0.0"
    }

    object ExoPlayer {
        const val Core = "com.google.android.exoplayer:exoplayer-core:${Versions.exoPlayerVer}"
        const val Ui = "com.google.android.exoplayer:exoplayer-ui:${Versions.exoPlayerVer}"
    }

    object Ui {
        const val RecyclerView = "androidx.recyclerview:recyclerview:1.2.1"
        const val Material = "com.google.android.material:material:${Versions.materialVer}"
    }

    // Threads
    object Coroutines {
        const val Core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVer}"
        const val Android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVer}"
        const val Coil = "io.coil-kt:coil:${Versions.coilVer}"
        const val Test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVer}"
    }

    object Dagger {
        const val Lib = "com.google.dagger:dagger:${Versions.daggerVer}"
        const val Compiler = "com.google.dagger:dagger-compiler:${Versions.daggerVer}"
        const val Android = "com.google.dagger:dagger-android:${Versions.daggerVer}"
        const val AndroidProcessor =
            "com.google.dagger:dagger-android-processor:${Versions.daggerVer}"
    }

    // AndroidX Lifecycle
    object Lifecycle {
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVer}"
        const val Runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVer}"
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVer}"
        const val SavedStateViewModel =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycleVer}"

        const val Service = "androidx.lifecycle:lifecycle-service:${Versions.lifecycleVer}"
        const val Process = "androidx.lifecycle:lifecycle-process:${Versions.lifecycleVer}"
    }

    object Application {
        val FileSystem = mapOf("path" to ":filesystem")
        val FileSystemUnix = mapOf("path" to ":filesystem-unix")
        val PackageManager = mapOf("path" to ":package-manager")
        val PluginSystemUi = mapOf("path" to ":pluginsystem-ui")
        val UiComponent = mapOf("path" to ":ui-component")
        val VortexService = mapOf("path" to ":vortex-service")
        val Navigation = mapOf("path" to ":navigation")
    }

    object Room {
        val Runtime = "androidx.room:room-runtime:${Versions.roomVer}"
        val Compiler = "androidx.room:room-compiler:${Versions.roomVer}"
    }

    object UnitTest {
        const val JUnit = "junit:junit:${Versions.junitVer}"
        const val Mockk = "io.mockk:mockk:${Versions.mockkVersion}"
        const val MockkJvm = "io.mockk:mockk-agent-jvm:${Versions.mockkVersion}"
        const val MockkAndroid = "io.mockk:mockk-android:${Versions.mockkVersion}"
    }

    object LeakCanary {
        const val Android = "com.squareup.leakcanary:leakcanary-android:2.10"
    }

    object Architecture {

    }

}
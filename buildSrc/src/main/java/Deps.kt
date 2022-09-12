object Deps {

    // AndroidX
    object AndroidX {
        const val DataStorePreferences = "androidx.datastore:datastore-preferences:1.0.0"
        const val Multidex = "androidx.multidex:multidex:${Versions.multidexVer}"
        const val Appcompat = "androidx.appcompat:appcompat:${Versions.appcompatVer}"
        const val Core = "androidx.core:core-ktx:${Versions.coreVer}"
        const val Fragment = "androidx.fragment:fragment-ktx:${Versions.fragmentVer}"
        const val Collection = "androidx.collection:collection-ktx:${Versions.collectionVer}"
    }

    object Ui {
        const val RecyclerView = "androidx.recyclerview:recyclerview:1.2.1"
        const val RecyclerViewSelection = "androidx.recyclerview:recyclerview-selection:1.1.0"
        const val Material = "com.google.android.material:material:${Versions.materialVer}"
    }

    // Threads
    object Coroutines {
        const val Core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVer}"
        const val Android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVer}"
        const val Test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVer}"
    }

    object Hilt {
        const val Android = "com.google.dagger:hilt-android:${Versions.hiltVer}"
        const val Compiler = "com.google.dagger:hilt-compiler:${Versions.hiltVer}"
    }

    // AndroidX Lifecycle
    object Lifecycle {
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVer}"
        const val Runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVer}"
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVer}"
        const val SavedStateViewModel =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycleVer}"
    }

    object Application {
        val FileSystem = mapOf("path" to ":filesystem")
        val FileSystemUnix = mapOf("path" to ":filesystem-unix")
        val PluginSystem = mapOf("path" to ":pluginsystem")
        val PluginSystemCommon = mapOf("path" to ":pluginsystem-common")
        val UiComponent = mapOf("path" to ":ui-component")
        val VortexService = mapOf("path" to ":vortex-service")
    }

    object UnitTest {
        const val JUnit = "junit:junit:${Versions.junitVer}"
        const val Mockk = "io.mockk:mockk:${Versions.mockkVersion}"
        const val MockkJvm = "io.mockk:mockk-agent-jvm:${Versions.mockkVersion}"
        const val MockkAndroid = "io.mockk:mockk-android:${Versions.mockkVersion}"
    }

    object Architecture {

    }

}
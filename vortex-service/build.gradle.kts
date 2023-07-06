plugins {
    id(Plugins.AndroidLibrary)
    id(Plugins.Parcelize)
    kotlin(Plugins.Android)
    kotlin(Plugins.Kapt)
}

kapt {
    correctErrorTypes = true
}

android {
    compileSdk = AndroidConfigure.targetSdk
    namespace = "io.github.excu101.vortex"

    defaultConfig {
        minSdk = AndroidConfigure.minSdk
        targetSdk = AndroidConfigure.targetSdk

        consumerProguardFiles("consumer-rules.pro")
    }

    sourceSets {
//        getByName<com.android.build.api.dsl.AndroidSourceSet>(name = "main") {
//            aidl.srcDirs("src/main/aidl/")
//        }
    }

    buildFeatures {
//        aidl = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = BuildConfig.JDK.VerEnum
        targetCompatibility = BuildConfig.JDK.VerEnum
    }

    kotlinOptions {
        jvmTarget = BuildConfig.JDK.Ver
    }
}

dependencies {
    implementation(Deps.AndroidX.Core)
    implementation(Deps.Ui.Material)
    implementation(Deps.ExoPlayer.Core)
    implementation(project(Deps.Application.FileSystem))
    implementation(project(Deps.Application.FileSystemUnix))
    implementation(Deps.Lifecycle.Service)
    implementation(Deps.Lifecycle.Runtime)
    implementation(project(Deps.Application.PackageManager))
    implementation(Deps.Lifecycle.Process)
    implementation(Deps.Coroutines.Android)

    implementation(project(Deps.Application.VortexTheme))

    implementation(Deps.Dagger.Lib)
    kapt(Deps.Dagger.Compiler)
}
plugins {
    id(Plugins.AndroidLibrary)
    id(Plugins.Parcelize)
    kotlin(Plugins.Android)
}

android {
    namespace = "io.github.excu101.filesystem.unix"
    compileSdk = AndroidConfigure.targetSdk

    defaultConfig {
        minSdk = AndroidConfigure.minSdk
        targetSdk = AndroidConfigure.targetSdk

        consumerProguardFiles("consumer-rules.pro")
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
    implementation(Deps.Coroutines.Core)
    implementation(project(Deps.Application.FileSystem))
}
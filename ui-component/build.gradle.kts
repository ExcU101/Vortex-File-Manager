plugins {
    id(Plugins.AndroidLibrary)
    kotlin(Plugins.Android)
    id(Plugins.Parcelize)
}

android {
    namespace = "io.github.excu101.vortex.ui.component"
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
    implementation(Deps.Ui.RecyclerView)
    implementation(Deps.Coroutines.Android)
    implementation(Deps.Ui.Material)
    implementation(Deps.AndroidX.Collection)
    implementation(project(Deps.Application.PackageManager))
    implementation(project(Deps.Application.FileSystem))
    implementation(project(Deps.Application.VortexTheme))
}
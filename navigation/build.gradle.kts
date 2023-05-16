plugins {
    id(Plugins.AndroidLibrary)
    kotlin(Plugins.Android)
}

android {
    namespace = "io.github.excu101.vortex.navigation"
    compileSdk = AndroidConfigure.targetSdk

    defaultConfig {
        minSdk = AndroidConfigure.minSdk
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
    implementation(Deps.AndroidX.Activity)
    implementation(Deps.AndroidX.ViewPager)
}
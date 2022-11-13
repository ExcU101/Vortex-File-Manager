plugins {
    id(Plugins.AndroidLibrary)
    kotlin(Plugins.Android)
}

android {
    namespace = "io.github.excu101.vortex.navigation"
    compileSdk = AndroidConfigure.targetSdk

    defaultConfig {
        minSdk = AndroidConfigure.minSdk
        targetSdk = AndroidConfigure.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Deps.AndroidX.Fragment)
}
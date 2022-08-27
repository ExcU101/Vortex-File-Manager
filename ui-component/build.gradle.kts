plugins {
    id(Plugins.AndroidLibrary)
    kotlin(Plugins.Android)
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
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
    implementation(Deps.Ui.RecyclerView)
    implementation(Deps.Ui.Material)
    implementation(project(Deps.Application.PluginSystem))
    implementation(project(Deps.Application.FileSystem))
}
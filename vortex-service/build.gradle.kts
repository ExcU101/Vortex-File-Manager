plugins {
    id(Plugins.AndroidLibrary)
    id(Plugins.Parcelize)
    kotlin(Plugins.Android)
}

android {
    compileSdk = AndroidConfigure.targetSdk

    defaultConfig {
        minSdk = AndroidConfigure.minSdk
        targetSdk = AndroidConfigure.targetSdk

        consumerProguardFiles("consumer-rules.pro")
    }

    sourceSets {
        getByName<com.android.build.api.dsl.AndroidSourceSet>(name = "main") {
            aidl.srcDirs("src/main/aidl")
        }
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
    implementation(project(Deps.Application.FileSystem))
}
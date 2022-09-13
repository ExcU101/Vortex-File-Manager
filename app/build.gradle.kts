import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id(Plugins.AndroidApplication)
    id(Plugins.HiltPlugin)
    id(Plugins.Parcelize)
    kotlin(Plugins.Android)
    kotlin(Plugins.Kapt)
}

kapt {
    correctErrorTypes = true
}

android {
    compileSdk = AndroidConfigure.targetSdk

    defaultConfig {
        applicationId = AndroidConfigure.applicationId
        minSdk = AndroidConfigure.minSdk
        targetSdk = AndroidConfigure.targetSdk
        versionCode = AndroidConfigure.versionCode
        versionName = AndroidConfigure.versionName
        multiDexEnabled = AndroidConfigure.multiDexEnabled
    }

    signingConfigs {
        create(BuildConfig.Release.Name) {
            val props = gradleLocalProperties(rootDir)
            storeFile = file(props["signing.path"].toString())
            storePassword = props["signing.pathPassword"].toString()
            keyAlias = props["signing.alias"].toString()
            keyPassword = props["signing.aliasPassword"].toString()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs[BuildConfig.Release.Name]
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        cmake {
            path = file(BuildConfig.CMake.Path)
            version = Versions.cmakeVer
        }
    }
}

dependencies {
    implementation(Deps.AndroidX.Core)
    implementation(Deps.AndroidX.Fragment)
    implementation(Deps.AndroidX.DataStorePreferences)
    implementation(Deps.AndroidX.Multidex)
    implementation(Deps.AndroidX.Appcompat)
    implementation(Deps.AndroidX.Collection)

    implementation(Deps.Hilt.Android)
    kapt(Deps.Hilt.Compiler)

    implementation(Deps.Ui.Material)
    implementation(Deps.Ui.RecyclerView)
    implementation(Deps.Ui.RecyclerViewSelection)

    implementation(Deps.Lifecycle.LiveData)
    implementation(Deps.Lifecycle.Runtime)
    implementation(Deps.Lifecycle.SavedStateViewModel)
    implementation(Deps.Lifecycle.ViewModel)

    implementation(project(Deps.Application.FileSystem))
    implementation(project(Deps.Application.FileSystemUnix))
    implementation(project(Deps.Application.PluginSystem))
    implementation(project(Deps.Application.PluginSystemCommon))
    implementation(project(Deps.Application.UiComponent))
    implementation(project(Deps.Application.VortexService))
}
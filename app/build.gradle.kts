import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id(Plugins.AndroidApplication)
    id(Plugins.Parcelize)
    kotlin(Plugins.Android)
    kotlin(Plugins.Kapt)
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = "io.github.excu101.vortex"
    compileSdk = AndroidConfigure.targetSdk

    defaultConfig {
        applicationId = AndroidConfigure.applicationId
        minSdk = AndroidConfigure.minSdk
        targetSdk = AndroidConfigure.targetSdk
        versionCode = AndroidConfigure.versionCode
        versionName = AndroidConfigure.versionName
        multiDexEnabled = AndroidConfigure.multiDexEnabled

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
//                    "room.incremental" to "true"
                )
            }
        }
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
        sourceCompatibility = BuildConfig.JDK.VerEnum
        targetCompatibility = BuildConfig.JDK.VerEnum
    }

    kotlinOptions {
        jvmTarget = BuildConfig.JDK.Ver
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

    implementation(Deps.Dagger.Lib)
    kapt(Deps.Dagger.Compiler)

    implementation(Deps.Ui.Material)
    implementation(Deps.Ui.RecyclerView)

    implementation(Deps.Lifecycle.LiveData)
    implementation(Deps.Lifecycle.Runtime)
    implementation(Deps.Lifecycle.SavedStateViewModel)
    implementation(Deps.Lifecycle.ViewModel)

//    debugImplementation(Deps.LeakCanary.Android)

    implementation(project(Deps.Application.FileSystem))
    implementation(project(Deps.Application.FileSystemUnix))
    implementation(project(Deps.Application.PackageManager))
    implementation(project(Deps.Application.UiComponent))
    implementation(project(Deps.Application.VortexService))
    implementation(project(Deps.Application.Navigation))
    implementation(project(Deps.Application.VortexTheme))
}
import org.gradle.api.JavaVersion

object BuildConfig {

    object Release {
        const val Name = "release"
    }

    object CMake {
        const val Path = "src/main/cpp/CMakeLists.txt"
    }

    object JDK {
        const val Ver = "17"
        val VerEnum = JavaVersion.VERSION_17
    }


}
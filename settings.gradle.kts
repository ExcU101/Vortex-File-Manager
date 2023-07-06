pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Vortex File Manager"
include(
    ":app",
    ":filesystem",
    ":filesystem-unix",
    ":package-manager",
    ":ui-component",
    ":vortex-service",
    ":pluginsystem-ui",
    ":navigation"
)
include(":filesystem-experimental")
include(":filesystem-experimental-linux")
include(":vortex-theme")

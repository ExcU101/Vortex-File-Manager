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
    ":pluginsystem",
    ":ui-component",
    ":vortex-service",
    ":manifest-dsl",
    ":pluginsystem-ui",
    ":navigation"
)
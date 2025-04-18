pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":app")
include(":sort-files-plugin")

// todo temp testing
// includeBuild("app")
// include(":sort-files-plugin")


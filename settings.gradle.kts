rootProject.name = "Capturable"

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("android.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("android.*")
            }
        }
        mavenCentral()
    }
}
include(":sample:sharedUI")
include(":sample:androidApp")
include(":sample:desktopApp")
include(":sample:webApp")

include(":capturable")
include(":capturable-extension")
include(":docs")

project(":capturable-extension").projectDir = file("./capturableExtension")

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(17)
    androidTarget {}

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui.tooling.preview)

            implementation(project(":capturable"))
            implementation(project(":capturable-extension"))
            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
        }
        //For Android,iOS
        val mobileMain by creating {
            dependsOn(commonMain.get())
        }
        
        androidMain {
            dependsOn(mobileMain)
            dependencies {
                implementation(libs.compose.ui.tooling)
                implementation("androidx.activity:activity-compose:1.10.1")
            }
        }


        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        iosMain {
            dependsOn(mobileMain)
        }

        jsMain.dependencies {
            implementation(libs.compose.html.core)
        }

    }

}

android {
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.wonddak.capturableExample"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    namespace = "dev.wonddak.capturableExample"
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "CaputrableExample"
            packageVersion = "1.0.0"

            linux {
                iconFile.set(project.file("desktopAppIcons/LinuxIcon.png"))
            }
            windows {
                iconFile.set(project.file("desktopAppIcons/WindowsIcon.ico"))
            }
            macOS {
                iconFile.set(project.file("desktopAppIcons/MacosIcon.icns"))
                bundleID = "dev.shreyaspatil.capturableExample.desktopApp"
            }
        }
    }
}

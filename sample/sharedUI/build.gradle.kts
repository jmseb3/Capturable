import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    applyDefaultHierarchyTemplate()
    android {
        namespace = "dev.wonddak.capturableExample"
        compileSdk = 36
        minSdk = 23
        androidResources.enable = true
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
    }

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
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.compose.resources)
            api(libs.compose.ui.tooling.preview)

            implementation(project(":capturable"))
            implementation(project(":capturable-extension"))

            api(libs.filekit.core)
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
            }
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

    }
}



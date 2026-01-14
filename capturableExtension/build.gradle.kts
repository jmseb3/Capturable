import com.android.build.api.dsl.androidLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish)
}

kotlin {

    androidLibrary {
        namespace = "dev.wonddak.capturable.extension"
        compileSdk = 36
        minSdk = 23
        androidResources.enable = true
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
    }
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    js {
        browser()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // Apply the default hierarchy again. It'll create, for example, the iosMain source set:
    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kotlinx.coroutines.core)

            implementation(project(":capturable"))

            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.filekit.dialogs.compose)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))

            implementation(libs.kotlinx.coroutines.test)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }

        //For Android,iOS
        val mobileMain by creating {
            dependsOn(commonMain.get())
        }

        //For Android,iOS,JVM
        val nonWebMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(mobileMain)
            dependsOn(nonWebMain)
            dependencies {

            }
        }

        androidUnitTest.dependencies {
            implementation(libs.junit.android)
            implementation(libs.mockk)
        }

        iosMain {
            dependsOn(mobileMain)
            dependsOn(nonWebMain)
        }
        jvmMain {
            dependsOn(nonWebMain)
        }

        jvmTest.dependencies {
            implementation(compose.desktop.uiTestJUnit4)
            implementation(compose.desktop.currentOs)
        }
    }
}

dokka {
    moduleName.set("Extension")

    dokkaPublications.html {
        outputDirectory.set(project.mkdir("build/dokka"))
    }

    dokkaSourceSets {
        this.commonMain {
            displayName.set("Common")
        }
        named("mobileMain") {
            displayName.set("Mobile(Android,iOS)")
        }
        named("nonWebMain") {
            displayName.set("nonWeb(Android,iOS,JVM)")
        }
        named("webMain") {
            displayName.set("Web(WASM,JS)")
        }
    }
}

mavenPublishing {
    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
            sourcesJar = true
        )
    )
}

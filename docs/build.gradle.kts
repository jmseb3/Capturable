plugins {
    alias(libs.plugins.dokka)
    kotlin("jvm") apply false
}

dependencies {
    dokka(project(":capturable"))
    dokka(project(":capturable-extension"))
}

dokka {
    moduleName.set("Capturable")

//    dokkaPublications.html {
//        includes.from("DocsModule.md")
//    }
}
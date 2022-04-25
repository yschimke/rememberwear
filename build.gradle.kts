@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidapplication) apply false
    alias(libs.plugins.androidlibrary) apply false
    alias(libs.plugins.androidtest) apply false
    alias(libs.plugins.kotlinandroid) apply false
    alias(libs.plugins.kotlinkapt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.firebase) apply false
    alias(libs.plugins.firebasecrashlytics) apply false
    alias(libs.plugins.firebaseperformance) apply false
    id("com.autonomousapps.dependency-analysis") version "1.0.0-rc06"
}

tasks.create<Delete>("clean") {
    group = "build"
    delete = setOf(
        rootProject.buildDir
    )
}

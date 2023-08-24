buildscript {
    dependencies {
        classpath(libs.gradle)
        classpath(libs.google.services)
        if (file("wear/google-services.json").exists()) {
            classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        }
    }
}
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidapplication) apply false
    alias(libs.plugins.androidlibrary) apply false
    alias(libs.plugins.androidtest) apply false
    alias(libs.plugins.kotlinandroid) apply false
    alias(libs.plugins.kotlinkapt) apply false
    alias(libs.plugins.hilt) apply false
    if (file("wear/google-services.json").exists()) {
        alias(libs.plugins.firebase) apply false
        alias(libs.plugins.firebasecrashlytics) apply false
        alias(libs.plugins.firebaseperformance) apply false
    }
    alias(libs.plugins.ksp) apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("com.autonomousapps.dependency-analysis") version "1.19.0"
}

tasks.create<Delete>("clean") {
    group = "build"
    delete = setOf(
        rootProject.buildDir
    )
}

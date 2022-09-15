buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.0")
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.1")
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
    alias(libs.plugins.firebase) apply false
    alias(libs.plugins.firebasecrashlytics) apply false
    alias(libs.plugins.firebaseperformance) apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
    id("com.autonomousapps.dependency-analysis") version "1.13.1"
}

tasks.create<Delete>("clean") {
    group = "build"
    delete = setOf(
        rootProject.buildDir
    )
}

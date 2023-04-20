@file:Suppress("SuspiciousCollectionReassignment")

import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.konan.properties.hasProperty
import java.util.Properties
import java.io.FileInputStream

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlinx-serialization")
    if (File("google-services.json").exists()) {
        id("com.google.gms.google-services")
        id("com.google.firebase.crashlytics")
    }
}

val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(FileInputStream(rootProject.file("local.properties")))
    }
}

val versionProperties = Properties().apply {
    val localFile = rootProject.file("wear/versions.properties")
    load(FileInputStream(localFile))
}

val versionCodeProperty = versionProperties.getProperty("VERSION_CODE").toInt()

android {
    namespace = "com.google.wear.soyted"

    compileSdk = 33

    defaultConfig {
        applicationId = "ee.schimke.wear.soyted"
        minSdk = 26
        targetSdk = 33
        testInstrumentationRunner = "com.google.wear.soyted.junit.CustomTestRunner"
        versionCode = versionCodeProperty
        versionName = "release-$versionCodeProperty"

        setProperty("archivesBaseName", "rememberwear")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    kotlinOptions {
        freeCompilerArgs += "-opt-in=androidx.compose.animation.ExperimentalAnimationApi"
        freeCompilerArgs += "-opt-in=androidx.wear.compose.material.ExperimentalWearMaterialApi"
        freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs += "-opt-in=kotlin.contracts.ExperimentalContracts"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        freeCompilerArgs += "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        freeCompilerArgs += "-opt-in=com.google.android.horologist.annotations.ExperimentalHorologistApi"
    }

    val releaseSigned = localProperties.hasProperty("keyStore")

    if (releaseSigned) {
        signingConfigs {
            create("release") {
                storeFile = file(localProperties["keyStore"] as String)
                keyAlias = localProperties["keyAlias"] as String?
                keyPassword = localProperties["keyPassword"] as String?
                storePassword = localProperties["storePassword"] as String?
            }
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            buildConfigField(
                "String",
                "API_KEY",
                localProperties["API_KEY"].writeBuildConfigString()
            )
            buildConfigField(
                "String",
                "API_SECRET",
                localProperties["API_SECRET"].writeBuildConfigString()
            )
        }
        release {
            buildConfigField(
                "String",
                "API_KEY",
                localProperties["API_KEY"].writeBuildConfigString()
            )
            buildConfigField(
                "String",
                "API_SECRET",
                localProperties["API_SECRET"].writeBuildConfigString()
            )
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName(if (releaseSigned) "release" else "debug")
        }
        create("benchmark") {
            buildConfigField(
                "String",
                "API_KEY",
                localProperties["API_KEY"].writeBuildConfigString()
            )
            buildConfigField(
                "String",
                "API_SECRET",
                localProperties["API_SECRET"].writeBuildConfigString()
            )
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.addAll(listOf("release", "debug"))
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        resources.excludes.add("META-INF/NOTICE.md")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("build-data.properties")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    jvmToolchain(11)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    (this as? org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions)?.let {
        it.jvmTarget.set(JvmTarget.JVM_11)
    }
}

val javaToolchains = project.extensions.getByType<JavaToolchainService>()

extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension> {
    jvmToolchain(11)
}

extensions.findByName("android")?.apply{
    this as BaseExtension
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(11)
        targetCompatibility = JavaVersion.toVersion(11)
    }
}

dependencies {
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.google.firebase.analytics)
    kapt(libs.hilt.compiler)
    ksp(libs.room.compiler)
    kapt(libs.dagger.hiltcompiler)

    implementation(libs.accompanist.swiperefresh)
    implementation(libs.androidx.corektx)
    implementation(libs.androidx.lifecycleruntimektx)
    implementation(libs.coil.compose)
    implementation(libs.compose.materialiconscore)
    implementation(libs.compose.ui)
    implementation(libs.compose.uitooling)
    implementation(libs.dagger.hiltandroid)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    if (file("google-services.json").exists()) {
        implementation(libs.firebase.performance)
    }
    implementation(libs.gms.playserviceswearable)
    implementation(libs.hilt.navigationcompose)
    implementation(libs.hilt.work)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlibjdk8)
    implementation(libs.kotlinx.coroutinescore)
    implementation(libs.kotlinx.coroutinesguava)
    implementation(libs.kotlinx.coroutinesplayservices)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    implementation(libs.squareup.logcat)
    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.okhttp3brotli)
    implementation(libs.squareup.okhttp3logginginterceptor)
    implementation(libs.wear.complicationsdata)
    implementation(libs.wear.complicationsdatasource)
    implementation(libs.wear.complicationsdatasourcektx)
    implementation(libs.wear.composematerial)
    implementation(libs.wear.composenavigation)
    implementation(libs.wear.input)
    implementation(libs.wear.remoteinteractions)
    implementation(libs.wear.tiles)
    implementation(libs.wear.tiles.material)
    implementation(libs.work.runtimektx)
    implementation(platform(libs.squareup.okhttp3bom))
    implementation(libs.horologist.tiles)
    implementation(libs.horologist.compose.layout)
    implementation(libs.horologist.network.awareness)
    implementation(libs.androidx.metrics.performance)

    implementation(libs.core.android)
    implementation(libs.serialization.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.xml)
    implementation(libs.squareup.retrofit2retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    implementation(libs.kotlinx.datetime.jvm)

    implementation(libs.horologist.compose.tools)

    kaptAndroidTest(libs.dagger.hiltandroidcompiler)
    testAnnotationProcessor(libs.dagger.hiltandroidcompiler)
    testAnnotationProcessor(libs.dagger.hiltcompiler)
    androidTestAnnotationProcessor(libs.dagger.hiltandroidcompiler)
    androidTestImplementation(libs.test.espressocore)
    androidTestImplementation(libs.test.extjunit)
    androidTestImplementation(libs.test.extjunitktx)
    androidTestImplementation(libs.dagger.hiltandroidtesting)
    androidTestImplementation(libs.kotlinx.coroutinestest)
    androidTestImplementation(libs.fastlane.screengrab)
    androidTestImplementation(libs.assertj.core)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.wear.tiles.renderer)
    androidTestImplementation(libs.work.testing)
}

fun Any?.writeBuildConfigString(): String =
    if (this != null && this != "") "\"${this}\"" else "null"

if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
    apply(plugin = "com.google.firebase.firebase-perf")
}

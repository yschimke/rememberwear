import org.jetbrains.kotlin.konan.properties.hasProperty
import java.util.Properties
import java.io.FileInputStream

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(FileInputStream(rootProject.file("local.properties")))
    }
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "ee.schimke.wear.soyted"
        minSdk = 26
        targetSdk = 30
        testInstrumentationRunner = "com.google.wear.soyted.junit.CustomTestRunner"
        versionCode = 14
        versionName = "14"

        setProperty("archivesBaseName", "rememberwear")
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    kotlinOptions {
        freeCompilerArgs += "-opt-in=androidx.compose.animation.ExperimentalAnimationApi"
        freeCompilerArgs += "-opt-in=androidx.wear.compose.material.ExperimentalWearMaterialApi"
        freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs += "-opt-in=androidx.wear.compose.navigation.rememberSwipeDismissableNavController"
        freeCompilerArgs += "-opt-in=kotlin.contracts.ExperimentalContracts"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        freeCompilerArgs += "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
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

dependencies {
    kapt(libs.hilt.compiler)
    kapt(libs.room.compiler)
    kapt(libs.dagger.hiltcompiler)
    kapt(libs.squareup.moshikotlincodegen)
    kaptTest(libs.dagger.hiltandroidcompiler)
    kapt(libs.tikxml.processor)

    implementation(libs.accompanist.swiperefresh)
    implementation(libs.androidx.activityktx)
    implementation(libs.androidx.corektx)
    implementation(libs.androidx.glanceweartiles)
    implementation(libs.androidx.legacysupportv4)
    implementation(libs.androidx.lifecycleruntimektx)
    implementation(libs.androidx.lifecycleservice)
    implementation(libs.androidx.securitycryptoktx)
    implementation(libs.androidx.wear)
    implementation(libs.coil.compose)
    implementation(libs.compose.materialiconscore)
    implementation(libs.compose.ui)
    implementation(libs.compose.uitooling)
    implementation(libs.dagger.hiltandroid)
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
    implementation(libs.squareup.moshiadapters)
    implementation(libs.squareup.moshikotlin)
    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.okhttp3brotli)
    implementation(libs.squareup.okhttp3logginginterceptor)
    implementation(libs.squareup.retrofit2convertermoshi)
    implementation(libs.squareup.retrofit2retrofit)
    implementation(libs.tikxml.annotation)
    implementation(libs.tikxml.core)
    implementation(libs.tikxml.retrofitconverter)
    implementation(libs.wear.complicationsdata)
    implementation(libs.wear.complicationsdatasource)
    implementation(libs.wear.complicationsdatasourcektx)
    implementation(libs.wear.composefoundation)
    implementation(libs.wear.composematerial)
    implementation(libs.wear.composenavigation)
    implementation(libs.wear.input)
    implementation(libs.wear.phoneinteractions)
    implementation(libs.wear.remoteinteractions)
    implementation(libs.wear.tiles)
    implementation(libs.wear.tilesproto)
    implementation(libs.work.runtimektx)
    implementation(platform(libs.squareup.okhttp3bom))
    implementation(libs.horologist.tiles)

    kaptAndroidTest(libs.dagger.hiltandroidcompiler)
    testAnnotationProcessor(libs.dagger.hiltandroidcompiler)
    testAnnotationProcessor(libs.dagger.hiltcompiler)
    testImplementation(libs.dagger.hiltandroidtesting)
    testImplementation(libs.junit)
    androidTestAnnotationProcessor(libs.dagger.hiltandroidcompiler)
    androidTestImplementation(libs.test.espressocore)
    androidTestImplementation(libs.test.extjunit)
    androidTestImplementation(libs.dagger.hiltandroidtesting)
    androidTestImplementation(libs.kotlinx.coroutinestest)
    androidTestImplementation(libs.fastlane.screengrab)
    testImplementation(libs.assertj.core)
    androidTestImplementation(libs.assertj.core)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
}

fun Any?.writeBuildConfigString(): String =
    if (this != null && this != "") "\"${this}\"" else "null"

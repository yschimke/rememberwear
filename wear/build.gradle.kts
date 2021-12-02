import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt") version libs.versions.kotlin
    id("dagger.hilt.android.plugin") version libs.versions.hilt
}

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.google.wear.rememberwear"
        minSdk = 26
        targetSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    kotlinOptions {
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "API_KEY",
                "\"${localProperties["API_KEY"]}\""
            )
            buildConfigField(
                "String",
                "API_SECRET",
                "\"${localProperties["API_SECRET"]}\""
            )
            buildConfigField(
                "String",
                "TOKEN",
                "\"${localProperties["TOKEN"]}\""
            )
        }
        release {
            buildConfigField(
                "String",
                "API_KEY",
                "\"${localProperties["API_KEY"]}\""
            )
            buildConfigField(
                "String",
                "API_SECRET",
                "\"${localProperties["API_SECRET"]}\""
            )
            buildConfigField(
                "String",
                "TOKEN",
                "\"${localProperties["TOKEN"]}\""
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
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("com.google.android.gms:play-services-wearable:17.1.0")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("androidx.wear:wear:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.5.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")


    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha03")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")

    implementation("androidx.compose.ui:ui:1.0.5")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.wear.compose:compose-foundation:1.0.0-alpha11")
    implementation("androidx.wear.compose:compose-material:1.0.0-alpha11")
    implementation("androidx.wear.compose:compose-navigation:1.0.0-alpha11")
    implementation("androidx.compose.ui:ui-tooling:1.0.5")

    implementation("io.coil-kt:coil-compose:${libs.versions.coil.get()}")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.20.0")

    implementation("androidx.compose.material:material-icons-core:1.0.5")

    implementation("androidx.hilt:hilt-work:1.0.0")
    implementation("androidx.work:work-multiprocess:2.7.1")
    implementation("androidx.work:work-runtime-ktx:2.7.1")

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.stdlibJdk8)
    implementation(libs.kotlin.reflect)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-adapters:1.12.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.2"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    implementation("com.google.dagger:hilt-android:2.39.1")
    kapt("com.google.dagger:hilt-compiler:2.39.1")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("com.squareup.retrofit2:converter-simplexml:2.9.0")

    // For instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.39.1")
    kaptAndroidTest("com.google.dagger:hilt-com.sun.tools.javac.resources.compiler:2.39.1")

    // For local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:2.39.1")
    testAnnotationProcessor("com.google.dagger:hilt-compiler:2.39.1")

    implementation("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")

    implementation("androidx.wear.tiles:tiles:1.0.0")
    implementation("androidx.wear.tiles:tiles-proto:1.0.0")
    debugImplementation("androidx.wear.tiles:tiles-renderer:1.0.0")

    implementation("androidx.wear:wear-complications-data-source:1.0.0-alpha22")
    implementation("androidx.wear:wear-complications-data:1.0.0-alpha22")

    implementation("com.google.android.libraries.maps:maps:3.1.0-beta")
    implementation("com.google.maps.android:maps-v3-ktx:2.3.0")
    implementation("com.google.maps.android:android-maps-utils-v3:2.3.0")
    implementation("com.android.volley:volley:1.2.1")
}
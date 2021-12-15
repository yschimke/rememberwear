plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.test")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 29
        targetSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        // declare a build type (release) to match the target app's build type
        create("benchmark") {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    targetProjectPath(":wear")
    experimentalProperties["android.experimental.self-instrumenting"] = true

    variantFilter {
        if (buildType.name.contains("release") || buildType.name.contains("debug")) {
            ignore = true
        }
    }
}

dependencies {
    implementation("androidx.test.ext:junit:1.1.3")
    implementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.benchmark:benchmark-macro-junit4:1.1.0-alpha12")
}

androidComponents {
    beforeVariants(selector().all()) { variant ->
        variant.enabled = variant.buildType == "benchmark"
    }
}
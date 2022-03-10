plugins {
    alias(libs.plugins.androidapplication) apply false
    alias(libs.plugins.kotlinandroid) apply false
    alias(libs.plugins.kotlinkapt) apply false
    alias(libs.plugins.hilt) apply false
}

tasks.create<Delete>("clean") {
    group = "build"
    delete = setOf(
        rootProject.buildDir
    )
}
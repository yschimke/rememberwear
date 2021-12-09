// Top-level build file where you can add configuration options common to all sub-projects/modules.

tasks.create<Delete>("clean") {
    group = "build"
    delete = setOf (
        rootProject.buildDir
    )
}

allprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group.startsWith("androidx.compose.") && requested.version == "1.1.0-beta04") {
                useVersion("1.1.0-beta03")
                because("fixes scrolling bug")
            }
        }
    }
}
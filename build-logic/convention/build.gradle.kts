plugins {
    `kotlin-dsl`
}

group = "com.example.todosampleapp.buildlogic"

dependencies {
    // Convention Plugin から各 Gradle プラグインの API を利用するために compileOnly で参照する
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
    compileOnly(libs.ktlint.gradlePlugin)
    compileOnly(libs.roborazzi.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "todosampleapp.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "todosampleapp.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "todosampleapp.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "todosampleapp.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidFeatureUi") {
            id = "todosampleapp.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }
        register("androidFeaturePresentation") {
            id = "todosampleapp.android.feature.presentation"
            implementationClass = "AndroidFeaturePresentationConventionPlugin"
        }
        register("androidFeatureEntry") {
            id = "todosampleapp.android.feature.entry"
            implementationClass = "AndroidFeatureEntryConventionPlugin"
        }
        register("jvmLibrary") {
            id = "todosampleapp.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}

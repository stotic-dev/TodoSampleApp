plugins {
    alias(libs.plugins.todosampleapp.android.application)
    alias(libs.plugins.todosampleapp.android.hilt)
    alias(libs.plugins.firebase)
}

android {
    namespace = "com.example.todosampleapp"

    defaultConfig {
        applicationId = "com.example.todosampleapp"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    // feature と infra の実体を束ねるのは app だけ
    implementation(projects.feature.todolist)
    implementation(projects.feature.todoadd)
    implementation(projects.feature.tododetail)
    implementation(projects.infra.data)
    implementation(projects.core.designsystem)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

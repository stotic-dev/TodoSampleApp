plugins {
    alias(libs.plugins.todosampleapp.android.feature)
}

android {
    namespace = "com.example.todosampleapp.feature.todoadd"
}

dependencies {
    implementation(projects.feature.todoadd.ui)
}

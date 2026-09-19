plugins {
    alias(libs.plugins.todosampleapp.android.feature.entry)
}

android {
    namespace = "com.example.todosampleapp.feature.todoadd.entry"
}

dependencies {
    implementation(projects.feature.todoadd.ui)
    implementation(projects.feature.todoadd.presentation)
}

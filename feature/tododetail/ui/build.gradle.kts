plugins {
    alias(libs.plugins.todosampleapp.android.feature.ui)
}

android {
    namespace = "com.example.todosampleapp.feature.tododetail.ui"
}

dependencies {
    implementation(projects.core.domain)
}

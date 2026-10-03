plugins {
    alias(libs.plugins.todosampleapp.android.feature.ui)
}

android {
    namespace = "com.example.todosampleapp.feature.todolist.ui"
}

dependencies {
    implementation(projects.core.domain)
}

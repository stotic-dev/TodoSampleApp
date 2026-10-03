plugins {
    alias(libs.plugins.todosampleapp.android.feature)
}

android {
    namespace = "com.example.todosampleapp.feature.todolist"
}

dependencies {
    implementation(projects.feature.todolist.ui)
}

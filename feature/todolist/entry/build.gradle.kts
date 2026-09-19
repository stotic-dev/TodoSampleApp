plugins {
    alias(libs.plugins.todosampleapp.android.feature.entry)
}

android {
    namespace = "com.example.todosampleapp.feature.todolist.entry"
}

dependencies {
    implementation(projects.feature.todolist.ui)
    implementation(projects.feature.todolist.presentation)
}

plugins {
    alias(libs.plugins.todosampleapp.android.feature.entry)
}

android {
    namespace = "com.example.todosampleapp.feature.tododetail.entry"
}

dependencies {
    implementation(projects.feature.tododetail.ui)
    implementation(projects.feature.tododetail.presentation)
}

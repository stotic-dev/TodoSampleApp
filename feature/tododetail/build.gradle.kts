plugins {
    alias(libs.plugins.todosampleapp.android.feature)
}

android {
    namespace = "com.example.todosampleapp.feature.tododetail"
}

dependencies {
    implementation(projects.feature.tododetail.ui)
}

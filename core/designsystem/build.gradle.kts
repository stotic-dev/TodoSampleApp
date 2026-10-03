plugins {
    alias(libs.plugins.todosampleapp.android.library.compose)
}

android {
    namespace = "com.example.todosampleapp.designsystem"
}

dependencies {
    implementation(libs.androidx.compose.ui.text.google.fonts)
}

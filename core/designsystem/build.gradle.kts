plugins {
    alias(libs.plugins.todosampleapp.android.library.compose)
    id("todosampleapp.android.test.roborazzi")
}

android {
    namespace = "com.example.todosampleapp.designsystem"
}

dependencies {
    implementation(libs.androidx.compose.ui.text.google.fonts)

    // --- 以下を追加 ---
    testImplementation(libs.androidx.junit) // JUnit 4 (Roborazziは現在JUnit4が標準)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

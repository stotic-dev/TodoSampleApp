plugins {
    alias(libs.plugins.todosampleapp.android.library)
    alias(libs.plugins.todosampleapp.android.hilt)
}

android {
    namespace = "com.example.todosampleapp.infra.data"
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.kotlinx.coroutines.core)
}

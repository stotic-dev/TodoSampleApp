plugins {
    alias(libs.plugins.todosampleapp.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
}

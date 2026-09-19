import com.example.todosampleapp.buildlogic.guardFeatureDependencies
import com.example.todosampleapp.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * feature:*:presentation 用。
 *
 * ViewModel と UiState を置くモジュール。Compose には依存しない。
 */
class AndroidFeaturePresentationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("todosampleapp.android.library")
            pluginManager.apply("todosampleapp.android.hilt")

            dependencies {
                "implementation"(project(":core:domain"))
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-savedstate").get())
                "implementation"(libs.findLibrary("kotlinx-coroutines-core").get())

                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(libs.findLibrary("mockk").get())
            }

            guardFeatureDependencies()
        }
    }
}

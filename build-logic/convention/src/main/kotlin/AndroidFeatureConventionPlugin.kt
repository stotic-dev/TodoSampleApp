import com.example.todosampleapp.buildlogic.guardFeatureDependencies
import com.example.todosampleapp.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * feature:<name> 用。
 *
 * ViewModel / UiState、Navigation の Route 定義、ViewModel の状態を ui に渡す Entry Composable を置くモジュール。
 * 同じ feature の ui に依存する。他 feature への遷移はラムダで受け取り、直接依存しない。
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("todosampleapp.android.library.compose")
            pluginManager.apply("todosampleapp.android.hilt")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                "implementation"(project(":core:domain"))
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-savedstate").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                "implementation"(libs.findLibrary("androidx-navigation-compose").get())
                "implementation"(libs.findLibrary("androidx-hilt-navigation-compose").get())
                "implementation"(libs.findLibrary("kotlinx-coroutines-core").get())
                "implementation"(libs.findLibrary("kotlinx-serialization-json").get())

                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(libs.findLibrary("mockk").get())
            }

            guardFeatureDependencies()
        }
    }
}

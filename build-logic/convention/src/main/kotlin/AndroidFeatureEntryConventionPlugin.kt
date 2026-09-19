import com.example.todosampleapp.buildlogic.guardFeatureDependencies
import com.example.todosampleapp.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * feature:*:entry 用。
 *
 * Navigation の Route 定義と、ViewModel の状態を ui に渡す糊（Entry Composable）を置くモジュール。
 * 同じ feature の ui / presentation に依存する。他 feature への遷移はラムダで受け取り、直接依存しない。
 */
class AndroidFeatureEntryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("todosampleapp.android.library.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                "implementation"(project(":core:domain"))
                "implementation"(libs.findLibrary("androidx-navigation-compose").get())
                "implementation"(libs.findLibrary("androidx-hilt-navigation-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                "implementation"(libs.findLibrary("kotlinx-serialization-json").get())
            }

            guardFeatureDependencies()
        }
    }
}

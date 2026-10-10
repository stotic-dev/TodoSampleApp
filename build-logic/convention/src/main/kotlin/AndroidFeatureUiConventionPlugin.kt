import com.android.build.api.dsl.LibraryExtension
import com.example.todosampleapp.buildlogic.guardFeatureDependencies
import com.example.todosampleapp.buildlogic.libs
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

/**
 * feature:*:ui 用。
 *
 * ステートレスな Composable だけを置くモジュール。Hilt / ViewModel / Navigation には依存しない。
 */
class AndroidFeatureUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("todosampleapp.android.library.compose")
            pluginManager.apply("todosampleapp.android.test.roborazzi")

            dependencies {
                "implementation"(project(":core:designsystem"))
            }

            guardFeatureDependencies()
        }
    }
}

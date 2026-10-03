import com.android.build.api.dsl.LibraryExtension
import com.example.todosampleapp.buildlogic.configureKotlinAndroid
import com.example.todosampleapp.buildlogic.configureKtlint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Compose を使わない Android Library 共通設定 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid()
            }
            configureKtlint()
        }
    }
}

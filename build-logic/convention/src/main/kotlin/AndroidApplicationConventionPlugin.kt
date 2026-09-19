import com.android.build.api.dsl.ApplicationExtension
import com.example.todosampleapp.buildlogic.configureAndroidCompose
import com.example.todosampleapp.buildlogic.configureKotlinAndroid
import com.example.todosampleapp.buildlogic.configureKtlint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** :app 用。Android Application + Compose + ktlint */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid()
                defaultConfig.targetSdk = 36
                configureAndroidCompose(this)
            }
            configureKtlint()
        }
    }
}

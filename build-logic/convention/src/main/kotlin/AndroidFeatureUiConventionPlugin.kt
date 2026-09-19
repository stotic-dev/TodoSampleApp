import com.android.build.api.dsl.LibraryExtension
import com.example.todosampleapp.buildlogic.guardFeatureDependencies
import com.example.todosampleapp.buildlogic.libs
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
 * Roborazzi によるスナップショットテストはこのモジュールで実行する
 * （`./gradlew :feature:<name>:ui:recordRoborazziDebug`）。
 */
class AndroidFeatureUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("todosampleapp.android.library.compose")
            pluginManager.apply("io.github.takahirom.roborazzi")

            extensions.configure<LibraryExtension> {
                // Robolectric でテーマ・フォントなどのリソースを読めるようにする
                testOptions.unitTests.isIncludeAndroidResources = true
            }

            // Robolectric (SDK 36) が JDK 内部の FileDescriptor / SharedSecrets にアクセスするために必要
            tasks.withType<Test>().configureEach {
                jvmArgs(
                    "--add-opens=java.base/java.io=ALL-UNNAMED",
                    "--add-exports=java.base/jdk.internal.access=ALL-UNNAMED",
                )
            }

            dependencies {
                "implementation"(project(":core:designsystem"))

                // スナップショットテスト
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("androidx-junit").get())
                "testImplementation"(libs.findLibrary("androidx-compose-ui-test-junit4").get())
                "testImplementation"(libs.findLibrary("robolectric").get())
                "testImplementation"(libs.findLibrary("roborazzi").get())
                "testImplementation"(libs.findLibrary("roborazzi-compose").get())
                "testImplementation"(libs.findLibrary("roborazzi-junit-rule").get())
                "debugImplementation"(libs.findLibrary("androidx-compose-ui-test-manifest").get())
            }

            guardFeatureDependencies()
        }
    }
}

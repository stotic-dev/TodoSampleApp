import com.android.build.api.dsl.CommonExtension
import com.example.todosampleapp.buildlogic.libs
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class AndroidTestRoborazziConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.github.takahirom.roborazzi")
            }

            // Android モジュール (Library または App) の共通設定
            extensions.findByType(CommonExtension::class.java)?.apply {
                testOptions.unitTests.isIncludeAndroidResources = true
            }

            // Robolectric 用の JVM Args
            tasks.withType<Test>().configureEach {
                jvmArgs(
                    "--add-opens=java.base/java.io=ALL-UNNAMED",
                    "--add-exports=java.base/jdk.internal.access=ALL-UNNAMED",
                )
            }

            // ローカル実行時のガード設定
            val isCi = providers.environmentVariable("CI").map { it == "true" }.orElse(false)
            val allowLocalRecord = providers.gradleProperty("allowLocalRecord").map { true }.orElse(false)
            val isRecordRequested = gradle.startParameter.taskNames.any { it.contains("recordRoborazzi") }
            tasks.withType<Test>().configureEach {
                doFirst {
                    if (isRecordRequested && !isCi.get() && !allowLocalRecord.get()) {
                        throw GradleException(
                            "スナップショットのベースラインはローカルではなく CI で記録してください。" // 略
                        )
                    }
                }
            }

            // 共通の依存関係
            dependencies {
                "testImplementation"(libs.findLibrary("roborazzi.junit.rule").get())
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("androidx-junit").get())
                "testImplementation"(libs.findLibrary("androidx-compose-ui-test-junit4").get())
                "testImplementation"(libs.findLibrary("robolectric").get())
                "testImplementation"(libs.findLibrary("roborazzi").get())
                "testImplementation"(libs.findLibrary("roborazzi-compose").get())
                "testImplementation"(libs.findLibrary("roborazzi-junit-rule").get())
                "debugImplementation"(libs.findLibrary("androidx-compose-ui-test-manifest").get())
            }
        }
    }
}

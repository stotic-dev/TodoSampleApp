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
 * Roborazzi によるスナップショットテストはこのモジュールで実行する
 * （`./gradlew :feature:<name>:ui:verifyRoborazziDebug`）。
 *
 * ベースライン (src/test/screenshots 配下の PNG) の記録は描画環境を揃えるため CI でのみ行う。
 * ローカルでの record はデフォルトで失敗させる（-PallowLocalRecord で解除可）。
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

            // ベースラインの記録は CI (update-snapshots ワークフロー) に限定する。
            // 実際の記録は recordRoborazzi* に先行する Test タスク内で行われるため、Test タスク側で止める
            val isCi = providers.environmentVariable("CI").map { it == "true" }.orElse(false)
            val allowLocalRecord = providers.gradleProperty("allowLocalRecord").map { true }.orElse(false)
            val isRecordRequested = gradle.startParameter.taskNames.any { it.contains("recordRoborazzi") }
            tasks.withType<Test>().configureEach {
                doFirst {
                    if (isRecordRequested && !isCi.get() && !allowLocalRecord.get()) {
                        throw GradleException(
                            "スナップショットのベースラインはローカルではなく CI で記録してください。" +
                                "PR に update-snapshots ラベルを付けると CI が記録・コミットします。" +
                                "（差分の確認は compareRoborazziDebug、どうしてもローカルで記録する場合は -PallowLocalRecord）",
                        )
                    }
                }
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

pluginManagement {
    // Convention Plugin（build-logic）を取り込む
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TodoSampleApp"

// projects.core.domain のような型安全なプロジェクトアクセサを有効化
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

// core: 全レイヤーから参照される軽量モジュール（Android SDK 非依存 or Compose のみ）
include(":core:domain")
include(":core:designsystem")

// infra: data 層の実体（Room など）。app からのみ参照する
include(":infra:data")

// feature: 画面ごとに ui / presentation / entry の 3 分割
include(":feature:todolist:ui")
include(":feature:todolist:presentation")
include(":feature:todolist:entry")
include(":feature:todoadd:ui")
include(":feature:todoadd:presentation")
include(":feature:todoadd:entry")
include(":feature:tododetail:ui")
include(":feature:tododetail:presentation")
include(":feature:tododetail:entry")

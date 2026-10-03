// Convention Plugin 用の included build。
// ルートの settings.gradle.kts から includeBuild("build-logic") で取り込む。
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        // ルートと同じバージョンカタログを共有する
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")

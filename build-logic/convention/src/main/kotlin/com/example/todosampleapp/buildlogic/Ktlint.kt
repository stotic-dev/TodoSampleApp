package com.example.todosampleapp.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

/** 全モジュール共通の ktlint 設定 */
internal fun Project.configureKtlint() {
    pluginManager.apply("org.jlleitschuh.gradle.ktlint")

    extensions.configure<KtlintExtension> {
        // Android 向けのルールを有効化
        android.set(true)
        // コンソールにカラー付きで出力
        verbose.set(true)
        // CIで指摘があってもビルドを失敗させず、Reviewdogでコメントするために常にtrueに設定
        ignoreFailures.set(true)
        // Reviewdogで読み取るためのレポート形式(XML)を有効化
        reporters {
            reporter(ReporterType.CHECKSTYLE)
        }
        // 生成コード（KSP/Room/Hilt など）はチェック対象外
        filter {
            exclude { it.file.path.contains("generated/") }
        }
    }

    // ローカルビルド時にも ktlintCheck を実行する（警告のみ）
    // Android モジュールは preBuild、JVM モジュールは compileKotlin にぶら下げる
    tasks.matching { it.name == "preBuild" || it.name == "compileKotlin" }.configureEach {
        dependsOn("ktlintCheck")
    }
}

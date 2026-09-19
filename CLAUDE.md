# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

**TodoSampleApp** — Android Jetpack Compose を使ったサンプルアプリ。Feature ごとに `ui / presentation / entry` へ分割したマルチモジュール構成。

- パッケージ名: `com.example.todosampleapp`
- minSdk: 24 / compileSdk・targetSdk: 36
- 言語: Kotlin + Jetpack Compose (Material3) / Hilt / Room / Navigation Compose

```
app ──▶ feature:*:entry ──▶ feature:*:presentation ──▶ core:domain
 │            └──────────▶ feature:*:ui ──▶ core:designsystem
 └──▶ infra:data ──▶ core:domain
```

feature → infra、feature → 他 feature の依存は禁止。詳細は各ディレクトリを触ると自動ロードされる `.claude/rules/` を参照。

## ビルド・テストコマンド

```bash
./gradlew assembleDebug                                  # デバッグビルド
./gradlew test                                           # ユニットテスト（全モジュール）
./gradlew :feature:todolist:presentation:testDebugUnitTest  # モジュール単位
./gradlew :feature:todolist:ui:verifyRoborazziDebug      # スナップショットテスト
./gradlew lint                                           # Android Lint
./gradlew ktlintCheck                                    # コードスタイル
```

## ルールとスキル

| 目的 | 参照先 |
| --- | --- |
| モジュールの依存ルール・Convention Plugin | `.claude/rules/gradle-modules.md`（`*.gradle.kts` / `build-logic/` を触ると自動ロード） |
| `core` / `infra` の書き方 | `.claude/rules/core-infra.md` |
| `feature/*/ui` `presentation` `entry` の書き方 | `.claude/rules/feature-{ui,presentation,entry}.md` |
| 新しい画面（Feature）を追加する | `/add-feature` |
| スナップショットテストの記録・更新 | `/snapshot-test` |

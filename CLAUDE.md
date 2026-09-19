# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

**TodoSampleApp** — Android Jetpack Compose を使ったサンプルアプリ。Feature ごとに `feature:<name>`（ViewModel / Route）と `feature:<name>:ui`（ステートレス Composable）へ分割したマルチモジュール構成。

- パッケージ名: `com.example.todosampleapp`
- minSdk: 24 / compileSdk・targetSdk: 36
- 言語: Kotlin + Jetpack Compose (Material3) / Hilt / Room / Navigation Compose

```
app ──▶ feature:* ──▶ feature:*:ui ──▶ core:designsystem
 │          └──────▶ core:domain
 └──▶ infra:data ──▶ core:domain
```

feature → infra、feature → 他 feature の依存は禁止。詳細は各ディレクトリを触ると自動ロードされる `.claude/rules/` を参照。

## ビルド・テストコマンド

```bash
./gradlew assembleDebug                                  # デバッグビルド
./gradlew test                                           # ユニットテスト（全モジュール）
./gradlew :feature:todolist:testDebugUnitTest            # モジュール単位
./gradlew :feature:todolist:ui:compareRoborazziDebug     # スナップショットの差分確認（ベースライン更新は CI で行う）
./gradlew lint                                           # Android Lint
./gradlew ktlintCheck                                    # コードスタイル
```

## ルールとスキル

| 目的 | 参照先 |
| --- | --- |
| モジュールの依存ルール・Convention Plugin | `.claude/rules/gradle-modules.md`（`*.gradle.kts` / `build-logic/` を触ると自動ロード） |
| `core` / `infra` の書き方 | `.claude/rules/core-infra.md` |
| `feature/<name>` / `feature/<name>/ui` の書き方 | `.claude/rules/feature.md`, `.claude/rules/feature-ui.md` |
| 新しい画面（Feature）を追加する | `/add-feature` |
| スナップショットテストの差分確認・ベースライン更新（CI で行う） | `/snapshot-test` |

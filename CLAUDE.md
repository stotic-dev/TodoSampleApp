# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

**TodoSampleApp** — Android Jetpack Compose を使ったサンプルアプリ。シングルモジュール構成（`:app`）。

- パッケージ名: `com.example.todosampleapp`
- minSdk: 24 / compileSdk・targetSdk: 36
- 言語: Kotlin + Jetpack Compose (Material3)

## ビルド・テストコマンド

```bash
# デバッグビルド
./gradlew assembleDebug

# リリースビルド
./gradlew assembleRelease

# ユニットテスト
./gradlew test

# 特定のテストクラスのみ実行
./gradlew test --tests "com.example.todosampleapp.ExampleUnitTest"

# インストルメンテーションテスト（接続済みデバイス/エミュレータが必要）
./gradlew connectedAndroidTest

# lint
./gradlew lint

# クリーンビルド
./gradlew clean assembleDebug
```

## アーキテクチャ

現在はスターターテンプレートの段階で、`MainActivity` にすべての UI ロジックが集約されている。

- `MainActivity` — エントリポイント。`setContent` で Compose UI をセット
- `ui/theme/` — Material3 テーマ定義（`Color.kt`, `Theme.kt`, `Type.kt`）

今後 Todo 機能を追加する場合は、以下のレイヤー構成を推奨:
- `data/` — Repository + Room/DataStore
- `domain/` — UseCase
- `ui/` — Screen Composable + ViewModel (StateFlow ベース)

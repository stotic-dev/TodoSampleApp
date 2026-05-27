# TodoSampleApp

Android Jetpack Compose を使ったサンプルアプリ。シングルモジュール構成（`:app`）。

- パッケージ名: `com.example.todosampleapp`
- minSdk: 24 / compileSdk・targetSdk: 36
- 言語: Kotlin + Jetpack Compose (Material3)

## 開発環境

| 項目 | 内容 |
| --- | --- |
| 言語 | Kotlin `2.2.10` |
| ビルドツール | Gradle (Kotlin DSL) + Android Gradle Plugin `9.1.0` |
| JDK | 17（CI 実行環境）/ ソース・ターゲット互換性は Java 11 |
| UI | Jetpack Compose (Compose BOM `2024.09.00`, Material3) |
| DI | Hilt `2.59.2`（KSP 経由） |
| DB | Room `2.7.1`（KSP 経由） |
| 画面遷移 | Navigation Compose `2.8.4` |
| シリアライズ | kotlinx.serialization |

依存ライブラリのバージョンは [`gradle/libs.versions.toml`](gradle/libs.versions.toml)（Version Catalog）で一元管理しています。

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

# Android Lint
./gradlew lint

# クリーンビルド
./gradlew clean assembleDebug
```

## リンター / コードスタイル

[ktlint](https://github.com/JLLeitschuh/ktlint-gradle)（Gradle プラグイン `14.2.0`）を採用しています。

### コードスタイル設定

スタイルは [`.editorconfig`](.editorconfig) で定義しています。

- `ktlint_official` の公式コードスタイルを採用
- 最大行長: 120
- ワイルドカードインポート禁止（`no-wildcard-imports`）
- `@Composable` 関数は PascalCase 慣習のため `function-naming` ルールの対象外
- インデント: スペース 4 / 改行コード: LF / 末尾改行を付与

### ktlint の動作

```bash
# 静的解析（チェックのみ）
./gradlew ktlintCheck

# 自動整形
./gradlew ktlintFormat
```

- **ローカルビルド**: `preBuild` で `ktlintCheck` を自動実行しますが、違反があっても**警告のみ**でビルドは失敗しません（`ignoreFailures = true`）。
- **CI / 厳格モード**: `-PktlintStrict=true` を指定すると、違反でビルドを失敗させます。

  ```bash
  ./gradlew ktlintCheck -PktlintStrict=true
  ```

- 生成コード（KSP / Room / Hilt などの `generated/` 配下）はチェック対象外です。

## CI

GitHub Actions でビルド・テストを自動実行します（ワークフロー定義: [`.github/workflows/ci.yml`](.github/workflows/ci.yml)）。

- **トリガー**: 全ブランチ宛ての Pull Request
- **実行環境**: `ubuntu-latest` / JDK 17 (Temurin)
- **同時実行制御**: 同一 PR への新しい push があると、進行中の古いジョブをキャンセル

### 実行ステップ

| ステップ | コマンド | 内容 |
| --- | --- | --- |
| Ktlint | `./gradlew ktlintCheck -PktlintStrict=true` | コードスタイルチェック（厳格モード・違反で失敗） |
| Lint | `./gradlew lint` | Android Lint |
| Unit test | `./gradlew test` | ユニットテスト |
| Build debug | `./gradlew assembleDebug` | デバッグビルド |

実行後、`app/build/reports/` 配下の各種レポートをアーティファクト（保持期間 7 日）としてアップロードします。

## ディレクトリ構成

```
.
├── app/                       # アプリモジュール
├── gradle/
│   └── libs.versions.toml     # Version Catalog（依存ライブラリ管理）
├── .github/workflows/ci.yml   # CI 定義
├── .editorconfig              # コードスタイル（ktlint 設定）
├── build.gradle.kts           # ルートビルドスクリプト
└── settings.gradle.kts        # モジュール構成
```

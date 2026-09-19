# TodoSampleApp

Android Jetpack Compose を使ったサンプルアプリ。Feature ごとに `feature:<name>`（ViewModel / Route）と `feature:<name>:ui`（ステートレス Composable）へ分割したマルチモジュール構成。

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

# 特定モジュールのテストのみ実行
./gradlew :core:domain:test
./gradlew :feature:todolist:testDebugUnitTest

# スナップショットテスト（feature:*:ui + core だけがビルドされる）
./gradlew :feature:todolist:ui:verifyRoborazziDebug   # ベースラインと比較
./gradlew :feature:todolist:ui:compareRoborazziDebug  # 差分画像を build/outputs/roborazzi に出力
# ベースライン (src/test/screenshots/*.png) の記録はローカルでは行わず、
# PR に update-snapshots ラベルを付けて CI に記録させる（下記「スナップショットテスト」参照）

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
| Screenshot test | `./gradlew verifyRoborazziDebug` | Roborazzi によるスナップショット検証（`feature:*:ui`） |

### スナップショットテスト

Robolectric の描画は OS やフォントで微妙に変わるため、ベースライン PNG は **CI (ubuntu / JDK 17) でのみ記録**します。ローカルの `recordRoborazzi*` は Convention Plugin が失敗させます（`-PallowLocalRecord` で解除可）。

1. Composable を変更したら `./gradlew :feature:<name>:ui:compareRoborazziDebug` で差分を目視
2. PR に `update-snapshots` ラベルを付ける（`gh pr edit <番号> --add-label update-snapshots`）
3. [`update-snapshots.yml`](.github/workflows/update-snapshots.yml) が `recordRoborazziDebug` を実行し、PNG を PR ブランチにコミットして新しいコミットの CI を起動（`GITHUB_TOKEN` による push では CI run が承認待ちになるため自動承認する。`SNAPSHOT_PUSH_TOKEN` に PAT を登録すれば通常の push として CI が走る）

CI の `verifyRoborazziDebug` が失敗した場合は、アーティファクト内の `**/build/outputs/roborazzi/*_compare.png` で差分を確認できます。
| Build debug | `./gradlew assembleDebug` | デバッグビルド |

実行後、各モジュールの `build/reports/` と Roborazzi の差分画像をアーティファクト（保持期間 7 日）としてアップロードします。

## モジュール構成

```
.
├── app/                       # Application / MainActivity / AppNavHost（画面遷移の配線）/ Hilt の集約
├── build-logic/               # Convention Plugin（各モジュール共通のビルド設定）
├── core/
│   ├── domain/                # 純 Kotlin(JVM)。ドメインモデルと Repository の interface
│   └── designsystem/          # AppTheme などの共通 UI 部品
├── infra/
│   └── data/                  # Room / RepositoryImpl / Hilt Module（app からのみ参照）
├── feature/
│   ├── todolist/              # ViewModel / UiState / Route 定義 / Entry Composable
│   │   └── ui/                # ステートレスな Composable + スナップショットテスト
│   ├── todoadd/               # 同上
│   └── tododetail/            # 同上
├── gradle/
│   └── libs.versions.toml     # Version Catalog（依存ライブラリ管理）
├── .github/workflows/ci.yml   # CI 定義
├── .editorconfig              # コードスタイル（ktlint 設定）
├── build.gradle.kts           # ルートビルドスクリプト
└── settings.gradle.kts        # モジュール構成
```

### 依存の向き

```
app ──▶ feature:* ──▶ feature:*:ui ──▶ core:designsystem
 │          └──────▶ core:domain
 └──▶ infra:data ──▶ core:domain
```

- **feature は infra に依存しない**。`core:domain` の interface だけを見て、実体は `app` で Hilt が束ねる。この制約は Convention Plugin がビルド時に検証する。
- **feature 同士も依存しない**。他画面への遷移は `feature:<name>` の `NavGraphBuilder` 拡張がラムダで受け取り、`app` の `AppNavHost` が配線する。
- **`ui` は Hilt / ViewModel / Navigation に依存しない**。そのためスナップショットテストは `ui` + `core:designsystem` + `core:domain` だけをビルドして実行できる。

### Convention Plugin

各モジュールの `build.gradle.kts` は `build-logic/convention` で定義したプラグインを適用するだけで済む。

| プラグイン | 用途 |
| --- | --- |
| `todosampleapp.android.application` | `:app` |
| `todosampleapp.android.library` / `.library.compose` | Android Library（Compose 有無） |
| `todosampleapp.android.hilt` | `@HiltViewModel` / `@Module` を持つモジュール |
| `todosampleapp.android.feature` | `feature:<name>`（Compose + Hilt + Navigation + Serialization） |
| `todosampleapp.android.feature.ui` | `feature:*:ui`（Compose + Roborazzi） |
| `todosampleapp.jvm.library` | `core:domain` などの純 Kotlin モジュール |

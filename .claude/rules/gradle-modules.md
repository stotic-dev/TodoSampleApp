---
paths:
  - "**/*.gradle.kts"
  - "build-logic/**"
  - "gradle/libs.versions.toml"
---

# モジュール構成とビルド設定

## 依存の向き

```
app ──▶ feature:* ──▶ feature:*:ui ──▶ core:designsystem
 │          └──────▶ core:domain
 └──▶ infra:data ──▶ core:domain
```

| モジュール | 役割 | 依存してよいもの |
| --- | --- | --- |
| `core:domain` | 純 Kotlin(JVM)。モデルと Repository の interface | なし |
| `core:designsystem` | `AppTheme` などの共通 UI 部品 | Compose のみ |
| `infra:data` | Room / RepositoryImpl / Hilt Module | `core:domain` |
| `feature:<name>` | `@HiltViewModel` / UiState / Route 定義 / Entry Composable | 同 feature の `ui`, `core:domain` |
| `feature:<name>:ui` | ステートレスな Composable + スナップショットテスト | `core:designsystem`, `core:domain` |
| `app` | Application / MainActivity / AppNavHost / Hilt 集約 | すべて |

- **feature → infra は禁止**。Convention Plugin (`guardFeatureDependencies`) がビルド時にエラーにする。feature は `core:domain` の interface だけを見て、実体は `app` で Hilt が束ねる。infra に依存させると Room/KSP のビルドが feature に波及する
- **feature 同士は依存しない**。画面遷移は `app/navigation/AppNavHost.kt` で配線する
- `google-services` プラグインと `google-services.json` は `:app` だけ

## Convention Plugin（build-logic/convention）

各モジュールの `build.gradle.kts` は対応するプラグインを適用し、`namespace` と固有の依存だけを書く。`compileSdk` / `minSdk` / `compileOptions` / ktlint などの共通設定はモジュール側に書かず、プラグイン側（`build-logic/convention/src/main/kotlin/`）を修正する。

| プラグイン | 用途 |
| --- | --- |
| `todosampleapp.android.application` | `:app` |
| `todosampleapp.android.library` / `.library.compose` | Android Library（Compose 有無） |
| `todosampleapp.android.hilt` | `@HiltViewModel` / `@Module` を持つモジュール |
| `todosampleapp.android.feature` | `feature:<name>`（Compose + Hilt + Navigation + Serialization + 依存ガード） |
| `todosampleapp.android.feature.ui` | `feature:*:ui`（Compose + Roborazzi + 依存ガード + ローカル record 禁止） |
| `todosampleapp.jvm.library` | `core:domain` などの純 Kotlin モジュール |

- 新しいモジュールは `settings.gradle.kts` に `include` し、`build.gradle.kts` では `alias(libs.plugins.todosampleapp.xxx)` で適用する
- カタログの `todosampleapp-*` プラグインエントリには `version` を書かない。`:feature:<name>` と子の `:feature:<name>:ui` のように親子関係のあるプロジェクトで同じ Convention Plugin をバージョン付きで要求すると「already on the classpath with an unknown version」で失敗する
- モジュール間依存は `projects.core.domain` のような型安全アクセサで書く
- ライブラリのバージョンは `gradle/libs.versions.toml` に集約する。build-logic から参照するプラグイン本体も `*-gradlePlugin` としてここに置く
- AGP 9 は Kotlin がビルトインなので `org.jetbrains.kotlin.android` は適用しない。`CommonExtension` には `defaultConfig { }` 形式の invoke が無いため、Convention Plugin 内では `defaultConfig.minSdk = 24` のようにプロパティで書く

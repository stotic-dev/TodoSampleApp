---
paths:
  - "core/**"
  - "infra/**"
---

# core / infra モジュール

## core:domain

- 純 Kotlin(JVM) モジュール。`android.*` / `androidx.*` を import しない。Android SDK に依存しないことでビルドとテストが最速になり、すべての feature がここに依存しても負担にならない
- ドメインモデル（`TodoItem`, `TodoList`）と Repository の **interface**（`TodoRepository`）を置く。実装は置かない
- テストは `src/test/kotlin` に JUnit4 で書く。Robolectric や coroutines-test は不要な設計を保つ

## core:designsystem

- `AppTheme` / Color / Type と、複数 feature で共有する Composable だけを置く
- 特定 feature の画面部品を持ち込まない。feature 固有のものは `feature/<name>/ui` に置く
- Google Fonts の証明書 (`res/values/font_certs.xml`) はここが持つ。`R` は `com.example.todosampleapp.designsystem.R`

## infra:data

- `core:domain` の interface の実装（`TodoRepositoryImpl`）、Room（`AppDatabase` / Dao / Entity）、Hilt Module（`@Binds` / `@Provides`）を置く
- 参照するのは `:app` だけ。feature から参照してはいけない（Convention Plugin が検証する）
- Entity ↔ ドメインモデルの変換は infra 側で閉じる。Entity を外に漏らさない
- 通信や Firebase など別種のインフラを追加するときは `infra/data` に混ぜず、`infra/network` のような兄弟モジュールを作る。重い SDK や KSP の影響範囲をモジュール内に閉じ込めるため

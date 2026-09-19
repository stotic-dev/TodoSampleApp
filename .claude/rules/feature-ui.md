---
paths:
  - "feature/*/ui/**"
---

# feature:*:ui モジュール

ステートレスな Composable だけを置く。このモジュールが Hilt / ViewModel / Navigation に依存しないことで、スナップショットテストが `ui` + `core:designsystem` + `core:domain` だけをビルドして実行できる。

## Composable の書き方

- 状態とイベントハンドラはすべて引数で受け取る。`hiltViewModel()` や `collectAsStateWithLifecycle()` は使わない（それらは `entry` の仕事）
- 引数の型は **プリミティブか `core:domain` のモデル**にする。presentation の UiState 型を受け取ると `ui → presentation` の依存が生まれ、presentation のテストに Compose が必要になる
  - 例: `TodoAddScreen(title: String, detail: String, canSave: Boolean, ...)` — `AddTodoInputItem` は受け取らない
- 画面全体の Composable は `<Name>Screen` として public にし、その中の部品は `private` にする
- `@Preview` は `AppTheme { }` で包む

## スナップショットテスト

- `src/test/kotlin/.../ui/<Name>ScreenshotTest.kt` に置く。既存の `TodoListScreenshotTest` を雛形にする
- `AppTheme(dynamicColor = false)` で包む。dynamic color は実行環境の壁紙色に依存するため
- `RoborazziRule.Options(outputDirectoryPath = "src/test/screenshots")` で画像をソースツリーに保存し、PNG をコミットする
- 画面の状態ごと（空・データあり・ローディングなど）に 1 テストを用意する
- Composable を変更したら `./gradlew :feature:<name>:ui:recordRoborazziDebug` で画像を更新し、`verifyRoborazziDebug` が通ることを確認する。手順は `/snapshot-test` スキルを参照

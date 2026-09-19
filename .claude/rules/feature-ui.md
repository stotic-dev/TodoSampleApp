---
paths:
  - "feature/*/ui/**"
---

# feature:*:ui モジュール

ステートレスな Composable だけを置く。このモジュールが Hilt / ViewModel / Navigation に依存しないことで、スナップショットテストが `ui` + `core:designsystem` + `core:domain` だけをビルドして実行できる。ViewModel や Route は親の `feature/<name>/src` にある（`feature.md` 参照）。

## Composable の書き方

- 状態とイベントハンドラはすべて引数で受け取る。`hiltViewModel()` や `collectAsStateWithLifecycle()` は使わない（それらは `feature/<name>` の Entry の仕事）
- 引数の型は **プリミティブか `core:domain` のモデル**にする。`feature/<name>` の UiState 型を受け取ると `ui → feature` の循環依存になる
  - 例: `TodoAddScreen(title: String, detail: String, canSave: Boolean, ...)` — `AddTodoInputItem` は受け取らない
- 画面全体の Composable は `<Name>Screen` として public にし、その中の部品は `private` にする
- `@Preview` は `AppTheme { }` で包む

## スナップショットテスト

- `src/test/kotlin/.../ui/<Name>ScreenshotTest.kt` に置く。既存の `TodoListScreenshotTest` を雛形にする
- `AppTheme(dynamicColor = false)` で包む。dynamic color は実行環境の壁紙色に依存するため
- `RoborazziRule.Options(outputDirectoryPath = "src/test/screenshots")` でベースライン PNG をソースツリーに置く
- 画面の状態ごと（空・データあり・ローディングなど）に 1 テストを用意する
- **ベースラインの記録はローカルではなく CI で行う**（環境差分を避けるため。ローカルの `recordRoborazzi*` は Convention Plugin が失敗させる）。Composable を変更したら `compareRoborazziDebug` で差分を目視し、PR に `update-snapshots` ラベルを付けて CI に記録させる。手順は `/snapshot-test` スキルを参照

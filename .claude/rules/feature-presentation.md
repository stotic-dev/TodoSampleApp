---
paths:
  - "feature/*/presentation/**"
---

# feature:*:presentation モジュール

ViewModel と UiState を置く。Compose / Navigation には依存しない。ViewModel のテストを JVM 上で軽く回すため。

## ViewModel

- `@HiltViewModel` + `@Inject constructor` で `core:domain` の Repository interface を受け取る
- 状態は `StateFlow` で公開する。`MutableStateFlow` は `private` にして `asStateFlow()` で出す
- 画面遷移そのものは扱わない。「保存が完了した」のような状態（例: `TodoAddUIState.COMPLETE`）を公開し、遷移は `entry` が `LaunchedEffect` で拾う
- Navigation の Route 引数は `savedStateHandle[KEY]` で読む。`toRoute<>()` は entry の Route 型に依存するため使わない。キーは `const val <NAME>_ARG = "id"` として公開し、entry 側の Route data class のプロパティ名と一致させる（例: `TODO_DETAIL_ID_ARG`）

## UiState

- 入力値のバリデーションなどを含む UiState（例: `AddTodoInputItem`）はこのモジュールに置く。`ui` には分解してプリミティブで渡す

## テスト

- `src/test/kotlin` に置き、mockk で Repository をモックする。既存の `TodoAddViewModelTest` / `TodoListViewModelTest` を雛形にする
- `Dispatchers.setMain(UnconfinedTestDispatcher())` を `@Before` で設定し、`@After` で `resetMain()` する
- ViewModel が構築時に Flow を購読する場合は、スタブ設定後に ViewModel を生成する

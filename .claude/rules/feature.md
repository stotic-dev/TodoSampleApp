---
paths:
  - "feature/*/src/**"
  - "feature/*/build.gradle.kts"
  - "app/src/main/java/**/navigation/**"
---

# feature:<name> モジュールと画面遷移

`feature/<name>/src` には ViewModel / UiState と、Navigation の Route 定義・ViewModel の状態を `ui` に渡す Entry Composable を置く。Composable の実体は `feature/<name>/ui` にある（`feature-ui.md` 参照）。feature 同士を疎結合に保つため、**他 feature の Route やクラスを参照しない**。

## ViewModel / UiState

- `@HiltViewModel` + `@Inject constructor` で `core:domain` の Repository interface を受け取る
- 状態は `StateFlow` で公開する。`MutableStateFlow` は `private` にして `asStateFlow()` で出す
- 画面遷移そのものは扱わない。「保存が完了した」のような状態（例: `TodoAddUIState.COMPLETE`）を公開し、Entry が `LaunchedEffect` で拾って `onBack()` を呼ぶ
- Route 引数は `savedStateHandle[KEY]` で読む。`toRoute<>()` でもよいが、キー定数（例: `TODO_DETAIL_ID_ARG = "id"`）を Route data class のプロパティ名と一致させておくとテストで `SavedStateHandle(mapOf(KEY to 1))` が作りやすい
- 入力バリデーションなどを含む UiState（例: `AddTodoInputItem`）はここに置き、`ui` にはプリミティブに分解して渡す

## Route と Entry（`<Name>Navigation.kt`）

```kotlin
@Serializable
data object TodoListRoute            // 引数があれば data class TodoDetailRoute(val id: Int)

fun NavGraphBuilder.todoListScreen(
    onNavigateToAdd: () -> Unit,     // 他画面への遷移はラムダで受け取る
    onNavigateToDetail: (todoId: Int) -> Unit,
) {
    composable<TodoListRoute> { TodoListEntry(onNavigateToAdd, onNavigateToDetail) }
}

@Composable
internal fun TodoListEntry(..., viewModel: TodoListViewModel = hiltViewModel()) {
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    TodoListScreen(todos = todos, ...)   // ui にはプリミティブ / domain モデルを渡す
}
```

- Entry Composable は `internal` にし、公開 API は Route と `NavGraphBuilder` 拡張だけにする
- `hiltViewModel()` / `collectAsStateWithLifecycle()` を呼ぶのは Entry だけ。`ui` に持ち込まない

## テスト

- `src/test/kotlin` に置き、mockk で Repository をモックする。雛形は `TodoAddViewModelTest` / `TodoListViewModelTest`
- `Dispatchers.setMain(UnconfinedTestDispatcher())` を `@Before` で設定し、`@After` で `resetMain()` する
- ViewModel が構築時に Flow を購読する場合は、スタブ設定後に ViewModel を生成する

## app 側の配線（`app/navigation/AppNavHost.kt`）

- 各 feature の `NavGraphBuilder.<name>Screen(...)` を呼び、ラムダに `navController.navigate(OtherRoute)` / `popBackStack()` を渡す
- feature を追加したら `app/build.gradle.kts` に `implementation(projects.feature.<name>)` を追加してからここに登録する

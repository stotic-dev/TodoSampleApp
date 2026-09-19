---
paths:
  - "feature/*/entry/**"
  - "app/src/main/java/**/navigation/**"
---

# feature:*:entry と app の画面遷移

`entry` は Navigation の Route 定義と、ViewModel の状態を `ui` に渡す糊。feature 同士を疎結合に保つため、**他 feature の Route を参照しない**。

## entry に置くもの（`<Name>Navigation.kt`）

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

- Route の引数プロパティ名は presentation 側の `SavedStateHandle` キー（`XXX_ARG`）と一致させる
- 「完了したら戻る」のような遷移は `LaunchedEffect(state)` で presentation の状態を監視して `onBack()` を呼ぶ
- Entry Composable は `internal` にし、公開 API は Route と `NavGraphBuilder` 拡張だけにする

## app 側の配線（`app/navigation/AppNavHost.kt`）

- 各 feature の `NavGraphBuilder.<name>Screen(...)` を呼び、ラムダに `navController.navigate(OtherRoute)` / `popBackStack()` を渡す
- feature を追加したら `app/build.gradle.kts` に `implementation(projects.feature.<name>.entry)` を追加してからここに登録する

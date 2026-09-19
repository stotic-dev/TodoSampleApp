---
name: add-feature
description: TodoSampleApp に新しい画面（Feature）を追加する手順。feature/<name>/{ui,presentation,entry} の 3 モジュールを作り、settings.gradle.kts への include、Convention Plugin の適用、Route と NavGraphBuilder 拡張、AppNavHost への配線、スナップショットテストの記録までを一通り行う。「〇〇画面を追加して」「新しい Feature を作って」「編集画面がほしい」など、新しい画面やモジュールを増やす依頼では、単一モジュール時代の感覚で app 配下に書き始めないよう必ずこのスキルを使う。
---

# 新しい Feature を追加する

この repo は Feature ごとに `ui` / `presentation` / `entry` の 3 モジュールに分割している。1 画面 = 3 モジュールなので手順が多いが、テンプレート化されているので順に埋めるだけでよい。以下 `<name>` は小文字の Feature 名（例: `todoedit`）、`<Name>` は PascalCase（例: `TodoEdit`）。

## 1. モジュールを登録する

`settings.gradle.kts` の feature ブロックに 3 行追加する。

```kotlin
include(":feature:<name>:ui")
include(":feature:<name>:presentation")
include(":feature:<name>:entry")
```

## 2. build.gradle.kts を 3 つ作る

`namespace` と固有の依存だけを書く。共通設定は Convention Plugin が持つので `android {}` に `compileSdk` などを書かない。

```kotlin
// feature/<name>/ui/build.gradle.kts
plugins { alias(libs.plugins.todosampleapp.android.feature.ui) }
android { namespace = "com.example.todosampleapp.feature.<name>.ui" }
dependencies { implementation(projects.core.domain) }   // domain モデルを表示するときだけ

// feature/<name>/presentation/build.gradle.kts
plugins { alias(libs.plugins.todosampleapp.android.feature.presentation) }
android { namespace = "com.example.todosampleapp.feature.<name>.presentation" }

// feature/<name>/entry/build.gradle.kts
plugins { alias(libs.plugins.todosampleapp.android.feature.entry) }
android { namespace = "com.example.todosampleapp.feature.<name>.entry" }
dependencies {
    implementation(projects.feature.<name>.ui)
    implementation(projects.feature.<name>.presentation)
}
```

## 3. ui — ステートレスな Screen

`feature/<name>/ui/src/main/kotlin/com/example/todosampleapp/feature/<name>/ui/<Name>Screen.kt`

- 引数はプリミティブか `core:domain` のモデル。ViewModel や UiState 型を受け取らない
- `@Preview` を `AppTheme { }` で包んで用意する
- 既存の `feature/todoadd/ui/.../TodoAddScreen.kt` が最もシンプルな雛形

## 4. presentation — ViewModel

`feature/<name>/presentation/src/main/kotlin/com/example/todosampleapp/feature/<name>/presentation/<Name>ViewModel.kt`

- `@HiltViewModel` + `@Inject constructor(repository: TodoRepository)`
- Route 引数を受け取るなら `const val <NAME>_ID_ARG = "id"` を定義し `savedStateHandle[<NAME>_ID_ARG]` で読む（雛形: `TodoDetailViewModel`）
- 遷移のトリガーは状態として公開する（雛形: `TodoAddViewModel` の `TodoAddUIState.COMPLETE`）
- テストを `src/test/kotlin/...` に追加する（雛形: `TodoAddViewModelTest`）

## 5. entry — Route と NavGraph 登録

`feature/<name>/entry/src/main/kotlin/com/example/todosampleapp/feature/<name>/entry/<Name>Navigation.kt`

```kotlin
@Serializable
data object <Name>Route                 // 引数付きなら data class <Name>Route(val id: Int)

fun NavGraphBuilder.<name>Screen(
    onBack: () -> Unit,                 // 必要な遷移をラムダで列挙する
) {
    composable<<Name>Route> { <Name>Entry(onBack = onBack) }
}

@Composable
internal fun <Name>Entry(
    onBack: () -> Unit,
    viewModel: <Name>ViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    <Name>Screen(/* state を分解して渡す */)
}
```

他 feature の Route をここで import してはいけない。遷移先は app が決める。

## 6. app に配線する

1. `app/build.gradle.kts` に `implementation(projects.feature.<name>.entry)` を追加
2. `app/src/main/java/com/example/todosampleapp/navigation/AppNavHost.kt` で `<name>Screen(...)` を呼び、ラムダに `navController.navigate(...)` / `popBackStack()` を渡す
3. 既存画面から遷移させるなら、その画面の `entry` に `onNavigateTo<Name>` ラムダを追加し、AppNavHost で結ぶ

## 7. スナップショットテストを追加して検証する

`feature/<name>/ui/src/test/kotlin/com/example/todosampleapp/feature/<name>/ui/<Name>ScreenshotTest.kt` を `TodoListScreenshotTest` を雛形に作り、状態ごとにテストを用意する。

```bash
./gradlew :feature:<name>:ui:recordRoborazziDebug   # PNG を src/test/screenshots に記録
./gradlew assembleDebug test verifyRoborazziDebug   # 全体が通ることを確認
```

記録した PNG はコミットに含める。画像の扱いに迷ったら `/snapshot-test` を参照。

## チェックリスト

- [ ] `settings.gradle.kts` に 3 モジュールを include した
- [ ] `ui` に `hiltViewModel` / `ViewModel` / `navigation` の import が無い
- [ ] `presentation` に Compose の import が無い
- [ ] `entry` が他 feature の Route を import していない
- [ ] `app/build.gradle.kts` に entry を追加し、`AppNavHost` に登録した
- [ ] スナップショット PNG を記録した

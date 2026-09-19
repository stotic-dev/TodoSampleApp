---
name: add-feature
description: TodoSampleApp に新しい画面（Feature）を追加する手順。feature/<name>（ViewModel / Route / Entry）と feature/<name>/ui（ステートレス Composable）の 2 モジュールを作り、settings.gradle.kts への include、Convention Plugin の適用、Route と NavGraphBuilder 拡張、AppNavHost への配線、スナップショットテストの追加までを一通り行う。「〇〇画面を追加して」「新しい Feature を作って」「編集画面がほしい」など、新しい画面やモジュールを増やす依頼では、単一モジュール時代の感覚で app 配下に書き始めないよう必ずこのスキルを使う。
---

# 新しい Feature を追加する

この repo は Feature ごとに `feature/<name>`（ViewModel / Route / Entry）と `feature/<name>/ui`（ステートレス Composable）の 2 モジュールに分割している。以下 `<name>` は小文字の Feature 名（例: `todoedit`）、`<Name>` は PascalCase（例: `TodoEdit`）。

## 1. モジュールを登録する

`settings.gradle.kts` の feature ブロックに追加する。

```kotlin
include(":feature:<name>", ":feature:<name>:ui")
```

## 2. build.gradle.kts を 2 つ作る

`namespace` と固有の依存だけを書く。共通設定は Convention Plugin が持つので `android {}` に `compileSdk` などを書かない。

```kotlin
// feature/<name>/build.gradle.kts
plugins { alias(libs.plugins.todosampleapp.android.feature) }
android { namespace = "com.example.todosampleapp.feature.<name>" }
dependencies { implementation(projects.feature.<name>.ui) }

// feature/<name>/ui/build.gradle.kts
plugins { alias(libs.plugins.todosampleapp.android.feature.ui) }
android { namespace = "com.example.todosampleapp.feature.<name>.ui" }
dependencies { implementation(projects.core.domain) }   // domain モデルを表示するときだけ
```

## 3. ui — ステートレスな Screen

`feature/<name>/ui/src/main/kotlin/com/example/todosampleapp/feature/<name>/ui/<Name>Screen.kt`

- 引数はプリミティブか `core:domain` のモデル。ViewModel や UiState 型を受け取らない
- `@Preview` を `AppTheme { }` で包んで用意する
- 既存の `feature/todoadd/ui/.../TodoAddScreen.kt` が最もシンプルな雛形

## 4. feature — ViewModel

`feature/<name>/src/main/kotlin/com/example/todosampleapp/feature/<name>/<Name>ViewModel.kt`

- `@HiltViewModel` + `@Inject constructor(repository: TodoRepository)`
- Route 引数を受け取るなら `const val <NAME>_ID_ARG = "id"` を定義し `savedStateHandle[<NAME>_ID_ARG]` で読む（雛形: `TodoDetailViewModel`）
- 遷移のトリガーは状態として公開する（雛形: `TodoAddViewModel` の `TodoAddUIState.COMPLETE`）
- テストを `feature/<name>/src/test/kotlin/...` に追加する（雛形: `TodoAddViewModelTest`）

## 5. feature — Route と NavGraph 登録

`feature/<name>/src/main/kotlin/com/example/todosampleapp/feature/<name>/<Name>Navigation.kt`

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

1. `app/build.gradle.kts` に `implementation(projects.feature.<name>)` を追加
2. `app/src/main/java/com/example/todosampleapp/navigation/AppNavHost.kt` で `<name>Screen(...)` を呼び、ラムダに `navController.navigate(...)` / `popBackStack()` を渡す
3. 既存画面から遷移させるなら、その画面の `<Name>Navigation.kt` に `onNavigateTo<Name>` ラムダを追加し、AppNavHost で結ぶ

## 7. スナップショットテストを追加して検証する

`feature/<name>/ui/src/test/kotlin/com/example/todosampleapp/feature/<name>/ui/<Name>ScreenshotTest.kt` を `TodoListScreenshotTest` を雛形に作り、状態ごとにテストを用意する。

```bash
./gradlew assembleDebug test                          # ビルドとユニットテスト
./gradlew :feature:<name>:ui:compareRoborazziDebug    # 画像を目視確認（build/outputs/roborazzi/）
```

ベースライン PNG は**ローカルでは記録しない**（環境差分を避けるため、Convention Plugin が `recordRoborazzi*` を失敗させる）。PR を作ったら `update-snapshots` ラベルを付けて CI に記録・コミットさせる。詳細は `/snapshot-test`。

## チェックリスト

- [ ] `settings.gradle.kts` に 2 モジュールを include した
- [ ] `ui` に `hiltViewModel` / `ViewModel` / `navigation` の import が無い
- [ ] `feature/<name>` が他 feature のクラスを import していない
- [ ] `app/build.gradle.kts` に feature を追加し、`AppNavHost` に登録した
- [ ] スナップショットテストを追加し、PR に `update-snapshots` ラベルを付けてベースラインを記録した

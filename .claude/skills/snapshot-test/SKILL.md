---
name: snapshot-test
description: Roborazzi によるスナップショット（スクリーンショット）テストの記録・検証・差分確認・更新の手順。feature/*/ui の Composable を変更したとき、スナップショットの差分で CI が落ちたとき、新しい画面状態のスナップショットを追加したいとき、「スクリーンショットテストを更新して」「verifyRoborazzi が失敗する」「画像の差分を見たい」といった依頼で使う。UI を変更した後に画像を更新し忘れると CI で落ちるので、Composable を触ったら必ずこのスキルの手順で record まで行う。
---

# スナップショットテストの運用

スナップショットテストは `feature/*/ui` モジュールにだけ置く。`ui` は Hilt / ViewModel / Navigation に依存しないので、実行時にビルドされるのは `ui` + `core:designsystem` + `core:domain` だけで済む。全体ビルドを避けるため、**必ずモジュールを指定して実行する**。

## コマンド

```bash
# 記録: src/test/screenshots/ に PNG を保存（初回・UI 変更後）
./gradlew :feature:<name>:ui:recordRoborazziDebug

# 検証: 保存済み PNG と比較し、差分があれば失敗（CI はこれを実行）
./gradlew :feature:<name>:ui:verifyRoborazziDebug

# 差分確認: 失敗せずに比較画像を build/outputs/roborazzi/ に出力
./gradlew :feature:<name>:ui:compareRoborazziDebug
```

全 feature を対象にするなら `./gradlew verifyRoborazziDebug`（Roborazzi プラグインは ui モジュールにしか適用されないので、それ以外はスキップされる）。

## UI を変更したときの流れ

1. Composable を修正する
2. `compareRoborazziDebug` を実行し、`feature/<name>/ui/build/outputs/roborazzi/*_compare.png` を Read ツールで開いて、意図した差分だけかを確認する（左: 期待、中: 差分、右: 実際）
3. 意図どおりなら `recordRoborazziDebug` で PNG を更新する
4. 更新した `src/test/screenshots/*.png` をコミットに含める

差分を見ずに record だけすると、意図しない崩れも「正」として固定されてしまう。必ず compare で目視してから record する。

## テストを追加するとき

- ファイル: `feature/<name>/ui/src/test/kotlin/.../ui/<Name>ScreenshotTest.kt`
- 雛形: `feature/todolist/ui/.../TodoListScreenshotTest.kt`
- 1 テスト = 1 画面状態（空・データあり・ローディング・エラーなど）。テスト名が PNG のファイル名になるので `todoList_empty` のように状態がわかる名前にする
- `AppTheme(dynamicColor = false)` で包む。dynamic color は実行環境に依存し画像が安定しない
- `@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)` で端末を固定する

## 環境まわりの注意

- Robolectric が SDK 36 で JDK 内部 API を使うため、`todosampleapp.android.feature.ui` プラグインがテスト JVM に `--add-opens` / `--add-exports` を付けている。`Failed to interact with raw FileDescriptor internals` が出たらこの設定が外れていないか確認する
- PNG はローカル (macOS) で記録している。CI (Linux) の `verifyRoborazziDebug` でフォント描画差分が出る場合は、CI 上で record した画像に差し替えるか、`RoborazziRule.Options` の `roborazziOptions` に `compareOptions = CompareOptions(changeThreshold = 0.01)` のような閾値を設定する
- `_compare.png` などの出力は `build/` 配下なのでコミットしない。コミットするのは `src/test/screenshots/*.png` だけ

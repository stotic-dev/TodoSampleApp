---
name: snapshot-test
description: Roborazzi によるスナップショット（スクリーンショット）テストの運用手順。検証・差分確認はローカルでもできるが、ベースライン PNG の記録・更新は CI（PR に update-snapshots ラベル）でのみ行う。feature/*/ui の Composable を変更したとき、CI の verifyRoborazziDebug が失敗したとき、新しい画面状態のスナップショットを追加したいとき、「スクリーンショットテストを更新して」「ベースラインを更新して」「画像の差分を見たい」といった依頼で使う。UI を変更した後にベースラインを更新し忘れると CI で落ちるので、Composable を触ったら必ずこのスキルの手順に従う。
---

# スナップショットテストの運用

スナップショットテストは `feature/*/ui` モジュールにだけ置く。`ui` は Hilt / ViewModel / Navigation に依存しないので、実行時にビルドされるのは `ui` + `core:designsystem` + `core:domain` だけで済む。

## 原則: ベースラインは CI で記録する

Robolectric の描画結果はフォントや OS で微妙に変わるため、ローカル（macOS）で記録した PNG は CI（ubuntu）の検証で差分になる。**ベースライン (`src/test/screenshots/*.png`) の記録・更新は CI でのみ行い、ローカルでは record しない。** Convention Plugin (`todosampleapp.android.feature.ui`) がローカルの `recordRoborazzi*` を失敗させる。

- 検証: `.github/workflows/ci.yml` の `verifyRoborazziDebug`（PR ごとに実行、差分があれば失敗）
- 記録: `.github/workflows/update-snapshots.yml`（PR に `update-snapshots` ラベルを付けると起動し、PR ブランチに PNG をコミットして CI を再実行）

## UI を変更したときの流れ

1. Composable を修正する
2. ローカルで差分を目視する
   ```bash
   ./gradlew :feature:<name>:ui:compareRoborazziDebug
   ```
   `feature/<name>/ui/build/outputs/roborazzi/*_compare.png` を Read ツールで開く（左: 期待、中: 差分、右: 実際）。ローカルと CI の描画差で細かな差分が出ることはあるが、レイアウト崩れの有無はここで判断できる
3. 意図どおりなら PR を作成（または更新）し、ラベルを付ける
   ```bash
   gh pr edit <PR番号> --add-label update-snapshots
   ```
4. CI が `recordRoborazziDebug` を実行し、変更があれば「スナップショットのベースラインを更新 (CI)」としてコミット、ラベルを外す。新しいコミットの CI run は `github-actions[bot]` 起点のため承認待ちになるが、ワークフローが自動承認する。結果は PR コメントに投稿される
5. `git pull` でローカルに取り込む

ベースラインが無い新規テストも同じ流れ。初回の CI は verify で失敗するので、ラベルを付けて記録させる。

## テストを追加するとき

- ファイル: `feature/<name>/ui/src/test/kotlin/.../ui/<Name>ScreenshotTest.kt`
- 雛形: `feature/todolist/ui/.../TodoListScreenshotTest.kt`
- 1 テスト = 1 画面状態（空・データあり・ローディング・エラーなど）。テスト名が PNG のファイル名になるので `todoList_empty` のように状態がわかる名前にする
- `AppTheme(dynamicColor = false)` で包む。dynamic color は実行環境に依存し画像が安定しない
- `@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)` で端末を固定する

## CI の verify が失敗したとき

1. Actions のアーティファクト `reports` から `**/build/outputs/roborazzi/*_compare.png` を取得して差分を見る
2. 意図した変更なら `update-snapshots` ラベルを付けてベースラインを更新する
3. 意図しない崩れなら Composable を直す

## 環境まわりの注意

- どうしてもローカルで記録したい場合は `-PallowLocalRecord` を付ける（生成した PNG はコミットしないこと）
- Robolectric が SDK 36 で JDK 内部 API を使うため、`feature.ui` プラグインがテスト JVM に `--add-opens` / `--add-exports` を付けている。`Failed to interact with raw FileDescriptor internals` が出たらこの設定が外れていないか確認する
- `update-snapshots` ワークフローは既定で `GITHUB_TOKEN` で push する。その場合、新しいコミットの `pull_request` CI run は承認待ち (action_required) になるため、ワークフロー内で `gh api .../approve` により自動承認している。`workflow_dispatch` で CI を起動しても PR のステータスチェックには反映されないので使わない
- `SNAPSHOT_PUSH_TOKEN`（`contents: write` の fine-grained PAT）を Secrets に登録すると、その PAT で push するようになり、ユーザーの push として CI が通常どおり起動する（承認ステップは不要になる）
- fork からの PR では `GITHUB_TOKEN` に push 権限が無いため、このワークフローは使えない

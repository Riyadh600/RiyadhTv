# Modifications in RiyadhTv

This file is the ledger for changes specific to Riyadh600/RiyadhTv. Keep each entry small and record the affected files, behavior, and reason. When merging an upstream UrTV release, merge upstream commits into this fork; do not replace these files with upstream copies. Resolve conflicts by preserving both the new upstream behavior and the fork changes, then add a dated entry here.

## Upstream update workflow

The checkout's `origin` is `https://github.com/Riyadh600/RiyadhTv.git`. Commit the fork changes first. Add the original project once as `upstream` if it is not already configured:

```sh
git remote add upstream https://github.com/tshi-dev/UrTV.git
git fetch upstream
git merge upstream/master
```

Resolve any conflicts in the fork, review this ledger, and build/publish from the fork. Existing fork commits remain part of the branch; `MODS.md` makes the intentional product changes easy to find when upstream edits the same code. Do not force-push over the fork's history.

## 2026-11-05 — Larger Watch Next row above favourites

- `android/launcher/src/main/java/sa/urtv/home/MainActivity.java`: render «أكمل المشاهدة» before the favourites, show up to four cards, increase its heading size, and route remote focus from the header to Watch Next, then favourites, then the app grid.
- `android/launcher/src/main/java/sa/urtv/home/TvRowsPick.java`: cap the row at four cards even while favourites are still loading.
- `docs/FEATURES.md`, `tests/tv_rows_test.py`, and `tests/TvRowsRun.java`: describe and assert the new layout and four-card cap.
- Reason: make continuing a show the prominent first row while keeping the Arabic home screen's focus order predictable.
- Base checkout revision: `d40bdc707fb7c6321621b7bf1bcb686fb7882425`.

## 2026-11-05 — Fork-owned updates and signing target

- `android/app/.../Updater.java` and `android/launcher/.../UpdateRules.java`: point both apps' update channels at Riyadh600/RiyadhTv releases.
- `android/launcher/.../UpdateText.java` and `optimize/install.sh`: show/download the fork's releases.
- `.github/tools/make_signing_key.py`: save GitHub Actions signing secrets to the fork repository when GitHub CLI is authenticated.
- `.github/workflows/android.yml`: use the original project's private licensed fonts only for its own builds; fork builds use the documented system-font fallback, so they do not depend on an upstream-only token.
- `README.md`, `docs/SETUP-PROMPT.md`, and `optimize/setup.sh`: direct setup and download instructions to the fork.
- Reason: make future fork builds update from the fork and preserve its own release identity.

## 2026-11-05 — Four selectable Watch Next card sizes

- `android/app/.../Prefs.java`, `SettingsActivity.java`, `ConfigProvider.java`, and `SaverReceiver.java`: save, expose, configure, and optionally set the four sizes using `watchnextsize`.
- `android/launcher/.../ConfigSnapshot.java`, `TvRowsPick.java`, and `MainActivity.java`: read the setting and recalculate row layout when the selected size changes.
- `optimize/saver.sh`, `docs/FEATURES.md`, and `docs/SETTINGS.md`: document the four sizes and command interface.
- Reason: let the owner choose natural, large (up to 4 cards), larger (up to 3), or largest (up to 2) card widths from settings.

## 2026-11-05 — Continue version numbering from the installed TV build

- `.github/workflows/android.yml`, `android/app/build.gradle`, and `android/launcher/build.gradle`: build numbers start at 169, continuing after build 168 reported by the TV, then increase with each GitHub Actions run. APKs and the updater's `version.txt` use the same number.
- Reason: let Android accept the fork APK as a newer version while keeping future fork builds in increasing order.

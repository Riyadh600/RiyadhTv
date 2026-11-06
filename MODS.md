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

## 2026-11-05 — Version 4.2, automatic update check, and optional calm wallpaper motion

- `VERSION`: set the displayed release name to 4.2.
- `android/launcher/.../UpdateActivity.java`, `UpdateText.java`, and `android/launcher/src/main/assets/phone/index.html`: check for updates when the TV update screen opens; when a verified update is available, proceed directly to Android's confirmation flow. Keep parental controls and Android's explicit install confirmation. Remove upstream repository URLs and AI handoff instructions from update messages.
- `android/app/.../Updater.java`: update the settings instructions to use the TV update screen.
- `android/app/.../Prefs.java`, `SettingsActivity.java`, `android/launcher/.../MainActivity.java`, and `src/template.html`: add one «حركة الخلفية» setting for still photos, static by default, with a slow low-amplitude zoom when enabled. The existing compact photos are reused; no video assets are added.
- `.github/workflows/android.yml`, `android/launcher/build.gradle`, and `android/launcher/.../UpdateChannel.java`: make the build branch available so `next` builds check the fork's `apk-next` release channel by default.
- `tests/UpdateRun.java` and `tests/update_test.py`: update existing source assertions for the revised update screen.
- `docs/FEATURES.md` and `docs/SETTINGS.md`: document the 4.2 update flow and wallpaper setting.
- Reason: make updates actionable on the TV itself, identify the new release as 4.2, and let the owner choose between a still background and subtle motion without increasing image downloads.

## 2026-11-05 — Home layout, live channels, and background controls

- `Prefs.java`, `ConfigSnapshot.java`, `SettingsActivity.java`, `MainActivity.java`, and `src/template.html`: make screensaver photo motion automatic; add independent home motion toggle and shared slow/normal/fast speed; move digit conversion into home settings; add five home colour themes.
- `MainActivity.java`: hide app and episode names, reduce excessive edge spacing and row gaps, and make the favourites glass band thinner and lower.
- `LiveChannels.java`, `LiveActivity.java`, `LivePlayerActivity.java`, `ParentalPolicy.java`, `Pill.java`, and `AndroidManifest.xml`: add optional top-row live-channel manager and fullscreen embedded YouTube playback, with channel names and links entered on TV or paired phone keyboard.
- `MainActivity.java`: add top-row update check with spinning indicator, automatic download of a verified fork update, and Android's required install confirmation; show an up-to-date message when no update exists.
- `docs/FEATURES.md` and `docs/SETTINGS.md`: document the new home settings and behaviors.
- Reason: apply the requested home-screen cleanup, personalized background controls, live links, and TV-side update flow while keeping installation under Android's confirmation.

## 2026-11-05 — Independent home labels and final update-pill placement

- `Prefs.java`, `ConfigSnapshot.java`, and `SettingsActivity.java`: add separate switches for application names and Watch Next episode names, both hidden by default.
- `MainActivity.java`: apply each label switch independently and place «تحديث تلفزيوني» as the final shortcut in the top row; show an in-app notice when a new build is found, start its download, then leave final installation confirmation to Android.
- `docs/FEATURES.md` and `docs/SETTINGS.md`: document the independent name controls.
- Reason: make each name group independently visible and put the update check after the other top-row icons.

## 2026-11-05 — Brief prayer-time heads-up in other apps

- `AdhanReceiver.java`, `AdhanScheduler.java`, `Prefs.java`, and new `PrayerTimeNotice.java`: post a high-priority, five-second prayer-time notification when the TV is awake and Android notifications are allowed, even when spoken adhan is off; add an independent toggle in the «تلاوة وأذان» prayer tab.
- New `HomePresence.java` and `MainActivity.java`: track when the UrTV home is in front and suppress the extra heads-up there, leaving its existing prayer bar as the only notice.
- `docs/FEATURES.md`: describe the behavior.
- Reason: show a short prayer-time alert over other apps without duplicating the current home-screen bar.

## 2026-11-05 — RiyTv display name and prayer notice clock

- `android/launcher/src/main/res/values/strings.xml`, `android/launcher/src/main/AndroidManifest.xml`, `android/app/src/main/res/values/strings.xml`, and `android/app/.../SettingsActivity.java`: show «تلفزيون الرياض» as the home app/tile and screensaver settings name.
- `android/launcher/.../tilawah/PrayerTimeNotice.java`: use the requested prayer-entry wording and include the current local time with صباحاً/مساءً.
- Reason: identify the fork by its chosen Arabic name and make the short prayer alert immediately informative.

## 2026-11-05 — RiyTv visual identity

- `assets/branding/riytv-icon.svg` and `assets/branding/riytv-banner.svg`: add editable vector sources for the RiyTv monitor mark and RiyTv banner.
- Android launcher and screensaver launcher icon/banner PNGs, `webos/icon.png`, `webos/largeIcon.png`, `tizen/icon.png`, and `.github/brand/hero.png`: render the updated artwork at each platform's required dimensions.
- Android app labels, update/help text, `webos/appinfo.json`, `tizen/config.xml`, and `README.md`: show «تلفزيون الرياض» / «RiyTv» as product branding.
- Reason: replace the old banner wordmark and use one consistent RiyTv identity across the repository's TV builds.
- Compatibility: Android application IDs, signature permissions, provider URIs, API/session headers, release asset names, and upstream data URLs retain their existing identifiers so installed RiyTv builds can update and the two APKs remain connected.

## 2026-11-06 — Fix RiyTv update screen build

- android/launcher/.../phone/UpdateChannel.java: import the launcher BuildConfig used to select the update channel.
- android/launcher/.../UpdateActivity.java: pass the update failure cause into the error renderer.
- android/launcher/src/main/res/drawable/ic_sync.xml: add the missing refresh icon used by the top update shortcut.
- Reason: fix the four Java compile errors reported by GitHub Actions for the RiyTv branding build.

## 2026-11-06 — Home polish, wired phone control, and playback

- `MainActivity.java`, `Pill.java`, and `ic_phone_remote.xml`: move the update shortcut directly after Sources, add a phone-control shortcut beside Control Center, allow long-press plus D-pad movement to reorder and save top-row icons, reduce home edge margins, make wallpaper drift visibly faster, keep favourite labels hidden when disabled, hide the Watch Next heading by default, use RiyTv themed glass colors across dialogs and top icons, and launch Android's uninstall confirmation with the package uninstall action.
- `GlassSource.java`, `Pill.java`, and `ControlCenter.java` consumers: tint glass surfaces and icons with the selected RiyTv home theme.
- `Prefs.java`, `ConfigProvider.java`, `SettingsActivity.java`, `SettingsHub.java`, `ConfigSnapshot.java`, and `MainActivity.java`: add independent Screensaver motion enable/speed and a Watch Next heading switch; add four all-apps icon/grid sizes; put digit selection in the home-screen settings category; move phone control out of the update category; expose the TV's system date/time settings there.
- `LiveActivity.java`, `LiveChannels.java`, `LivePlayerActivity.java`, and `AndroidManifest.xml`: change live entries to a TV-friendly tile grid with long-press add/edit/delete actions; prefer SmartTube or the TV YouTube app to avoid embedded-WebView black screens.
- `Guard.java`, `PhoneNet.java`, `PhoneHost.java`, `PhoneControl.java`, `PhoneActivity.java`, `PhoneRun.java`, and phone documentation: allow the phone remote to listen on the TV's private Ethernet IPv4 when Wi-Fi is inactive, for a phone on Wi-Fi on the same router.
- `src/template.html`: tighten the Screensaver stage and bottom strip spacing; use faster motion durations.
- `tests/settings_hub_test.py`: update checks for the new settings ownership and date/time row.
- Reason: apply the requested layout and playback corrections, extend phone control across a wired TV and wireless phone on the same LAN, and make controls visually consistent with RiyTv.
- Verification limit: the current shell has no Git, Python, Java, or Gradle executable available; the TV's Ethernet cable is currently unplugged. No new APK was installed.

## 2026-11-06 — Merge upstream UrTV 4.2.204 (build 204)

- Merge the pinned upstream release source at `63c7c418c83f2974fcfd3759e1b21902cc004615` into RiyadhTV `next`; import its code, data, docs, assets, and release tests.
- Bring in scheduled task reminders, the optional football home and matches pages, markets and portfolio, Android 14 target changes, casting preservation, low-memory behavior, and expanded settings.
- Preserve RiyTv's own signing workflow and version-code sequence, fork update channel, app identity, live-channel manager, Watch Next sizing/order, home themes and spacing, and the custom prayer-time notice.
- Resolve shared provider, settings snapshot, focus-policy, manifest, and prayer-alarm changes so the upstream features and RiyTv controls can coexist. Keep RiyTv-specific branding and user-facing update text.
- Reason: take the upstream 4.2.204 improvements while retaining the fork's requested behavior and keeping future upstream merges traceable.
- Verification: GitHub Actions on the merge proposal must pass before producing a test APK. No APK is installed on the TV by this change.

## 2026-11-06 — Merge build fixes

- `android/launcher/.../MainActivity.java`: restore the missing `ValueAnimator` import needed by the upstream theme and RiyTv wallpaper motion.
- `android/launcher/.../phone/UpdateText.java`: keep the fork's Arabic update messages and remove duplicate upstream overloads that referenced missing language types.
- `.github/workflows/checks.yml` and `tests/check.py`: keep non-font checks active in RiyTv Actions; treat missing licensed Thmanyah files as the documented system-font fallback and skip only licensed glyph/fresh-dist checks that cannot be measured without them.
- Reason: resolve Java compilation failures from the merge and make fork CI work without access to the upstream private fonts repository.

- Follow-up CI fix: retain the upstream language callback overloads in `UpdateText.java` while keeping the fork's Arabic up-to-date message; set the live-player WebView text zoom explicitly to 100%.

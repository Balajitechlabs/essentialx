# Contributing to Essentials

Thank you for your interest in contributing to Essentials! This guide details the development setup, architecture conventions, UI component reuse rules, and feature implementation guidelines.

---

## Environment Setup

1. **Android Studio**: Install the latest stable release of [Android Studio](https://developer.android.com/studio).
2. **JDK**: JDK 17 or higher is required.
3. **Clone Repository**:
   ```bash
   git clone https://github.com/sameerasw/essentials.git
   ```
4. **Target Branch**: All branches and pull requests must be based on and targeted to merge back into **`develop`**.
5. **Shizuku / Root**: Many privileged features require [Shizuku](https://shizuku.rikka.app/) or Root for testing on your device or emulator.

---

## Core Development Guidelines & Principles

### 1. Component Reuse (Always Prefer Existing Components)
Before writing custom composables, check the existing design system library in `app/src/main/java/com/sameerasw/essentials/ui/core/`:
- **Containers (`ui/core/containers/`)**:
  - `RoundedCardContainer`: Outer container for grouped settings cards (24.dp corner radius, `surfaceContainer` background).
  - `RoundedCardLazyContainer`: Container for lazy-scrolling lists.
- **Settings List Items & Cards (`ui/core/cards/`)**:
  - `IconToggleItem`: Preferred for settings rows with an icon, title, subtitle/description, and switch. Always pass `index` and `count` for automatic segmented corner morphing.
  - `ConfigPickerItem`: For dropdown or modal picker items inside containers.
  - `FeatureCard`: High-level feature banner/cards with pastel backgrounds (`ColorUtil.getPastelColorFor`) and vibrant icons (`ColorUtil.getVibrantColorFor`).
  - `PermissionCard`: Standardized permission status card with action buttons.
- **Pickers (`ui/core/pickers/`)**:
  - `SegmentedPicker`, `MultiSegmentedPicker`: Connected button group pickers with haptic feedback.
- **Bottom Sheets (`ui/core/sheets/`)**:
  - `EssentialsBottomSheet`, `PermissionsBottomSheet`, `FeatureHelpBottomSheet`.

> [!IMPORTANT]
> Do NOT create duplicate ad-hoc cards, custom switches, or non-standard list rows when standard `ui/core/` components already exist.

---

### 2. Jetpack Compose & Material 3 Expressive Rules
- **No Inline Package Imports**: Always import classes at the top of the file. Never write inline package paths like `com.sameerasw.essentials.ui.core.cards.IconToggleItem(...)`.
- **Containers & Segmenting**: Group related settings into `RoundedCardContainer`. Never use bare uncontained list items.
- **Material 3 Expressive Theming**:
  - Main containers use `surfaceContainer` and `24.dp` rounded corners.
  - Sheet dialogs use `surfaceContainerHigh` or `surfaceContainer`.
  - Supports dynamic coloring and Pitch Black (pure `#000000` AMOLED) overrides.
- **Disabled State Guidance**: When a feature is disabled or missing permissions, use the `enabled = false` pattern with `onDisabledClick` to launch a guidance sheet rather than hiding controls silently.

---

### 3. Iconography & Strings Localization
- **No Hardcoded Strings**: Never hardcode user-facing strings in UI code. Always add entries to `app/src/main/res/values/strings.xml` and use `stringResource(R.string...)` or `context.getString(R.string...)`. Check before adding to avoid duplicates.
- **Resource Icons**: Always prefer rounded drawable resource icons (`R.drawable.rounded_*`) over vector imports. If an icon is missing during implementation, use the expected `R.drawable.rounded_*` reference.

---

### 4. Mandatory Haptics (`HapticUtil`)
- Add haptic feedback on all interactive elements (buttons, switches, sliders, segment pickers, and tiles) using `HapticUtil` (`performUIHaptic`, `performVirtualKeyHaptic`, `performHeavyHaptic`, `performLightHaptic`).
- For background services and Quick Settings tiles, use `HapticUtil.performHapticForService(context)`.

---

### 5. Quick Settings Tiles
- When implementing new Quick Settings tiles, refer to the dedicated guide: [ADD_QS_TILE.md](file:///Users/sameerasandakelum/GIT/essentials/docs/ADD_QS_TILE.md).
- Ensure the tile is declared in `AndroidManifest.xml`, registered in `QsTileRegistry.kt`, supported headlessly in `QsTileActionRouter.kt`, added to `QuickSettingsTilesSettingsUI.kt`, and validated on the **Favorite QS Tiles Glance Widget**.

---

### 6. Search Integration (`FeatureRegistry.kt`)
- All user-facing settings and toggles must be indexed in `FeatureRegistry.kt` using `SearchSetting(...)` entries.
- UI elements must attach `Modifier.highlight(highlightSetting == "key")` so that universal search results smoothly navigate and highlight the target control.

---

### 7. Code Cleanliness & Comments
- Write clean, idiomatic Kotlin code.
- Avoid conversational, repetitive, or redundant comments. Use concise technical comments only where non-obvious architecture or hardware logic requires explanation.

---

## Pull Request Workflow

1. **Branching**: Create a branch off `develop` (e.g. `feature/my-feature` or `fix/issue-description`).
2. **Target**: Pull requests must target the `develop` branch.
3. **Atomic Scope**: Keep PRs focused on a single feature or bug fix.
4. **Local Verification**: Ensure the project compiles cleanly (`./gradlew assembleDebug`) and passes testing on a physical device or emulator.
5. **PR Description**: Detail the rationale, changes made, and include screenshots or screen recordings for any UI changes.

# Feature Modules & UI Composable Guide

This document describes all 17 feature modules in `ui/features/` and their respective UI composables, bottom sheets, and pickers.

---

## Feature Modules Reference

### 1. `ui/features/power/`
- **Composables**: `PowerAndBatterySettingsUI.kt`, `CaffeinateSettingsUI.kt`, `BatteryNotificationSettingsUI.kt`, `MapsPowerSavingSettingsUI.kt`
- **Sheets**: `ScreenOffSettingsSheet.kt`, `DimWallpaperSettingsSheet.kt`
- **Purpose**: Controls display awake timeout rules, low/full battery alerts, and navigation power saving.

### 2. `ui/features/hardware/`
- **Composables**: `FlashlightSettingsUI.kt`, `FlashlightPulseSettingsUI.kt`, `ButtonRemapSettingsUI.kt`, `PocketModeSettingsUI.kt`
- **Sheets**: `DeviceEffectsSettingsSheet.kt`
- **Purpose**: Flashlight intensity modulation, hardware button remapping, and proximity sensor pocket detection.

### 3. `ui/features/network/`
- **Composables**: `NetworksSettingsUI.kt`, `DynamicNightLightSettingsUI.kt`
- **Sheets**: `WifiNetworkSelectionSheet.kt`, `BluetoothDeviceSelectionSheet.kt`
- **Purpose**: Private DNS server presets, network mode QS tiles, and Wi-Fi/Bluetooth device selection.

### 4. `ui/features/audio/`
- **Composables**: `SoundModeTileSettingsUI.kt`, `NotificationSnoozingSettingsUI.kt`, `SnoozeNotificationsSettingsUI.kt`, `ShutUpSettingsUI.kt`
- **Sheets**: `SoundModeSettingsSheet.kt`, `ShutUpPerAppSettingsSheet.kt`, `LikeSongSettingsSheet.kt`
- **Purpose**: Media ducking (ShutUp), notification snooze timers, and sound profile cycling.

### 5. `ui/features/security/`
- **Composables**: `AppLockSettingsUI.kt`, `RemoteLockSettingsUI.kt`, `ScreenLockedSecuritySettingsUI.kt`
- **Purpose**: Biometric app lock, smartwatch remote lock triggers, and status bar expansion restrictions when locked.

### 6. `ui/features/system/`
- **Composables**: `StatusBarIconSettingsUI.kt`, `NavigationSettingsUI.kt`, `OtherCustomizationsSettingsUI.kt`, `TextAnimationsSettingsUI.kt`, `LockScreenClockSettingsUI.kt`, `CalendarSyncSettingsUI.kt`, `KeyboardSettingsUI.kt`, `LiveWallpaperSettingsUI.kt`
- **Sheets**: `SometimesEssentialsSettingsSheet.kt`
- **Purpose**: System status bar icon hiding, animation duration scales, lock screen typography, and IME keyboard settings.

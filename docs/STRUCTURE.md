# Project Directory & Package Structure

This document outlines the package layout and file organization of the Essentials Android app following the **Feature-Modular Architecture** refactor.

---

## High-Level Directory Overview

```
app/src/main/java/com/sameerasw/essentials/
├── MainActivity.kt                  # Root Activity launching main Jetpack Compose UI
├── EssentialsApp.kt                 # Application subclass handling Sentry & global initialization
├── data/                            # Data access, repositories, preferences, and data sources
│   ├── model/                       # Data models & DTOs
│   └── repository/                  # Repositories (SettingsRepository, GitHubRepository, etc.)
├── domain/                          # Business logic, registries, and domain models
│   ├── controller/                  # Domain feature controllers (CaffeinateController, etc.)
│   ├── model/                       # Domain entities
│   └── registry/                    # Feature, Permission, and Search Registries
├── services/                        # Android background services, tile services, and receivers
│   ├── automation/                  # Custom automation rules engine & executors
│   ├── dreams/                      # Ambient screen saver DreamService implementation
│   ├── handlers/                    # Background event handlers
│   ├── tiles/                       # Quick Settings Tile services & QS registry
│   └── widgets/                     # Glance app widgets and receivers
├── ui/                              # Jetpack Compose UI layer
│   ├── activities/                  # Sub-activities (AppLockActivity, SettingsActivity, etc.)
│   ├── core/                        # Reusable core UI components
│   │   ├── cards/                   # Material 3 cards (IconToggleItem, FeatureCard, etc.)
│   │   ├── containers/              # Card container layouts (RoundedCardContainer, etc.)
│   │   ├── pickers/                 # Segmented and multi-select pickers
│   │   └── sheets/                  # Standard bottom sheets (EssentialsBottomSheet, etc.)
│   └── features/                    # Feature-based UI modules
│       ├── apps/                    # App standby & user dictionary controls
│       ├── audio/                   # ShutUp media ducking & sound mode tiles
│       ├── automation/              # DIY rules editor & AI prompt generator
│       ├── battery/                 # Battery stats, charging logs, and info tabs
│       ├── display/                 # Refresh rate, Smart Pixels, & AOD settings
│       ├── freeze/                  # App freezer, tags manager, and grid UI
│       ├── hardware/                # Torch intensity, pulse patterns, & button remap
│       ├── lighting/                # Notification edge lighting & sweep shape pickers
│       ├── location/                # Geofencing destination alarms & cards
│       ├── network/                 # Private DNS presets & network mode tiles
│       ├── power/                   # Caffeinate timeout & battery notification UI
│       ├── security/                # AppLock, RemoteLock, & lock screen security
│       ├── system/                  # Status bar icon blacklist & animation scales
│       ├── tiles/                   # Quick Settings tile manager UI
│       ├── wallpaper/               # Live wallpaper engine controls & picker
│       ├── watch/                   # WearOS companion sync & ADB install guidance
│       └── watermark/               # Photo watermark engine & EXIF metadata editor
├── utils/                           # Modular utility helpers
│   ├── battery/                     # Battery Ring drawer & history log manager
│   ├── hardware/                    # SurfaceFlinger, refresh rate, & torch controls
│   ├── security/                    # Shizuku binder, Root shell, & Biometric helpers
│   └── ui/                          # HapticUtil, ColorUtil, & permission guidance
└── viewmodels/                      # Decoupled domain ViewModels
    ├── MainViewModel.kt             # Main state coordinator
    ├── PermissionViewModel.kt       # System permissions state ViewModel
    ├── SettingsViewModel.kt         # Preferences & pinned features ViewModel
    ├── SecurityViewModel.kt         # AppLock & RemoteLock ViewModel
    ├── BatteryViewModel.kt          # Battery stats & charging state ViewModel
    ├── QuickSettingsTilesViewModel.kt # QS tile toggle states ViewModel
    ├── StatusBarIconViewModel.kt    # System status bar icon blacklist ViewModel
    ├── AppUpdatesViewModel.kt       # GitHub release tracking & update ViewModel
    ├── CaffeinateViewModel.kt       # Display awake timer ViewModel
    ├── DIYViewModel.kt              # Automation rules ViewModel
    ├── LocationReachedViewModel.kt  # GPS geofencing ViewModel
    ├── WatchViewModel.kt            # Smartwatch sync ViewModel
    ├── WatermarkViewModel.kt        # Photo watermark & EXIF ViewModel
    └── GitHubAuthViewModel.kt       # GitHub OAuth device flow ViewModel
```

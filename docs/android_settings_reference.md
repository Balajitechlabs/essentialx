# Comprehensive Android Settings Reference Guide

This document contains **EVERY SINGLE SETTING** extracted directly from Android source code (`Settings.java`).
It covers all **1,310 settings** across `Settings.System`, `Settings.Secure`, and `Settings.Global`.

## Overview & Statistics

| Provider | Total Settings | Description |
| :--- | :--- | :--- |
| **`Settings.System`** | **130** | System preferences specific to user experience (ringtones, haptics, display timeout). |
| **`Settings.Secure`** | **495** | Secure preferences specific to individual users (privacy, lockscreen, IME, accessibility). |
| **`Settings.Global`** | **685** | System-wide global device preferences shared across all users and profiles. |
| **Total** | **1,310** | **Complete Android OS Settings Catalog** |

---

## Navigation / Table of Contents

- [1. Settings.System (130 Settings)](#1-settingssystem-130-settings)
- [2. Settings.Secure (495 Settings)](#2-settingssecure-495-settings)
- [3. Settings.Global (685 Settings)](#3-settingsglobal-685-settings)

---

## 1. Settings.System (130 Settings)

### Categories in this section:
- [Accessibility & Inclusion (1 settings)](#accessibility-inclusion)
- [Audio, Sound, Vibration & Volume (26 settings)](#audio-sound-vibration-volume)
- [Connectivity, Wi-Fi & Network (26 settings)](#connectivity-wi-fi-network)
- [Display, Screen & Appearance (17 settings)](#display-screen-appearance)
- [General & System Settings (33 settings)](#general-system-settings)
- [Input Methods, Voice & Gestures (17 settings)](#input-methods-voice-gestures)
- [Location & Positioning (1 settings)](#location-positioning)
- [Notifications & Do Not Disturb (Zen) (5 settings)](#notifications-do-not-disturb-(zen))
- [Power, Battery & Thermal (2 settings)](#power-battery-thermal)
- [Security, Privacy & Lockscreen (2 settings)](#security-privacy-lockscreen)

<details open>
<summary><h3 style="display:inline;" id="accessibility-inclusion">Accessibility & Inclusion (1 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `HEARING_AID` | `"hearing_aid"` | Whether the hearing aid is enabled. The value is boolean (1 or 0). | `@Readable` `@UnsupportedAppUsage` |

</details>

<details open>
<summary><h3 style="display:inline;" id="audio-sound-vibration-volume">Audio, Sound, Vibration & Volume (26 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ALARM_ALERT` | `"alarm_alert"` | Persistent store for the system-wide default alarm alert. | `@Readable` |
| `ALARM_ALERT_CACHE` | `"alarm_alert_cache"` | {@hide} | `@Readable` |
| `APPLY_RAMPING_RINGER` | `"apply_ramping_ringer"` | Whether applying ramping ringer on incoming phone call ringtone. 1 = apply ramping ringer 0 = do not apply ramping ringer | `@Readable` |
| `AUTO_LAUNCH_MEDIA_CONTROLS` | `"auto_launch_media_controls"` | Controls whether auto-launching media controls is enabled on wearable devices. The valid values for this key are: 0 (disabled) or 1 (enabled). | `Standard` |
| `HAPTIC_FEEDBACK_INTENSITY` | `"haptic_feedback_intensity"` | The intensity of haptic feedback vibrations, if configurable. Not all devices are capable of changing their feedback intensity; on these devices there will likely be no difference between the various vibration intensities except for intensity 0 (off) and the rest. Values: 0 - Vibration is disabled 1 - Weak vibrations 2 - Medium vibrations 3 - Strong vibrations | `@Readable` |
| `HARDWARE_HAPTIC_FEEDBACK_INTENSITY` | `"hardware_haptic_feedback_intensity"` | The intensity of haptic feedback vibrations for interaction with hardware components from the device, like buttons and sensors, if configurable. Not all devices are capable of changing their feedback intensity; on these devices there will likely be no difference between the various vibration intensities except for intensity 0 (off) and the rest. Values: 0 - Vibration is disabled 1 - Weak vibrations 2 - Medium vibrations 3 - Strong vibrations | `Standard` |
| `MEDIA_BUTTON_RECEIVER` | `"media_button_receiver"` | Persistent store for the system default media button event receiver. | `@Readable(maxTargetSdk = Build.VERSION_CODES.R)` |
| `MEDIA_VIBRATION_INTENSITY` | `"media_vibration_intensity"` | The intensity of media vibrations, if configurable. This includes any vibration that is part of media, such as music, movie, soundtrack, game or animations. Not all devices are capable of changing their vibration intensity; on these devices there will likely be no difference between the various vibration intensities except for intensity 0 (off) and the rest. Values: 0 - Vibration is disabled 1 - Weak vibrations 2 - Medium vibrations 3 - Strong vibrations | `Standard` |
| `MODE_RINGER_STREAMS_AFFECTED` | `"mode_ringer_streams_affected"` | Determines which streams are affected by ringer and zen mode changes. The stream type's bit should be set to 1 if it should be muted when going into an inaudible ringer mode. | `@Readable` |
| `NOTIFICATIONS_USE_RING_VOLUME` | `"notifications_use_ring_volume"` | Whether the notifications should use the ring volume (value of 1) or a separate notification volume (value of 0). In most cases, users will have this enabled so the notification and ringer volumes will be the same. However, power users can disable this and use the separate notification volume control. Note: This is a one-off setting that will be removed in the future when there is profile support. For this reason, it is kept hidden from the public APIs. | `@Readable` `@Deprecated` |
| `NOTIFICATION_COOLDOWN_VIBRATE_UNLOCKED` | `"notification_cooldown_vibrate_unlocked"` | When enabled, notification attention effects will be restricted to vibration only as long as the screen is unlocked. The value 1 - enable, 0 - disable | `Standard` |
| `NOTIFICATION_SOUND` | `"notification_sound"` | Persistent store for the system-wide default notification sound. | `@Readable` |
| `NOTIFICATION_SOUND_CACHE` | `"notification_sound_cache"` | {@hide} | `@Readable` |
| `VIBRATE_INPUT_DEVICES` | `"vibrate_input_devices"` | If 1, redirects the system vibrator to all currently attached input devices that support vibration. If there are no such input devices, then the system vibrator is used instead. If 0, does not register the system vibrator. This setting is mainly intended to provide a compatibility mechanism for applications that only know about the system vibrator and do not use the input device vibrator API. | `@Readable` |
| `VIBRATE_IN_SILENT` | `"vibrate_in_silent"` | Whether silent mode should allow vibration feedback. This is used internally in AudioService and the Sound settings activity to coordinate decoupling of vibrate and silent modes. This setting will likely be removed in a future release with support for audio/vibe feedback profiles. Not used anymore. On devices with vibrator, the user explicitly selects silent or vibrate mode. Kept for use by legacy database upgrade code in DatabaseHelper. | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `VIBRATE_ON` | `"vibrate_on"` | Whether vibrate is on for different events. This is used internally, changing this value will not change the vibrate. See AudioManager. | `@Readable` |
| `VIBRATE_WHEN_RINGING` | `"vibrate_when_ringing"` | Whether the phone vibrates when it is ringing due to an incoming call. This will be used by Phone and Setting apps; it shouldn't affect other apps. The value is boolean (1 or 0). Note: this is not same as "vibrate on ring", which had been available until ICS. It was about AudioManager's setting and thus affected all the applications which relied on the setting, while this is purely about the vibration setting for incoming calls. vibrations for incoming calls. User settings are applied automatically by the service and should not be applied by individual apps. | `@Readable` `@Deprecated` |
| `VOLUME_ACCESSIBILITY` | `"volume_a11y"` | Acessibility volume. This is used internally, changing this value will not change the volume. | `@Readable` |
| `VOLUME_ALARM` | `"volume_alarm"` | Alarm volume. This is used internally, changing this value will not change the volume. See AudioManager. | `@Readable` |
| `VOLUME_ASSISTANT` | `"volume_assistant"` | Volume index for virtual assistant. | `@Readable` |
| `VOLUME_MASTER` | `"volume_master"` | Master volume (float in the range 0.0f to 1.0f). | `@Readable` |
| `VOLUME_MUSIC` | `"volume_music"` | Music/media/gaming volume. This is used internally, changing this value will not change the volume. See AudioManager. | `@Readable` |
| `VOLUME_NOTIFICATION` | `"volume_notification"` | Notification volume. This is used internally, changing this value will not change the volume. See AudioManager. | `@Readable` |
| `VOLUME_RING` | `"volume_ring"` | Ringer volume. This is used internally, changing this value will not change the volume. See AudioManager. | `@Readable` |
| `VOLUME_SYSTEM` | `"volume_system"` | System/notifications volume. This is used internally, changing this value will not change the volume. See AudioManager. | `@Readable` |
| `VOLUME_VOICE` | `"volume_voice"` | Voice call volume. This is used internally, changing this value will not change the volume. See AudioManager. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="connectivity-wi-fi-network">Connectivity, Wi-Fi & Network (26 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `APPEND_FOR_LAST_AUDIBLE` | `"_last_audible"` | Appended to various volume related settings to record the previous values before they the settings were affected by a silent/vibrate ringer mode change. | `@Readable` |
| `BLUETOOTH_DISCOVERABILITY` | `"bluetooth_discoverability"` | Determines whether remote devices may discover and/or connect to this device. Type: INT 2 -- discoverable and connectable 1 -- connectable but not discoverable 0 -- neither connectable nor discoverable | `@Readable` |
| `BLUETOOTH_DISCOVERABILITY_TIMEOUT` | `"bluetooth_discoverability_timeout"` | Bluetooth discoverability timeout. If this value is nonzero, then Bluetooth becomes discoverable for a certain number of seconds, after which is becomes simply connectable. The value is in seconds. | `@Readable` |
| `CLOCKWORK_BLUETOOTH_SETTINGS_PREF` | `"cw_bt_settings_pref"` | Controls whether bluetooth is on or off on wearable devices. The valid values for this key are: 0 (disabled) or 1 (enabled). | `Standard` |
| `DEBUG_ENABLE_ENHANCED_CALL_BLOCKING` | `"debug.enable_enhanced_calling"` | When {@code 1}, Telecom enhanced call blocking functionality is enabled. When {@code 0}, enhanced call blocking functionality is disabled. | `@Readable` |
| `HAPTIC_FEEDBACK_ENABLED` | `"haptic_feedback_enabled"` | Whether haptic feedback (Vibrate on tap) is enabled. The value is boolean (1 or 0). vibrations. User settings are applied automatically by the service and should not be applied by individual apps. | `@Readable` `@Deprecated` |
| `KEYBOARD_VIBRATION_ENABLED` | `"keyboard_vibration_enabled"` | Whether keyboard vibration feedback is enabled. The value is boolean (1 or 0). | `@Readable` |
| `LOCKSCREEN_DISABLED` | `"lockscreen.disabled"` | Whether the lockscreen should be completely disabled. | `@Readable` |
| `LOCKSCREEN_SOUNDS_ENABLED` | `"lockscreen_sounds_enabled"` | Whether to play sounds when the keyguard is shown and dismissed. | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED` | `"lock_pattern_tactile_feedback_enabled"` | {@link android.provider.Settings.Secure#LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED} instead | `@Deprecated` |
| `LOCK_PATTERN_VISIBLE` | `"lock_pattern_visible_pattern"` | instead | `@Deprecated` |
| `LOCK_TO_APP_ENABLED` | `"lock_to_app_enabled"` | Whether lock-to-app will be triggered by long-press on recents. | `@Readable` |
| `MULTI_AUDIO_FOCUS_ENABLED` | `"multi_audio_focus_enabled"` | Whether or not to enable multiple audio focus. When enabled, requires more management by user over application playback activity, for instance pausing media apps when another starts. | `@Readable` |
| `NOTIFICATION_COOLDOWN_ENABLED` | `"notification_cooldown_enabled"` | When enabled, notifications attention effects: sound, vibration, flashing will have a cooldown timer. The value 1 - enable, 0 - disable | `Standard` |
| `SOUND_EFFECTS_ENABLED` | `"sound_effects_enabled"` | Whether the sounds effects (key clicks, lid open ...) are enabled. The value is boolean (1 or 0). | `@Readable` |
| `VOLUME_BLUETOOTH_SCO` | `"volume_bluetooth_sco"` | Bluetooth Headset volume. This is used internally, changing this value will not change the volume. See AudioManager. | `@Readable` |
| `WEAR_ACCESSIBILITY_GESTURE_ENABLED` | `"wear_accessibility_gesture_enabled"` | If the triple press gesture for toggling accessibility is enabled. Set to 1 for true and 0 for false. This setting is used only internally. | `Standard` |
| `WEAR_ACCESSIBILITY_GESTURE_ENABLED_DURING_OOBE` | `"wear_accessibility_gesture_enabled_during_oobe"` | If the triple press gesture for toggling accessibility is enabled during OOBE. Set to 1 for true and 0 for false. This setting is used only internally. | `Standard` |
| `WEAR_TTS_PREWARM_ENABLED` | `"wear_tts_prewarm_enabled"` | If the text-to-speech pre-warm is enabled. Set to 1 for true and 0 for false. This setting is used only internally. | `Standard` |
| `WHEN_TO_MAKE_WIFI_CALLS` | `"when_to_make_wifi_calls"` | When to use Wi-Fi calling | `@Readable` |
| `WIFI_STATIC_DNS1` | `"wifi_static_dns1"` | If using static IP, the primary DNS's IP address. Example: "192.168.1.1" | `@Readable` `@Deprecated` |
| `WIFI_STATIC_DNS2` | `"wifi_static_dns2"` | If using static IP, the secondary DNS's IP address. Example: "192.168.1.2" | `@Readable` `@Deprecated` |
| `WIFI_STATIC_GATEWAY` | `"wifi_static_gateway"` | If using static IP, the gateway's IP address. Example: "192.168.1.1" | `@Readable` `@Deprecated` |
| `WIFI_STATIC_IP` | `"wifi_static_ip"` | The static IP address. Example: "192.168.1.51" | `@Readable` `@Deprecated` |
| `WIFI_STATIC_NETMASK` | `"wifi_static_netmask"` | If using static IP, the net mask. Example: "255.255.255.0" | `@Readable` `@Deprecated` |
| `WIFI_USE_STATIC_IP` | `"wifi_use_static_ip"` | Whether to use static IP and other static network attributes. Set to 1 for true and 0 for false. | `@Readable` `@Deprecated` |

</details>

<details open>
<summary><h3 style="display:inline;" id="display-screen-appearance">Display, Screen & Appearance (17 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACCELEROMETER_ROTATION` | `"accelerometer_rotation"` | Control whether the accelerometer will be used to change screen orientation. If 0, it will not be used unless explicitly requested by the application; if 1, it will be used by default unless explicitly disabled by the application. | `@Readable` |
| `DEFAULT_DEVICE_FONT_SCALE` | `"device_font_scale"` | Default scaling factor for fonts for the specific device, float. The value is read from the {@link R.dimen.def_device_font_scale} configuration property. | `@Readable` |
| `DIM_SCREEN` | `"dim_screen"` | Whether or not to dim the screen. 0=no 1=yes | `@Readable` `@Deprecated` |
| `DISPLAY_COLOR_MODE` | `"display_color_mode"` | The display color mode. | `@Readable` |
| `DISPLAY_COLOR_MODE_VENDOR_HINT` | `"display_color_mode_vendor_hint"` | Hint to decide whether restored vendor color modes are compatible with the new device. If unset or a match is not made, only the standard color modes will be restored. | `Standard` |
| `FONT_SCALE` | `"font_scale"` | Scaling factor for fonts, float. | `@Readable` |
| `HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY` | `"hide_rotation_lock_toggle_for_accessibility"` | Control whether the rotation lock toggle in the System UI should be hidden. Typically this is done for accessibility purposes to make it harder for the user to accidentally toggle the rotation lock while the display rotation has been locked for accessibility. If 0, then rotation lock toggle is not hidden for accessibility (although it may be unavailable for other reasons). If 1, then the rotation lock toggle is hidden. | `@Readable` `@UnsupportedAppUsage` |
| `SCREEN_AUTO_BRIGHTNESS_ADJ` | `"screen_auto_brightness_adj"` | Adjustment to auto-brightness to make it generally more (>0.0 <1.0) or less ( -1.0) bright. | `@Readable` `@UnsupportedAppUsage` |
| `SCREEN_BRIGHTNESS` | `"screen_brightness"` | The screen backlight brightness between 1 (minimum) and 255 (maximum). Use {@link android.view.WindowManager.LayoutParams#screenBrightness} to set the screen brightness instead. | `@Readable` |
| `SCREEN_BRIGHTNESS_FOR_ALS` | `"screen_brightness_for_als"` | The screen backlight brightness for automatic mode. Value should be one of: SCREEN_BRIGHTNESS_AUTOMATIC_BRIGHT SCREEN_BRIGHTNESS_AUTOMATIC_NORMAL SCREEN_BRIGHTNESS_AUTOMATIC_DIM | `Standard` |
| `SCREEN_BRIGHTNESS_MODE` | `"screen_brightness_mode"` | Controls whether to enable automatic brightness mode. Value can be set to {@link #SCREEN_BRIGHTNESS_MODE_MANUAL} or {@link #SCREEN_BRIGHTNESS_MODE_AUTOMATIC}. If {@link #SCREEN_BRIGHTNESS_MODE_AUTOMATIC} is set, the system may change {@link #SCREEN_BRIGHTNESS} automatically. | `@Readable` |
| `SCREEN_FLASH_NOTIFICATION` | `"screen_flash_notification"` | Setting to enable screen flash notification feature. 0 = Off 1 = On | `Standard` |
| `SCREEN_FLASH_NOTIFICATION_COLOR` | `"screen_flash_notification_color_global"` | Integer property that specifes the color for screen flash notification as a packed 32-bit color. | `Standard` |
| `SCREEN_OFF_TIMEOUT` | `"screen_off_timeout"` | The amount of time in milliseconds before the device goes to sleep or begins to dream after a period of inactivity. This value is also known as the user activity timeout period since the screen isn't necessarily turned off when it expires. This value is bounded by maximum timeout set by {@link android.app.admin.DevicePolicyManager#setMaximumTimeToLock(ComponentName, long)}. | `@Readable` |
| `USER_ROTATION` | `"user_rotation"` | Default screen rotation when no other policy applies. When {@link #ACCELEROMETER_ROTATION} is zero and no on-screen Activity expresses a preference, this rotation value will be used. Must be one of the {@link android.view.Surface#ROTATION_0 Surface rotation constants}. | `@Readable` |
| `WALLPAPER_ACTIVITY` | `"wallpaper_activity"` | Name of activity to use for wallpaper on the home screen. | `@Readable` `@Deprecated` |
| `WINDOW_ORIENTATION_LISTENER_LOG` | `"window_orientation_listener_log"` | Log raw orientation data from {@link com.android.server.policy.WindowOrientationListener} for use with the orientationplot.py tool. 0 = no 1 = yes | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="general-system-settings">General & System Settings (33 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ADVANCED_SETTINGS` | `"advanced_settings"` | Is advanced settings mode turned on. 0 == no, 1 == yes | `@Readable` |
| `ALARM_VIBRATION_INTENSITY` | `"alarm_vibration_intensity"` | The intensity of alarm vibrations, if configurable. Not all devices are capable of changing their vibration intensity; on these devices there will likely be no difference between the various vibration intensities except for intensity 0 (off) and the rest. Values: 0 - Vibration is disabled 1 - Weak vibrations 2 - Medium vibrations 3 - Strong vibrations | `Standard` |
| `DATE_FORMAT` | `"date_format"` | No explicit description provided in source comments. | `@Readable` `@Deprecated` |
| `DTMF_TONE_TYPE_WHEN_DIALING` | `"dtmf_tone_type"` | CDMA only settings DTMF tone type played by the dialer when dialing. 0 = Normal 1 = Long | `@Readable` |
| `DTMF_TONE_WHEN_DIALING` | `"dtmf_tone"` | Whether the audible DTMF tones are played by the dialer when dialing. The value is boolean (1 or 0). | `@Readable` |
| `EGG_MODE` | `"egg_mode"` | I am the lolrus. Nonzero values indicate that the user has a bukkit. Backward-compatible with PrefGetPreference(prefAllowEasterEggs) . | `@Readable` |
| `END_BUTTON_BEHAVIOR` | `"end_button_behavior"` | What happens when the user presses the end call button if they're not on a call. Values: 0 - The end button does nothing. 1 - The end button goes to the home screen. 2 - The end button puts the device to sleep and locks the keyguard. 3 - The end button goes to the home screen. If the user is already on the home screen, it puts the device to sleep. | `@Readable` |
| `LOCALE_PREFERENCES` | `"locale_preferences"` | The information of locale preference. This records user's preference to avoid unsynchronized and existing locale preference in {@link Locale#getDefault(Locale.Category)}. Note: The format follow the IETF BCP47 expression E.g. : und-u-ca-gregorian-hc-h23 | `Standard` |
| `MASTER_BALANCE` | `"master_balance"` | Master balance (float -1.f = 100% left, 0.f = dead center, 1.f = 100% right). | `@Readable` |
| `MASTER_MONO` | `"master_mono"` | Master mono (int 1 = mono, 0 = normal). | `@Readable` `@UnsupportedAppUsage` |
| `MIN_REFRESH_RATE` | `"min_refresh_rate"` | The user selected min refresh rate in frames per second. If infinite, the user wants the highest possible refresh rate. If this isn't set, 0 will be used. | `@Readable` |
| `MOUSE_REVERSE_VERTICAL_SCROLLING` | `"mouse_reverse_vertical_scrolling"` | Whether to enable reversed vertical scrolling for connected mice. When enabled, scrolling down on the mouse wheel will move the screen up and vice versa. | `Standard` |
| `MOUSE_SWAP_PRIMARY_BUTTON` | `"mouse_swap_primary_button"` | Whether to enable swapping the primary button for connected mice. When enabled, right clicking will be the primary button and left clicking will be the secondary button (e.g. show menu). | `Standard` |
| `MUTE_STREAMS_AFFECTED` | `"mute_streams_affected"` | Determines which streams are affected by mute. The stream type's bit should be set to 1 if it should be muted when a mute request is received. | `@Readable` |
| `NEXT_ALARM_FORMATTED` | `"next_alarm_formatted"` | A formatted string of the next alarm that is set, or the empty string if there is no alarm set. | `@Readable` `@Deprecated` |
| `PEAK_REFRESH_RATE` | `"peak_refresh_rate"` | The user selected peak refresh rate in frames per second. If infinite, the user wants the highest possible refresh rate. If this isn't set, the system falls back to a device specific default. | `@Readable` |
| `PREFERRED_REGION` | `"preferred_region"` | User can change the region from region settings. This records user's preferred region. E.g. : if user's locale is en-US, this will record US | `Standard` |
| `RINGTONE` | `"ringtone"` | Persistent store for the system-wide default ringtone URI. If you need to play the default ringtone at any given time, it is recommended you give {@link #DEFAULT_RINGTONE_URI} to the media player. It will resolve to the set default ringtone at the time of playing. | `@Readable` |
| `RINGTONE_CACHE` | `"ringtone_cache"` | {@hide} | `Standard` |
| `RING_VIBRATION_INTENSITY` | `"ring_vibration_intensity"` | The intensity of ringtone vibrations, if configurable. Not all devices are capable of changing their vibration intensity; on these devices there will likely be no difference between the various vibration intensities except for intensity 0 (off) and the rest. Values: 0 - Vibration is disabled 1 - Weak vibrations 2 - Medium vibrations 3 - Strong vibrations | `@Readable` |
| `SETUP_WIZARD_HAS_RUN` | `"setup_wizard_has_run"` | Whether the setup wizard has been run before (on first boot), or if it still needs to be run. nonzero = it has been run in the past 0 = it has not been run in the past | `@Readable` |
| `SHOW_GTALK_SERVICE_STATUS` | `"SHOW_GTALK_SERVICE_STATUS"` | No explicit description provided in source comments. | `@Readable` |
| `SHOW_KEY_PRESSES` | `"show_key_presses"` | Show key presses dispatched to focused windows on the screen. 0 = no 1 = yes | `Standard` |
| `SHOW_WEB_SUGGESTIONS` | `"show_web_suggestions"` | setting for this. | `@Readable` `@Deprecated` |
| `SIP_ADDRESS_ONLY` | `"SIP_ADDRESS_ONLY"` | One of the sip call options: Only if destination is a SIP address. | `@Readable` |
| `SIP_ALWAYS` | `"SIP_ALWAYS"` | One of the sip call options: Always use SIP with network access. | `@Readable` |
| `SIP_CALL_OPTIONS` | `"sip_call_options"` | Call Preference String. "SIP_ALWAYS" : Always use SIP with network access "SIP_ADDRESS_ONLY" : Only if destination is a SIP address | `@Readable` |
| `SIP_RECEIVE_CALLS` | `"sip_receive_calls"` | Receive incoming SIP calls? 0 = no 1 = yes | `@Readable` |
| `SYSTEM_LOCALES` | `"system_locales"` | The serialized system locale value. Do not use this value directory. To get system locale, use {@link LocaleList#getDefault} instead. To update system locale, use {@link com.android.internal.app.LocalePicker#updateLocales} instead. | `@Readable` |
| `TEXT_AUTO_CAPS` | `"auto_caps"` | Setting to enable Auto Caps in text editors. 1 = On, 0 = Off | `@Readable` |
| `TEXT_AUTO_PUNCTUATE` | `"auto_punctuate"` | Setting to enable Auto Punctuate in text editors. 1 = On, 0 = Off. This feature converts two spaces to a "." and space. | `@Readable` |
| `TEXT_AUTO_REPLACE` | `"auto_replace"` | Setting to enable Auto Replace (AutoText) in text editors. 1 = On, 0 = Off | `@Readable` |
| `TTY_MODE` | `"tty_mode"` | CDMA only settings TTY Mode 0 = OFF 1 = FULL 2 = VCO 3 = HCO | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |

</details>

<details open>
<summary><h3 style="display:inline;" id="input-methods-voice-gestures">Input Methods, Voice & Gestures (17 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `INPUT_GAIN_INDEX_SETTINGS` | `"input_gain_index_settings"` | The mapping of input device to its input gain index. | `@Readable` |
| `POINTER_FILL_STYLE` | `"pointer_fill_style"` | Pointer fill style, specified by {@link android.view.PointerIcon.PointerIconVectorStyleFill} constants. | `@Readable` |
| `POINTER_SCALE` | `"pointer_scale"` | Pointer scale setting. This float value represents the scale by which the size of the pointer increases. | `@Readable` |
| `POINTER_SPEED` | `"pointer_speed"` | Pointer speed setting. This is an integer value in a range between -7 and +7, so there are 15 possible values. -7 = slowest 0 = default speed +7 = fastest | `@Readable` `@TestApi` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `POINTER_STROKE_STYLE` | `"pointer_stroke_style"` | Pointer stroke style, specified by {@link android.view.PointerIcon.PointerIconVectorStyleStroke} constants. | `@Readable` |
| `SHOW_ROTARY_INPUT` | `"show_rotary_input"` | Show rotary input dispatched to focused windows on the screen. 0 = no 1 = yes | `Standard` |
| `SHOW_TOUCHES` | `"show_touches"` | Show touch positions on screen? 0 = no 1 = yes | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `SIP_ASK_ME_EACH_TIME` | `"SIP_ASK_ME_EACH_TIME"` | the user should be prompted each time a call is made whether it should be placed using SIP. The {@link com.android.providers.settings.DatabaseHelper} replaces this with SIP_ADDRESS_ONLY. | `@Readable` `@Deprecated` |
| `TIME_12_24` | `"time_12_24"` | Display the user's times, e.g. in the status bar, as 12 or 24 hours. 24 = 24 hour 12 = 12 hour [unset] = use the device locale's default | `@Readable` |
| `TOUCHPAD_NATURAL_SCROLLING` | `"touchpad_natural_scrolling"` | Whether to invert the touchpad scrolling direction. If set to 1 (the default), moving two fingers downwards on the touchpad will scroll upwards, consistent with normal touchscreen scrolling. If set to 0, moving two fingers downwards will scroll downwards. | `@Readable` |
| `TOUCHPAD_POINTER_SPEED` | `"touchpad_pointer_speed"` | Touchpad pointer speed setting. This is an integer value in a range between -7 and +7, so there are 15 possible values. -7 = slowest 0 = default speed +7 = fastest | `Standard` |
| `TOUCHPAD_RIGHT_CLICK_ZONE` | `"touchpad_right_click_zone"` | Whether to enable a right-click zone on touchpads. When set to 1, pressing to click in a section on the right-hand side of the touchpad will result in a context click (a.k.a. right click). | `Standard` |
| `TOUCHPAD_SYSTEM_GESTURES` | `"touchpad_system_gestures"` | Whether to enable system gestures (three- and four-finger swipes) on touchpads. | `Standard` |
| `TOUCHPAD_TAP_DRAGGING` | `"touchpad_tap_dragging"` | Whether to enable tap dragging on touchpads. | `Standard` |
| `TOUCHPAD_TAP_TO_CLICK` | `"touchpad_tap_to_click"` | Whether to enable tap-to-click on touchpads. | `Standard` |
| `TOUCHPAD_THREE_FINGER_TAP_CUSTOMIZATION` | `"touchpad_three_finger_tap_customization"` | Whether to enable three finger tap customization on touchpads. | `Standard` |
| `TOUCHPAD_VISUALIZER` | `"touchpad_visualizer"` | Show touchpad input visualization on screen. 0 = no 1 = yes | `Standard` |

</details>

<details open>
<summary><h3 style="display:inline;" id="location-positioning">Location & Positioning (1 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `POINTER_LOCATION` | `"pointer_location"` | Show pointer location on screen? 0 = no 1 = yes | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |

</details>

<details open>
<summary><h3 style="display:inline;" id="notifications-do-not-disturb-(zen)">Notifications & Do Not Disturb (Zen) (5 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `CAMERA_FLASH_NOTIFICATION` | `"camera_flash_notification"` | Setting to enable camera flash notification feature. 0 = Off 1 = On | `Standard` |
| `NOTIFICATION_COOLDOWN_ALL` | `"notification_cooldown_all"` | When enabled, notification cooldown will apply to all notifications. Otherwise cooldown will only apply to conversations. The value 1 - enable, 0 - disable Only valid if {@code NOTIFICATION_COOLDOWN_ENABLED} is enabled. | `Standard` |
| `NOTIFICATION_LIGHT_PULSE` | `"notification_light_pulse"` | Whether the notification LED should repeatedly flash when a notification is pending. The value is boolean (1 or 0). | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `NOTIFICATION_VIBRATION_INTENSITY` | `"notification_vibration_intensity"` | The intensity of notification vibrations, if configurable. Not all devices are capable of changing their vibration intensity; on these devices there will likely be no difference between the various vibration intensities except for intensity 0 (off) and the rest. Values: 0 - Vibration is disabled 1 - Weak vibrations 2 - Medium vibrations 3 - Strong vibrations | `@Readable` |
| `UNREAD_NOTIFICATION_DOT_INDICATOR` | `"unread_notification_dot_indicator"` | Controls whether the unread notification dot indicator is shown on wearable devices. The valid values for this key are: 0 (disabled) or 1 (enabled). | `Standard` |

</details>

<details open>
<summary><h3 style="display:inline;" id="power-battery-thermal">Power, Battery & Thermal (2 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ADAPTIVE_SLEEP` | `"adaptive_sleep"` | Control whether to enable adaptive sleep mode. | `@Readable` `@Deprecated` |
| `SHOW_BATTERY_PERCENT` | `"status_bar_show_battery_percent"` | Setting to determine whether or not to show the battery percentage in the status bar. 0 - Don't show percentage 1 - Show percentage | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="security-privacy-lockscreen">Security, Privacy & Lockscreen (2 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `FOLD_LOCK_BEHAVIOR` | `"fold_lock_behavior_setting"` | Control lock behavior on fold If this isn't set, the system falls back to a device specific default. | `@Readable` |
| `TEXT_SHOW_PASSWORD` | `"show_password"` | Setting to showing password characters in text editors. 1 = On, 0 = Off | `@Readable` |

</details>

---

## 2. Settings.Secure (495 Settings)

### Categories in this section:
- [Accessibility & Inclusion (35 settings)](#accessibility-inclusion)
- [Apps, Storage & Package Management (22 settings)](#apps-storage-package-management)
- [Audio, Sound, Vibration & Volume (20 settings)](#audio-sound-vibration-volume)
- [Connectivity, Wi-Fi & Network (160 settings)](#connectivity-wi-fi-network)
- [Developer Options & Debugging (2 settings)](#developer-options-debugging)
- [Display, Screen & Appearance (67 settings)](#display-screen-appearance)
- [General & System Settings (75 settings)](#general-system-settings)
- [Input Methods, Voice & Gestures (49 settings)](#input-methods-voice-gestures)
- [Location & Positioning (10 settings)](#location-positioning)
- [Multi-User, Accounts & Profiles (6 settings)](#multi-user-accounts-profiles)
- [Notifications & Do Not Disturb (Zen) (7 settings)](#notifications-do-not-disturb-(zen))
- [Power, Battery & Thermal (12 settings)](#power-battery-thermal)
- [Security, Privacy & Lockscreen (30 settings)](#security-privacy-lockscreen)

<details open>
<summary><h3 style="display:inline;" id="accessibility-inclusion">Accessibility & Inclusion (35 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACCESSIBILITY_ALLOW_DIAGONAL_SCROLLING` | `"accessibility_allow_diagonal_scrolling"` | Accessibility Window Magnification Allow diagonal scrolling value. The value is boolean. 1 : on, 0 : off | `Standard` |
| `ACCESSIBILITY_AUTOCLICK_DELAY` | `"accessibility_autoclick_delay"` | Integer setting specifying amount of time in ms the mouse pointer has to stay still before performing click when {@link #ACCESSIBILITY_AUTOCLICK_ENABLED} is set. | `@Readable` |
| `ACCESSIBILITY_BOUNCE_KEYS` | `"accessibility_bounce_keys"` | Whether to enable bounce keys for Physical Keyboard accessibility. If set to non-zero value, any key press on physical keyboard within the provided threshold duration (in milliseconds) of the same key, will be ignored. | `@Readable` |
| `ACCESSIBILITY_BUTTON_MODE` | `"accessibility_button_mode"` | Controls the accessibility button mode. System will force-set the value to {@link #ACCESSIBILITY_BUTTON_MODE_GESTURE} if {@link #NAVIGATION_MODE} is button; force-set the value to {@link ACCESSIBILITY_BUTTON_MODE_NAVIGATION_BAR} if {@link #NAVIGATION_MODE} is gestural; otherwise, remain the option. 0 = button in navigation bar 1 = button floating on the display 2 = button using gesture to trigger | `Standard` |
| `ACCESSIBILITY_BUTTON_TARGETS` | `"accessibility_button_targets"` | Setting specifying the accessibility services, accessibility shortcut targets, or features to be toggled via the accessibility button in the navigation bar, or the floating accessibility button. This is a colon-separated string list which contains the flattened {@link ComponentName} and the class name of a system class implementing a supported accessibility feature. | `@Readable` |
| `ACCESSIBILITY_BUTTON_TARGET_COMPONENT` | `"accessibility_button_target_component"` | Setting specifying the accessibility service or feature to be toggled via the accessibility button in the navigation bar. This is either a flattened {@link ComponentName} or the class name of a system class implementing a supported accessibility feature. | `@Readable` |
| `ACCESSIBILITY_CAPTIONING_EDGE_TYPE` | `"accessibility_captioning_edge_type"` | Integer property that specifes the edge type for captions, one of: {@link android.view.accessibility.CaptioningManager.CaptionStyle#EDGE_TYPE_NONE} {@link android.view.accessibility.CaptioningManager.CaptionStyle#EDGE_TYPE_OUTLINE} {@link android.view.accessibility.CaptioningManager.CaptionStyle#EDGE_TYPE_DROP_SHADOW} | `@Readable` |
| `ACCESSIBILITY_CAPTIONING_LOCALE` | `"accessibility_captioning_locale"` | Setting that specifies the language for captions as a locale string, e.g. en_US. | `@Readable` |
| `ACCESSIBILITY_CAPTIONING_PRESET` | `"accessibility_captioning_preset"` | Integer property that specifies the preset style for captions, one of: {@link android.view.accessibility.CaptioningManager.CaptionStyle#PRESET_CUSTOM} a valid index of {@link android.view.accessibility.CaptioningManager.CaptionStyle#PRESETS} | `@Readable` |
| `ACCESSIBILITY_CAPTIONING_TYPEFACE` | `"accessibility_captioning_typeface"` | String property that specifies the typeface for captions, one of: DEFAULT MONOSPACE SANS_SERIF SERIF | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `ACCESSIBILITY_FLOATING_MENU_ICON_TYPE` | `"accessibility_floating_menu_icon_type"` | The icon type of the accessibility floating menu. 0 = full circle type 1 = half circle type | `Standard` |
| `ACCESSIBILITY_FLOATING_MENU_MIGRATION_TOOLTIP_PROMPT` | `"accessibility_floating_menu_migration_tooltip_prompt"` | Prompts the user to the Accessibility button is replaced with the floating menu. 0 = disabled 1 = enabled | `Standard` |
| `ACCESSIBILITY_FLOATING_MENU_OPACITY` | `"accessibility_floating_menu_opacity"` | The opacity value for the accessibility floating menu fade out effect, from 0.0 (transparent) to 1.0 (opaque). | `Standard` |
| `ACCESSIBILITY_FLOATING_MENU_SIZE` | `"accessibility_floating_menu_size"` | The size of the accessibility floating menu. 0 = small size 1 = large size | `Standard` |
| `ACCESSIBILITY_GESTURE_TARGETS` | `"accessibility_gesture_targets"` | Setting specifying the accessibility services, shortcut targets or features to be toggled via the gesture shortcut This is a colon-separated string list which contains the flattened {@link ComponentName} and the class name of a system class implementing a supported accessibility feature. | `@Readable` |
| `ACCESSIBILITY_INTERACTIVE_UI_TIMEOUT_MS` | `"accessibility_interactive_ui_timeout_ms"` | Setting that specifies recommended timeout in milliseconds for controls which need user's interactions. | `@Readable` |
| `ACCESSIBILITY_KEY_GESTURE_TARGETS` | `"accessibility_key_gesture_targets"` | Setting specifying the accessibility services, accessibility shortcut targets, or features to be toggled via a keyboard shortcut gesture. This is a colon-separated string list which contains the flattened {@link ComponentName} and the class name of a system class implementing a supported accessibility feature. | `Standard` |
| `ACCESSIBILITY_LARGE_POINTER_ICON` | `"accessibility_large_pointer_icon"` | Whether or not larger size icons are used for the pointer of mouse/trackpad for accessibility. (0 = false, 1 = true) | `@Readable` `@UnsupportedAppUsage` |
| `ACCESSIBILITY_MAGNIFICATION_CAPABILITY` | `"accessibility_magnification_capability"` | Controls magnification capability. Accessibility magnification is capable of at least one of the magnification modes. | `@Readable` `@TestApi` |
| `ACCESSIBILITY_MAGNIFICATION_MODE` | `"accessibility_magnification_mode"` | Controls magnification mode when magnification is enabled via a system-wide triple tap gesture or the accessibility shortcut. | `@Readable` `@TestApi` |
| `ACCESSIBILITY_NON_INTERACTIVE_UI_TIMEOUT_MS` | `"accessibility_non_interactive_ui_timeout_ms"` | Setting that specifies recommended timeout in milliseconds for controls which don't need user's interactions. | `@Readable` |
| `ACCESSIBILITY_QS_TARGETS` | `"accessibility_qs_targets"` | Setting specifying the accessibility services, accessibility shortcut targets, or features to be toggled via a tile in the quick settings panel. This is a colon-separated string list which contains the flattened {@link ComponentName} and the class name of a system class implementing a supported accessibility feature. | `Standard` |
| `ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN` | `"accessibility_shortcut_dialog_shown"` | Setting specifying if the accessibility shortcut dialog has been shown to this user. | `@Readable` |
| `ACCESSIBILITY_SHORTCUT_TARGET_MAGNIFICATION_CONTROLLER` | `"com.android.server.accessibility.MagnificationController"` | The system class name of magnification controller which is a target to be toggled via accessibility shortcut or accessibility button. | `@Readable` |
| `ACCESSIBILITY_SHORTCUT_TARGET_SERVICE` | `"accessibility_shortcut_target_service"` | Setting specifying the accessibility services, accessibility shortcut targets, or features to be toggled via the accessibility shortcut. This is a colon-separated string list which contains the flattened {@link ComponentName} and the class name of a system class implementing a supported accessibility feature. | `@Readable` `@TestApi` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `ACCESSIBILITY_SLOW_KEYS` | `"accessibility_slow_keys"` | Whether to enable slow keys for Physical Keyboard accessibility. If set to non-zero value, any key press on physical keyboard needs to be pressed and held for the provided threshold duration (in milliseconds) to be registered in the system. | `@Readable` |
| `ACCESSIBILITY_SOFT_KEYBOARD_MODE` | `"accessibility_soft_keyboard_mode"` | Setting that specifies what mode the soft keyboard is in (default or hidden). Can be modified from an AccessibilityService using the SoftKeyboardController. | `@Readable` |
| `ACCESSIBILITY_SPEAK_PASSWORD` | `"speak_password"` | Whether to speak passwords while in accessibility mode. Apps should ignore this setting and provide complete information to accessibility at all times, which was the behavior when this value was {@code true}. | `@Readable` `@Deprecated` |
| `ACCESSIBILITY_STICKY_KEYS` | `"accessibility_sticky_keys"` | Whether to enable sticky keys for Physical Keyboard accessibility. This is a boolean value that determines if Sticky keys feature is enabled. | `@Readable` |
| `HEARING_AID_CALL_ROUTING` | `"hearing_aid_call_routing"` | Phone call routing value for hearing aid. It routes phone call to hearing aid or device speaker. 0 = Default 1 = Route to hearing aid 2 = Route to device speaker | `Standard` |
| `HEARING_AID_NOTIFICATION_ROUTING` | `"hearing_aid_notification_routing"` | Notification routing value for hearing aid. It routes notification sounds to hearing aid or device speaker. 0 = Default 1 = Route to hearing aid 2 = Route to device speaker | `Standard` |
| `HEARING_AID_RINGTONE_ROUTING` | `"hearing_aid_ringtone_routing"` | Ringtone routing value for hearing aid. It routes ringtone to hearing aid or device speaker. 0 = Default 1 = Route to hearing aid 2 = Route to device speaker | `Standard` |
| `NOTIFIED_NON_ACCESSIBILITY_CATEGORY_SERVICES` | `"notified_non_accessibility_category_services"` | List of the notified non-accessibility category accessibility services. | `@Readable` |
| `SKIP_ACCESSIBILITY_SHORTCUT_DIALOG_TIMEOUT_RESTRICTION` | `"skip_accessibility_shortcut_dialog_timeout_restriction"` | Setting specifying if the timeout restriction {@link ViewConfiguration#getAccessibilityShortcutKeyTimeout()} of the accessibility shortcut dialog is skipped. | `Standard` |
| `TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES` | `"touch_exploration_granted_accessibility_services"` | List of the accessibility services to which the user has granted permission to put the device into touch exploration mode. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="apps-storage-package-management">Apps, Storage & Package Management (22 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ALWAYS_ON_VPN_APP` | `"always_on_vpn_app"` | Package designated as always-on VPN provider. | `Standard` |
| `AMBIENT_CONTEXT_CONSENT_COMPONENT` | `"ambient_context_consent_component"` | Current provider of the component for requesting ambient context consent. Default value in @string/config_defaultAmbientContextConsentComponent. No VALIDATOR as this setting will not be backed up. | `Standard` |
| `AMBIENT_CONTEXT_PACKAGE_NAME_EXTRA_KEY` | `"ambient_context_package_name_key"` | Current provider of the intent extra key for the caller's package name while requesting ambient context consent. No VALIDATOR as this setting will not be backed up. | `Standard` |
| `AUTOMATIC_STORAGE_MANAGER_BYTES_CLEARED` | `"automatic_storage_manager_bytes_cleared"` | How many bytes the automatic storage manager has cleared out. | `@Readable` |
| `AUTOMATIC_STORAGE_MANAGER_DAYS_TO_RETAIN` | `"automatic_storage_manager_days_to_retain"` | How many days of information for the automatic storage manager to retain on the device. | `@Readable` |
| `AUTOMATIC_STORAGE_MANAGER_LAST_RUN` | `"automatic_storage_manager_last_run"` | Last run time for the automatic storage manager. | `@Readable` |
| `AUTOMATIC_STORAGE_MANAGER_TURNED_OFF_BY_POLICY` | `"automatic_storage_manager_turned_off_by_policy"` | If the automatic storage manager has been disabled by policy. Note that this doesn't mean that the automatic storage manager is prevented from being re-enabled -- this only means that it was turned off by policy at least once. | `@Readable` |
| `BACKUP_AUTO_RESTORE` | `"backup_auto_restore"` | Controls whether application data is automatically restored from backup at install time. Type: int ( 0 = disabled, 1 = enabled ) | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `BACKUP_LOCAL_TRANSPORT_PARAMETERS` | `"backup_local_transport_parameters"` | Local transport parameters so we can configure it for tests. This is encoded as a key=value list, separated by commas. The following keys are supported: fake_encryption_flag (boolean) Type: string | `@Readable` |
| `BACKUP_MANAGER_CONSTANTS` | `"backup_manager_constants"` | Backup manager behavioral parameters. This is encoded as a key=value list, separated by commas. Ex: "key_value_backup_interval_milliseconds=14400000,key_value_backup_require_charging=true" The following keys are supported: key_value_backup_interval_milliseconds (long) key_value_backup_fuzz_milliseconds (long) key_value_backup_require_charging (boolean) key_value_backup_required_network_type (int) full_backup_interval_milliseconds (long) full_backup_require_charging (boolean) full_backup_required_network_type (int) backup_finished_notification_receivers (String[]) backup_finished_notification_receivers uses ":" as delimeter for values. Type: string | `@Readable` |
| `BACKUP_PROVISIONED` | `"backup_provisioned"` | Indicates whether settings backup has been fully provisioned. Type: int ( 0 = unprovisioned, 1 = fully provisioned ) | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `BACKUP_TRANSPORT` | `"backup_transport"` | Component of the transport to use for backup/restore. | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `CARRIER_APPS_HANDLED` | `"carrier_apps_handled"` | The latest SDK version that CarrierAppUtils#disableCarrierAppsUntilPrivileged has been executed for. This is used to ensure that we only take one pass which will disable apps that are not privileged (if any). From then on, we only want to enable apps (when a matching SIM is inserted), to avoid disabling an app that the user might actively be using. Will be set to {@link android.os.Build.VERSION#SDK_INT} once executed. Note that older SDK versions prior to R set 1 for this value. | `@Readable` |
| `CONTEXTUAL_SEARCH_PACKAGE` | `"contextual_search_package"` | String property which contains the package name of the contextual search provider supplied by individual OEM's R.string.config_defaultContextualSearchPackageName. | `@Readable` |
| `CUSTOM_BUGREPORT_HANDLER_APP` | `"custom_bugreport_handler_app"` | The package name for the custom bugreport handler app. This app must be bugreport allow-listed. This is currently used only by Power Menu short press. | `Standard` |
| `DIALER_DEFAULT_APPLICATION` | `"dialer_default_application"` | Specifies the package name currently configured to be the default dialer application | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `INSTALL_NON_MARKET_APPS` | `"install_non_market_apps"` | Whether applications can be installed for this user via the system's {@link Intent#ACTION_INSTALL_PACKAGE} mechanism. 1 = permit app installation via the system package installer intent 0 = do not allow use of the package installer {@link PackageManager#canRequestPackageInstalls()} | `@Readable` `@Deprecated` |
| `NEARBY_FAST_PAIR_SETTINGS_DEVICES_COMPONENT` | `"nearby_fast_pair_settings_devices_component"` | Current provider of Fast Pair saved devices page. Default value in @string/config_defaultNearbyFastPairSettingsDevicesComponent. No VALIDATOR as this setting will not be backed up. | `Standard` |
| `NEARBY_SHARING_COMPONENT` | `"nearby_sharing_component"` | Current provider of proximity-based sharing services. Default value in @string/config_defaultNearbySharingComponent. No VALIDATOR as this setting will not be backed up. | `@Readable` |
| `SMS_DEFAULT_APPLICATION` | `"sms_default_application"` | Specifies the package name currently configured to be the primary sms application | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `TAPS_APP_TO_EXIT` | `"taps_app_to_exit"` | For user taps app to exit One-Handed Mode. | `Standard` |
| `THEME_CUSTOMIZATION_OVERLAY_PACKAGES` | `"theme_customization_overlay_packages"` | Map of android.theme.customization.* categories to the enabled overlay package for that category, formatted as a serialized {@link org.json.JSONObject}. If there is no corresponding package included for a category, then all overlay packages in that category must be disabled. A few category keys have special meaning and are used for Material You theming. A {@code FabricatedOverlay} containing Material You tonal palettes will be generated in case {@code android.theme.customization.system_palette} contains a {@link android.annotation.ColorInt}. The strategy used for generating the tonal palettes can be defined with the {@code android.theme.customization.theme_style} key, with one of the following options: {@code TONAL_SPOT} is a mid vibrancy palette that uses an accent 3 analogous to accent 1. {@code VIBRANT} is a high vibrancy palette that harmoniously blends subtle shifts between colors. {@code EXPRESSIVE} is a high vibrancy palette that pairs unexpected and unique accents colors together. {@code SPRITZ} is a low vibrancy palette that creates a soft wash between colors. {@code RAINBOW} uses both chromatic accents and neutral surfaces to create a more subtle color experience for users. {@code FRUIT_SALAD} experiments with the concept of "two tone colors" to give users more expression. Example of valid fabricated theme specification: { "android.theme.customization.system_palette":"B1611C", "android.theme.customization.theme_style":"EXPRESSIVE" } | `@Readable` `@SystemApi` |

</details>

<details open>
<summary><h3 style="display:inline;" id="audio-sound-vibration-volume">Audio, Sound, Vibration & Volume (20 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `AUDIO_DEVICE_INVENTORY` | `"audio_device_inventory"` | Internal collection of audio device inventory items The device item stored are {@link com.android.server.audio.AdiDeviceState} | `Standard` |
| `HEARING_AID_MEDIA_ROUTING` | `"hearing_aid_media_routing"` | Media routing value for hearing aid. It routes media to hearing aid or device speaker. 0 = Default 1 = Route to hearing aid 2 = Route to device speaker | `Standard` |
| `MANUAL_RINGER_TOGGLE_COUNT` | `"manual_ringer_toggle_count"` | Number of times the user has manually clicked the ringer toggle | `@Readable` |
| `MEDIA_CONTROLS_LOCK_SCREEN` | `"media_controls_lock_screen"` | Whether to enable media controls on lock screen. When enabled, media controls will appear on lock screen. | `Standard` |
| `MEDIA_CONTROLS_RECOMMENDATION` | `"qs_media_recommend"` | Controls whether contextual suggestions can be shown in the media controls. | `Standard` |
| `MEDIA_CONTROLS_RESUME` | `"qs_media_resumption"` | Whether or not to enable media resumption When enabled, media controls in quick settings will populate on boot and persist if resumable via a MediaBrowserService. | `@Readable` |
| `SHOW_MEDIA_WHEN_BYPASSING` | `"show_media_when_bypassing"` | Whether or not media is shown automatically when bypassing as a heads up. | `@Readable` |
| `STATUS_BAR_SHOW_VIBRATE_ICON` | `"status_bar_show_vibrate_icon"` | Whether to show the vibrate icon in the Status Bar (default off) | `Standard` |
| `SYNC_PARENT_SOUNDS` | `"sync_parent_sounds"` | Defines whether managed profile ringtones should be synced from it's parent profile 0 = ringtones are not synced 1 = ringtones are synced from the profile's parent (default) This value is only used for managed profiles. | `@Readable` `@TestApi` `@RequiresPermission(Manifest.permission.WRITE_SECURE_SETTINGS)` |
| `TTS_DEFAULT_COUNTRY` | `"tts_default_country"` | Default text-to-speech country. Sandwich release. Apps should never need to read this setting directly, instead can query the TextToSpeech framework classes for the default locale. {@link TextToSpeech#getLanguage()}. | `@Readable` `@Deprecated` |
| `TTS_DEFAULT_LANG` | `"tts_default_lang"` | Default text-to-speech language. Sandwich release. Apps should never need to read this setting directly, instead can query the TextToSpeech framework classes for the default locale. {@link TextToSpeech#getLanguage()}. | `@Readable` `@Deprecated` |
| `TTS_DEFAULT_LOCALE` | `"tts_default_locale"` | Stores the default tts locales on a per engine basis. Stored as a comma seperated list of values, each value being of the form {@code engine_name:locale} for example, {@code com.foo.ttsengine:eng-USA,com.bar.ttsengine:esp-ESP}. This supersedes {@link #TTS_DEFAULT_LANG}, {@link #TTS_DEFAULT_COUNTRY} and {@link #TTS_DEFAULT_VARIANT}. Apps should never need to read this setting directly, and can query the TextToSpeech framework classes for the locale that is in use. | `@Readable` |
| `TTS_DEFAULT_PITCH` | `"tts_default_pitch"` | Default text-to-speech engine pitch. 100 = 1x | `@Readable` |
| `TTS_DEFAULT_RATE` | `"tts_default_rate"` | Default text-to-speech engine speech rate. 100 = 1x | `@Readable` |
| `TTS_DEFAULT_SYNTH` | `"tts_default_synth"` | Default text-to-speech engine. | `@Readable` |
| `TTS_DEFAULT_VARIANT` | `"tts_default_variant"` | Default text-to-speech locale variant. Sandwich release. Apps should never need to read this setting directly, instead can query the TextToSpeech framework classes for the locale that is in use {@link TextToSpeech#getLanguage()}. | `@Readable` `@Deprecated` |
| `TTS_USE_DEFAULTS` | `"tts_use_defaults"` | Setting to always use the default text-to-speech settings regardless of the application settings. 1 = override application settings, 0 = use application settings (if specified). the framework text to speech APIs as of the Ice Cream Sandwich release. | `@Readable` `@Deprecated` |
| `UNSAFE_VOLUME_MUSIC_ACTIVE_MS` | `"unsafe_volume_music_active_ms"` | Persisted playback time after a user confirmation of an unsafe volume level. | `@Readable` |
| `VOLUME_DIALOG_DISMISS_TIMEOUT` | `"volume_dialog_dismiss_timeout"` | Volume dialog timeout in ms. | `Standard` |
| `VOLUME_HUSH_GESTURE` | `"volume_hush_gesture"` | What behavior should be invoked when the volume hush gesture is triggered One of VOLUME_HUSH_OFF, VOLUME_HUSH_VIBRATE, VOLUME_HUSH_MUTE. | `@Readable` `@SystemApi` |

</details>

<details open>
<summary><h3 style="display:inline;" id="connectivity-wi-fi-network">Connectivity, Wi-Fi & Network (160 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACCESSIBILITY_AUTOCLICK_ENABLED` | `"accessibility_autoclick_enabled"` | Setting that specifies whether automatic click when the mouse pointer stops moving is enabled. | `@Readable` `@UnsupportedAppUsage` |
| `ACCESSIBILITY_CAPTIONING_ENABLED` | `"accessibility_captioning_enabled"` | Setting that specifies whether timed text (captions) should be displayed in video content. Text display properties are controlled by the following settings: {@link #ACCESSIBILITY_CAPTIONING_LOCALE} {@link #ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR} {@link #ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR} {@link #ACCESSIBILITY_CAPTIONING_EDGE_COLOR} {@link #ACCESSIBILITY_CAPTIONING_EDGE_TYPE} {@link #ACCESSIBILITY_CAPTIONING_TYPEFACE} {@link #ACCESSIBILITY_CAPTIONING_FONT_SCALE} | `@Readable` |
| `ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED` | `"accessibility_display_daltonizer_enabled"` | Setting that specifies whether display color space adjustment is enabled. | `@Readable` `@UnsupportedAppUsage` |
| `ACCESSIBILITY_DISPLAY_INVERSION_ENABLED` | `"accessibility_display_inversion_enabled"` | Setting that specifies whether display color inversion is enabled. | `@Readable` |
| `ACCESSIBILITY_DISPLAY_MAGNIFICATION_EDGE_HAPTIC_ENABLED` | `"accessibility_display_magnification_edge_haptic_enabled"` | Whether the feature that the device will fire a haptic when users scroll and hit the edge of the screen is enabled. | `Standard` |
| `ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED` | `"accessibility_display_magnification_enabled"` | Setting that specifies whether the display magnification is enabled via a system-wide triple tap gesture. Display magnifications allows the user to zoom in the display content and is targeted to low vision users. The current magnification scale is controlled by {@link #ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE}. | `@Readable` `@TestApi` `@UnsupportedAppUsage` |
| `ACCESSIBILITY_DISPLAY_MAGNIFICATION_NAVBAR_ENABLED` | `"accessibility_display_magnification_navbar_enabled"` | Setting that specifies whether the display magnification is enabled via a shortcut affordance within the system's navigation area. Display magnifications allows the user to zoom in the display content and is targeted to low vision users. The current magnification scale is controlled by {@link #ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE}. {@link #ACCESSIBILITY_BUTTON_TARGETS} holds the magnification system class name when navigation bar magnification is enabled. | `@Readable` `@SystemApi` |
| `ACCESSIBILITY_ENABLED` | `"accessibility_enabled"` | If accessibility is enabled. | `@Readable` |
| `ACCESSIBILITY_FLOATING_MENU_FADE_ENABLED` | `"accessibility_floating_menu_fade_enabled"` | Whether the fade effect for the accessibility floating menu is enabled. | `Standard` |
| `ACCESSIBILITY_FORCE_INVERT_COLOR_ENABLED` | `"accessibility_force_invert_color_enabled"` | For the force dark theme feature which inverts any apps that don't already support dark theme. If true, it will automatically invert any app that is mainly light. This is related to the force dark override setting, however it will always force the apps colors and will ignore any developer hints or opt-out APIs. | `@Readable` |
| `ACCESSIBILITY_HIGH_TEXT_CONTRAST_ENABLED` | `"high_text_contrast_enabled"` | Whether to draw text with high contrast while in accessibility mode. | `@Readable` |
| `ACCESSIBILITY_MAGNIFICATION_ALWAYS_ON_ENABLED` | `"accessibility_magnification_always_on_enabled"` | Whether the magnification always on feature is enabled. If true, the magnifier will not deactivate on Activity transitions; it will only zoom out to 100%. | `Standard` |
| `ACCESSIBILITY_MAGNIFICATION_FOLLOW_TYPING_ENABLED` | `"accessibility_magnification_follow_typing_enabled"` | Whether the following typing focus feature for magnification is enabled. | `Standard` |
| `ACCESSIBILITY_MAGNIFICATION_JOYSTICK_ENABLED` | `"accessibility_magnification_joystick_enabled"` | Whether the magnification joystick controller feature is enabled. | `Standard` |
| `ACCESSIBILITY_MAGNIFICATION_TWO_FINGER_TRIPLE_TAP_ENABLED` | `"accessibility_magnification_two_finger_triple_tap_enabled"` | Setting that specifies whether the display magnification is enabled via a system-wide two fingers triple tap gesture. | `Standard` |
| `ACCESSIBILITY_MOUSE_KEYS_ENABLED` | `"accessibility_mouse_keys_enabled"` | Whether to enable mouse keys for Physical Keyboard accessibility. If set to true, key presses (of the mouse keys) on physical keyboard will control mouse pointer on the display. | `@Readable` |
| `ACCESSIBILITY_PINCH_TO_ZOOM_ANYWHERE_ENABLED` | `"accessibility_pinch_to_zoom_anywhere_enabled"` | For pinch to zoom anywhere feature. If true, you should be able to pinch to magnify the window anywhere. | `Standard` |
| `ACCESSIBILITY_SINGLE_FINGER_PANNING_ENABLED` | `"accessibility_single_finger_panning_enabled"` | For magnification feature where panning can be controlled with a single finger. If true, you can pan using a single finger gesture. | `Standard` |
| `ADAPTIVE_CHARGING_ENABLED` | `"adaptive_charging_enabled"` | Whether or not adaptive charging feature is enabled by user. Type: int (0 for false, 1 for true) Default: 1 | `Standard` |
| `ADAPTIVE_CONNECTIVITY_ENABLED` | `"adaptive_connectivity_enabled"` | Whether the Adaptive connectivity option is enabled. | `Standard` |
| `ASSIST_DISCLOSURE_ENABLED` | `"assist_disclosure_enabled"` | Specifies whether the screen will show an animation if screen contents are sent to the assist application (active voice interaction service). Note that the disclosure will be forced for third-party assistants or if the device does not support disabling it. | `@Readable` |
| `ASSIST_GESTURE_ENABLED` | `"assist_gesture_enabled"` | Whether the assist gesture should be enabled. | `@Readable` |
| `ASSIST_GESTURE_SILENCE_ALERTS_ENABLED` | `"assist_gesture_silence_alerts_enabled"` | Whether the assist gesture should silence alerts. | `@Readable` |
| `ASSIST_GESTURE_WAKE_ENABLED` | `"assist_gesture_wake_enabled"` | Whether the assist gesture should wake the phone. | `@Readable` |
| `ASSIST_LONG_PRESS_HOME_ENABLED` | `"assist_long_press_home_enabled"` | Whether the assistant can be triggered by long-pressing the home button | `Standard` |
| `ASSIST_SCREENSHOT_ENABLED` | `"assist_screenshot_enabled"` | Specifies whether a screenshot of the screen contents will be sent to the assist application (active voice interaction service). | `@Readable` |
| `ASSIST_STRUCTURE_ENABLED` | `"assist_structure_enabled"` | Specifies whether the current app context on scren (assist data) will be sent to the assist application (active voice interaction service). | `@Readable` |
| `ASSIST_TOUCH_GESTURE_ENABLED` | `"assist_touch_gesture_enabled"` | Whether the assistant can be triggered by a touch gesture. | `Standard` |
| `AUDIO_SAFE_CSD_AS_A_FEATURE_ENABLED` | `"audio_safe_csd_as_a_feature_enabled"` | Stores a boolean that defines whether the CSD as a feature is enabled or not. | `Standard` |
| `AUTOFILL_USER_DATA_MAX_CATEGORY_COUNT` | `"autofill_user_data_max_category_count"` | Defines value returned by {@link android.service.autofill.UserData#getMaxCategoryCount()}. | `@Readable` `@SystemApi` |
| `AUTOFILL_USER_DATA_MAX_FIELD_CLASSIFICATION_IDS_SIZE` | `"autofill_user_data_max_field_classification_size"` | Defines value returned by {@link android.service.autofill.UserData#getMaxFieldClassificationIdsSize()}. | `@Readable` `@SystemApi` |
| `AUTOFILL_USER_DATA_MAX_USER_DATA_SIZE` | `"autofill_user_data_max_user_data_size"` | Defines value returned by {@link android.service.autofill.UserData#getMaxUserDataSize()}. | `@Readable` `@SystemApi` |
| `AUTOFILL_USER_DATA_MAX_VALUE_LENGTH` | `"autofill_user_data_max_value_length"` | Defines value returned by {@link android.service.autofill.UserData#getMaxValueLength()}. | `@Readable` `@SystemApi` |
| `AUTOFILL_USER_DATA_MIN_VALUE_LENGTH` | `"autofill_user_data_min_value_length"` | Defines value returned by {@link android.service.autofill.UserData#getMinValueLength()}. | `@Readable` `@SystemApi` |
| `AUTOMATIC_STORAGE_MANAGER_ENABLED` | `"automatic_storage_manager_enabled"` | Whether or not the automatic storage manager is enabled and should run on the device. | `@Readable` |
| `AUTO_REVOKE_DISABLED` | `"auto_revoke_disabled"` | Whether or not the system Auto Revoke feature is disabled. | `@Readable` `@SystemApi` |
| `AWARE_ENABLED` | `"aware_enabled"` | Controls whether aware is enabled. | `@Readable` |
| `AWARE_LOCK_ENABLED` | `"aware_lock_enabled"` | Controls whether aware_lock is enabled. | `@Readable` |
| `BACKGROUND_DATA` | `"background_data"` | Whether background data usage is allowed. availability of background data depends on several combined factors. When background data is unavailable, {@link ConnectivityManager#getActiveNetworkInfo()} will now appear disconnected. | `@Readable` `@Deprecated` |
| `BACKUP_ENABLED` | `"backup_enabled"` | /** Controls whether settings backup is enabled. Type: int ( 0 = disabled, 1 = enabled ) | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `BACKUP_SCHEDULING_ENABLED` | `"backup_scheduling_enabled"` | Controls whether framework backup scheduling is enabled. | `Standard` |
| `BIOMETRIC_APP_ENABLED` | `"biometric_app_enabled"` | Whether or not biometric is allowed for apps (through BiometricPrompt). | `@Readable` |
| `BIOMETRIC_DEBUG_ENABLED` | `"biometric_debug_enabled"` | Whether or not debugging is enabled. | `@Readable` |
| `BIOMETRIC_FACE_VIRTUAL_ENABLED` | `"biometric_face_virtual_enabled"` | Whether or not face virtual sensors are enabled. | `@FlaggedApi("com.android.server.biometrics.face_vhal_feature")` |
| `BIOMETRIC_FINGERPRINT_VIRTUAL_ENABLED` | `"biometric_fingerprint_virtual_enabled"` | Whether or not fingerprint virtual sensors are enabled. | `@FlaggedApi("com.android.server.biometrics.face_vhal_feature")` |
| `BIOMETRIC_KEYGUARD_ENABLED` | `"biometric_keyguard_enabled"` | Whether or not biometric is allowed on Keyguard. | `@Readable` |
| `BIOMETRIC_VIRTUAL_ENABLED` | `"biometric_virtual_enabled"` | Whether or not both fingerprint and face virtual sensors are enabled. | `@Readable` `@TestApi` |
| `BLUETOOTH_ADDRESS` | `"bluetooth_address"` | This is used by Bluetooth Manager to store adapter address | `@Readable(maxTargetSdk = Build.VERSION_CODES.S)` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLUETOOTH_ADDR_VALID` | `"bluetooth_addr_valid"` | This is used by Bluetooth Manager to store whether adapter address is valid | `@Readable(maxTargetSdk = Build.VERSION_CODES.S)` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLUETOOTH_LE_BROADCAST_APP_SOURCE_NAME` | `"bluetooth_le_broadcast_app_source_name"` | This is used by LocalBluetoothLeBroadcast to store the app source name. | `Standard` |
| `BLUETOOTH_LE_BROADCAST_CODE` | `"bluetooth_le_broadcast_code"` | This is used by LocalBluetoothLeBroadcast to store the broadcast code. | `Standard` |
| `BLUETOOTH_LE_BROADCAST_FALLBACK_ACTIVE_DEVICE_ADDRESS` | `"bluetooth_le_broadcast_fallback_active_device_address"` | This is used by LocalBluetoothLeBroadcast to store the fallback active device address. | `Standard` |
| `BLUETOOTH_LE_BROADCAST_IMPROVE_COMPATIBILITY` | `"bluetooth_le_broadcast_improve_compatibility"` | This is used by LocalBluetoothLeBroadcast to downgrade the broadcast quality to improve compatibility. 0 = false 1 = true | `Standard` |
| `BLUETOOTH_LE_BROADCAST_NAME` | `"bluetooth_le_broadcast_name"` | This is used by LocalBluetoothLeBroadcast to store the broadcast name. | `Standard` |
| `BLUETOOTH_LE_BROADCAST_PROGRAM_INFO` | `"bluetooth_le_broadcast_program_info"` | This is used by LocalBluetoothLeBroadcast to store the broadcast program info. | `Standard` |
| `BLUETOOTH_NAME` | `"bluetooth_name"` | This is used by Bluetooth Manager to store adapter name | `@Readable(maxTargetSdk = Build.VERSION_CODES.S)` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLUETOOTH_ON_WHILE_DRIVING` | `"bluetooth_on_while_driving"` | Flag to set if the system should predictively attempt to re-enable Bluetooth while the user is driving. | `@Readable` |
| `BUBBLE_IMPORTANT_CONVERSATIONS` | `"bubble_important_conversations"` | When enabled conversations marked as favorites will be set to bubble. The value 1 - enable, 0 - disable | `@Readable` |
| `CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED` | `"camera_double_tap_power_gesture_disabled"` | Whether the camera launch gesture to double tap the power button when the screen is off should be disabled. | `@Readable` |
| `CAMERA_DOUBLE_TWIST_TO_FLIP_ENABLED` | `"camera_double_twist_to_flip_enabled"` | Whether the camera double twist gesture to flip between front and back mode should be enabled. | `@Readable` |
| `CAMERA_GESTURE_DISABLED` | `"camera_gesture_disabled"` | Whether the camera launch gesture should be disabled. | `@Readable` |
| `CAMERA_LIFT_TRIGGER_ENABLED` | `"camera_lift_trigger_enabled"` | Whether or not the smart camera lift trigger that launches the camera when the user moves the phone into a position for taking photos should be enabled. | `@Readable` |
| `CHARGING_SOUNDS_ENABLED` | `"charging_sounds_enabled"` | Whether to play a sound for charging events. | `@Readable` |
| `CHARGING_VIBRATION_ENABLED` | `"charging_vibration_enabled"` | Whether to vibrate for charging events. | `@Readable` |
| `COMMUNAL_MODE_ENABLED` | `"communal_mode_enabled"` | Control whether communal mode is allowed on this device. | `Standard` |
| `COMMUNAL_MODE_TRUSTED_NETWORKS` | `"communal_mode_trusted_networks"` | An array of SSIDs of Wi-Fi networks that, when connected, are considered safe to enable the communal mode. | `Standard` |
| `CONNECTIVITY_RELEASE_PENDING_INTENT_DELAY_MS` | `"connectivity_release_pending_intent_delay_ms"` | The number of milliseconds to hold on to a PendingIntent based request. This delay gives the receivers of the PendingIntent an opportunity to make a new network request before the Network satisfying the request is potentially removed. | `@Readable` |
| `CONTENT_CAPTURE_ENABLED` | `"content_capture_enabled"` | Defines whether Content Capture is enabled for the user. Type: {@code int} ({@code 0} for disabled, {@code 1} for enabled). Default: enabled | `@Readable` `@TestApi` |
| `CONTEXTUAL_SCREEN_TIMEOUT_ENABLED` | `"contextual_screen_timeout_enabled"` | Whether contextual screen timeout is enabled. | `Standard` |
| `CONTROLS_ENABLED` | `"controls_enabled"` | Whether this user has enabled Quick controls. 0 indicates disabled and 1 indicates enabled. A non existent value should be treated as enabled. be removed in a future release. | `@Readable` `@Deprecated` |
| `CROSS_PROFILE_CALENDAR_ENABLED` | `"cross_profile_calendar_enabled"` | Whether parent profile can access remote calendar data in managed profile. | `@Readable` |
| `DISABLED_PRINT_SERVICES` | `"disabled_print_services"` | List of the disabled print services. | `@Readable` `@TestApi` |
| `DISABLED_SYSTEM_INPUT_METHODS` | `"disabled_system_input_methods"` | List of system input methods that are currently disabled. This is a string containing the IDs of all disabled input methods, each ID separated by ':'. | `@Readable(maxTargetSdk = Build.VERSION_CODES.TIRAMISU)` |
| `DISABLE_SECURE_WINDOWS` | `"disable_secure_windows"` | Whether or not secure windows should be disabled. This only works on debuggable builds. When this setting is set to a non-zero value, all windows are treated as non-secure. Content in windows with {@link android.view.WindowManager.LayoutParams#FLAG_SECURE} will appear in screenshots and recordings. | `Standard` |
| `DISPLAY_WHITE_BALANCE_ENABLED` | `"display_white_balance_enabled"` | Control whether display white balance is currently enabled. | `@Readable` |
| `DOUBLE_TAP_POWER_BUTTON_GESTURE` | `"double_tap_power_button_gesture"` | Double tap power button gesture behavior. 0 = Camera launch 1 = Wallet launch | `@Readable` |
| `DOUBLE_TAP_POWER_BUTTON_GESTURE_ENABLED` | `"double_tap_power_button_gesture_enabled"` | Whether double tap the power button gesture is enabled. | `@Readable` |
| `DOUBLE_TAP_TO_WAKE` | `"double_tap_to_wake"` | Controls whether double tap to wake is enabled. | `@Readable` |
| `DOZE_DOUBLE_TAP_GESTURE` | `"doze_pulse_on_double_tap"` | Whether the device should pulse on double tap gesture. | `@Readable` |
| `DOZE_ENABLED` | `"doze_enabled"` | Whether the device should doze if configured. | `@Readable` `@UnsupportedAppUsage` |
| `EMERGENCY_GESTURE_ENABLED` | `"emergency_gesture_enabled"` | Whether the emergency gesture should be enabled. | `Standard` |
| `EMERGENCY_GESTURE_SOUND_ENABLED` | `"emergency_gesture_sound_enabled"` | Whether the emergency gesture sound should be enabled. | `Standard` |
| `EMERGENCY_THERMAL_ALERT_DISABLED` | `"emergency_thermal_alert_disabled"` | Whether the emergency thermal alert would be disabled (0: default) or not (1). | `Standard` |
| `ENABLED_ACCESSIBILITY_AUDIO_DESCRIPTION_BY_DEFAULT` | `"enabled_accessibility_audio_description_by_default"` | Whether select sound track with audio description by default. | `Standard` |
| `ENABLED_ACCESSIBILITY_SERVICES` | `"enabled_accessibility_services"` | List of the enabled accessibility providers. | `@Readable` |
| `ENABLED_INPUT_METHODS` | `"enabled_input_methods"` | List of input methods that are currently enabled. This is a string containing the IDs of all enabled input methods, each ID separated by ':'. Format like "ime0;subtype0;subtype1;subtype2:ime1:ime2;subtype0" where imeId is ComponentName and subtype is int32. Note: This setting is not readable to the app targeting API level 34 or higher. use {@link android.view.inputmethod.InputMethodManager#getEnabledInputMethodList()} instead. | `@Readable(maxTargetSdk = Build.VERSION_CODES.TIRAMISU)` |
| `ENABLED_NOTIFICATION_ASSISTANT` | `"enabled_notification_assistant"` | Read only list of the service components that the current user has explicitly allowed to see and assist with all of the user's notifications. {@link NotificationManager#isNotificationAssistantAccessGranted(ComponentName)}. | `@Readable` `@Deprecated` |
| `ENABLED_NOTIFICATION_LISTENERS` | `"enabled_notification_listeners"` | Read only list of the service components that the current user has explicitly allowed to see all of the user's notifications, separated by ':'. {@link NotificationManager#isNotificationListenerAccessGranted(ComponentName)}. | `@Readable` `@UnsupportedAppUsage` `@Deprecated` |
| `ENABLED_NOTIFICATION_POLICY_ACCESS_PACKAGES` | `"enabled_notification_policy_access_packages"` | Read only list of the packages that the current user has explicitly allowed to manage do not disturb, separated by ':'. | `@Readable` `@TestApi` `@Deprecated` |
| `ENABLED_PRINT_SERVICES` | `"enabled_print_services"` | List of the enabled print services. N and beyond uses {@link #DISABLED_PRINT_SERVICES}. But this might be used in an upgrade from pre-N. | `@Readable` `@UnsupportedAppUsage` |
| `ENABLED_VR_LISTENERS` | `"enabled_vr_listeners"` | Names of the service components that the current user has explicitly allowed to be a VR mode listener, separated by ':'. | `@Readable` `@TestApi` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `ENHANCED_VOICE_PRIVACY_ENABLED` | `"enhanced_voice_privacy_enabled"` | Whether the enhanced voice privacy mode is enabled. 0 = normal voice privacy 1 = enhanced voice privacy | `@Readable` |
| `FACE_UNLOCK_APP_ENABLED` | `"face_unlock_app_enabled"` | Whether or not face unlock is allowed for apps (through BiometricPrompt). | `@Readable` |
| `FACE_UNLOCK_KEYGUARD_ENABLED` | `"face_unlock_keyguard_enabled"` | Whether or not face unlock is allowed on Keyguard. | `@Readable` |
| `FLASHLIGHT_AVAILABLE` | `"flashlight_available"` | Whether or not the flashlight (camera torch mode) is available required to turn on flashlight. | `@Readable` |
| `FLASHLIGHT_ENABLED` | `"flashlight_enabled"` | Whether or not flashlight is enabled. | `@Readable` |
| `GLANCEABLE_HUB_ENABLED` | `"glanceable_hub_enabled"` | Defines the enabled state for the glanceable hub. | `Standard` |
| `GLOBAL_ACTIONS_PANEL_AVAILABLE` | `"global_actions_panel_available"` | Whether the Global Actions Panel can be toggled on or off in Settings. | `@Readable` |
| `GLOBAL_ACTIONS_PANEL_DEBUG_ENABLED` | `"global_actions_panel_debug_enabled"` | Enables debug mode for the Global Actions Panel. | `@Readable` |
| `GLOBAL_ACTIONS_PANEL_ENABLED` | `"global_actions_panel_enabled"` | Whether the Global Actions Panel is enabled. | `@Readable` |
| `HINGE_ANGLE_LIDEVENT_ENABLED` | `"hinge_angle_lidevent_enabled"` | Whether hinge angle lidevent is enabled. | `Standard` |
| `INSTANT_APPS_ENABLED` | `"instant_apps_enabled"` | Specifies whether the web action API is enabled. | `@Readable` `@SystemApi` |
| `IN_CALL_NOTIFICATION_ENABLED` | `"in_call_notification_enabled"` | Whether the in call notification is enabled to play sound during calls. The value is boolean (1 or 0). | `@Readable` |
| `KEY_REPEAT_ENABLED` | `"key_repeat_enabled"` | Whether to enable key repeats for Physical Keyboard. If set to false, continuous key presses on physical keyboard will not cause the pressed key to repeated. | `@Readable` |
| `LOCATION_TIME_ZONE_DETECTION_ENABLED` | `"location_time_zone_detection_enabled"` | The current location time zone detection enabled state for the user. See {@link android.app.time.TimeManager#getTimeZoneCapabilitiesAndConfig} for access. See {@link android.app.time.TimeManager#updateTimeZoneConfiguration} to update. | `Standard` |
| `LOCKSCREEN_USE_DOUBLE_LINE_CLOCK` | `"lockscreen_use_double_line_clock"` | Whether to use the lockscreen double-line clock | `Standard` |
| `LOCK_PATTERN_ENABLED` | `"lock_pattern_autolock"` | Whether autolock is enabled (0 = false, 1 = true) level of the keyguard. Accessing this setting from an app that is targeting {@link VERSION_CODES#M} or later throws a {@code SecurityException}. | `@Readable` `@Deprecated` |
| `LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED` | `"lock_pattern_tactile_feedback_enabled"` | Whether lock pattern will vibrate as user enters (0 = false, 1 = true) lockscreen uses {@link Settings.System#HAPTIC_FEEDBACK_ENABLED}. Accessing this setting from an app that is targeting {@link VERSION_CODES#M} or later throws a {@code SecurityException}. | `@Readable` `@Deprecated` |
| `LOCK_PATTERN_VISIBLE` | `"lock_pattern_visible_pattern"` | Whether lock pattern is visible as user enters (0 = false, 1 = true) {@link VERSION_CODES#M} or later throws a {@code SecurityException}. | `@Readable` `@Deprecated` |
| `LOCK_SCREEN_OWNER_INFO_ENABLED` | `"lock_screen_owner_info_enabled"` | This preference enables showing the owner info on LockScreen. | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` `@Deprecated` |
| `LOCK_SCREEN_WEATHER_ENABLED` | `"lockscreen_weather_enabled"` | Whether lockscreen weather is enabled. | `Standard` |
| `MOUNT_UMS_NOTIFY_ENABLED` | `"mount_ums_notify_enabled"` | Whether or not a notification is displayed while UMS is enabled. (0 = false, 1 = true) | `@Readable` |
| `NAV_BAR_FORCE_VISIBLE` | `"nav_bar_force_visible"` | Indicates whether the nav bar is forced to always be visible, even in immersive mode. Type: int (0 for false, 1 for true) | `Standard` |
| `NFC_PAYMENT_DEFAULT_COMPONENT` | `"nfc_payment_default_component"` | The default NFC payment component with {@link android.app.role.RoleManager#ROLE_WALLET} parameter. | `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` `@Deprecated` |
| `NFC_PAYMENT_FOREGROUND` | `"nfc_payment_foreground"` | Whether NFC payment is handled by the foreground application or a default. | `@Readable` |
| `NOTIFICATION_BUBBLES` | `"notification_bubbles"` | When enabled, notifications able to bubble will display an affordance allowing the user to bubble them. The value is boolean (1 to enable or 0 to disable). | `@Readable` `@TestApi` |
| `NOTIFICATION_HISTORY_ENABLED` | `"notification_history_enabled"` | When enabled the system will maintain a rolling history of received notifications. When disabled the history will be disabled and deleted. The value 1 - enable, 0 - disable | `@Readable` |
| `ODI_CAPTIONS_ENABLED` | `"odi_captions_enabled"` | Setting to indicate that on device captions are enabled. | `@Readable` `@SystemApi` |
| `ODI_CAPTIONS_VOLUME_UI_ENABLED` | `"odi_captions_volume_ui_enabled"` | Setting to indicate live caption button show or hide in the volume rocker. | `Standard` |
| `ONE_HANDED_MODE_ENABLED` | `"one_handed_mode_enabled"` | For user preference if One-Handed Mode enabled. | `Standard` |
| `PACKAGES_TO_CLEAR_DATA_BEFORE_FULL_RESTORE` | `"packages_to_clear_data_before_full_restore"` | List of packages, which data need to be unconditionally cleared before full restore. Type: string | `@Readable` |
| `PARENTAL_CONTROL_ENABLED` | `"parental_control_enabled"` | No longer supported. | `@Readable` |
| `SCREENSAVER_COMPLICATIONS_ENABLED` | `"screensaver_complications_enabled"` | Whether complications are enabled to be shown over the screensaver by the user. | `Standard` |
| `SCREENSAVER_ENABLED` | `"screensaver_enabled"` | Whether screensavers are enabled. | `@Readable` |
| `SCREENSAVER_HOME_CONTROLS_ENABLED` | `"screensaver_home_controls_enabled"` | Whether home controls are enabled to be shown over the screensaver by the user. | `Standard` |
| `SCREEN_OFF_UNLOCK_UDFPS_ENABLED` | `"screen_off_udfps_enabled"` | Whether or not the UDFPS device is enabling the screen off unlock settings. | `Standard` |
| `SEARCH_ALL_ENTRYPOINTS_ENABLED` | `"search_all_entrypoints_enabled"` | Whether all entrypoints (e.g. long-press home, long-press nav handle) can trigger contextual search. | `Standard` |
| `SFPS_PERFORMANT_AUTH_ENABLED` | `"sfps_performant_auth_enabled_v2"` | Whether or not a SFPS device is enabling the performant auth setting. The "_V2" suffix was added to re-introduce the default behavior for users. See b/265264294 fore more details. | `Standard` |
| `SPATIAL_AUDIO_ENABLED` | `"spatial_audio_enabled"` | Indicates whether the spatial audio feature was enabled for this user. Type : int (0 disabled, 1 enabled) | `Standard` |
| `SPELL_CHECKER_ENABLED` | `"spell_checker_enabled"` | Whether spell checker is enabled or not. | `@Readable` |
| `STYLUS_BUTTONS_ENABLED` | `"stylus_buttons_enabled"` | Whether stylus button presses are disabled. This is a boolean that determines if stylus buttons are ignored. | `@Readable` `@TestApi` |
| `STYLUS_HANDWRITING_ENABLED` | `"stylus_handwriting_enabled"` | Toggle for enabling stylus handwriting. When enabled, current Input method receives stylus {@link MotionEvent}s if an {@link Editor} is focused. | `@Readable` `@TestApi` |
| `STYLUS_POINTER_ICON_ENABLED` | `"stylus_pointer_icon_enabled"` | Toggle for enabling stylus pointer icon. Pointer icons for styluses will only be be shown when this is enabled. Enabling this alone won't enable the stylus pointer; config_enableStylusPointerIcon needs to be true as well. | `@Readable` |
| `SWIPE_BOTTOM_TO_NOTIFICATION_ENABLED` | `"swipe_bottom_to_notification_enabled"` | For user preference if swipe bottom to expand notification gesture enabled. | `Standard` |
| `SYSTEM_NAVIGATION_KEYS_ENABLED` | `"system_navigation_keys_enabled"` | Whether SystemUI navigation keys is enabled. | `@Readable` |
| `TOUCH_EXPLORATION_ENABLED` | `"touch_exploration_enabled"` | If touch exploration is enabled. | `@Readable` |
| `TRACKPAD_GESTURE_BACK_ENABLED` | `"trackpad_gesture_back_enabled"` | Indicates whether the trackpad back gesture is enabled. Type: int (0 for false, 1 for true) | `Standard` |
| `TRACKPAD_GESTURE_HOME_ENABLED` | `"trackpad_gesture_home_enabled"` | Indicates whether the trackpad home gesture is enabled. Type: int (0 for false, 1 for true) | `Standard` |
| `TRACKPAD_GESTURE_NOTIFICATION_ENABLED` | `"trackpad_gesture_notification_enabled"` | Indicates whether the trackpad notification gesture is enabled. Type: int (0 for false, 1 for true) | `Standard` |
| `TRACKPAD_GESTURE_OVERVIEW_ENABLED` | `"trackpad_gesture_overview_enabled"` | Indicates whether the trackpad overview gesture is enabled. Type: int (0 for false, 1 for true) | `Standard` |
| `TRACKPAD_GESTURE_QUICK_SWITCH_ENABLED` | `"trackpad_gesture_quick_switch_enabled"` | Indicates whether the trackpad quick switch gesture is enabled. Type: int (0 for false, 1 for true) | `Standard` |
| `TTS_ENABLED_PLUGINS` | `"tts_enabled_plugins"` | Space delimited list of plugin packages that are enabled. | `@Readable` |
| `TTY_MODE_ENABLED` | `"tty_mode_enabled"` | Whether the TTY mode mode is enabled. 0 = disabled 1 = enabled | `@Readable` |
| `UI_TRANSLATION_ENABLED` | `"ui_translation_enabled"` | Toggle to enable/disable for the apps to use the Ui translation for Views. The value indicates whether the Ui translation is enabled by the user. Type: {@code int} ({@code 0} for disabled, {@code 1} for enabled) | `@Readable` `@SystemApi` |
| `USB_AUDIO_AUTOMATIC_ROUTING_DISABLED` | `"usb_audio_automatic_routing_disabled"` | Whether automatic routing of system audio to USB audio peripheral is disabled. The value is boolean (1 or 0), where 1 means automatic routing is disabled, and 0 means automatic routing is enabled. | `@Readable` |
| `VISUAL_QUERY_ACCESSIBILITY_DETECTION_ENABLED` | `"visual_query_accessibility_detection_enabled"` | Whether or not the accessibility data streaming is enbled for the {@link VisualQueryDetectedResult#setAccessibilityDetectionData}. | `Standard` |
| `WAKE_GESTURE_ENABLED` | `"wake_gesture_enabled"` | Whether the device should wake when the wake gesture sensor detects motion. | `@Readable` |
| `WEAR_TALKBACK_ENABLED` | `"wear_talkback_enabled"` | Is talkback service enabled or not. 0 == no, 1 == yes | `Standard` |
| `WIFI_WATCHDOG_ACCEPTABLE_PACKET_LOSS_PERCENTAGE` | `"wifi_watchdog_acceptable_packet_loss_percentage"` | The acceptable packet loss percentage (range 0 - 100) before trying another AP on the same network. | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_AP_COUNT` | `"wifi_watchdog_ap_count"` | The number of access points required for a network in order for the watchdog to monitor it. | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_BACKGROUND_CHECK_DELAY_MS` | `"wifi_watchdog_background_check_delay_ms"` | The delay between background checks. | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_BACKGROUND_CHECK_ENABLED` | `"wifi_watchdog_background_check_enabled"` | Whether the Wi-Fi watchdog is enabled for background checking even after it thinks the user has connected to a good access point. | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_BACKGROUND_CHECK_TIMEOUT_MS` | `"wifi_watchdog_background_check_timeout_ms"` | The timeout for a background ping | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_INITIAL_IGNORED_PING_COUNT` | `"wifi_watchdog_initial_ignored_ping_count"` | The number of initial pings to perform that *may* be ignored if they fail. Again, if these fail, they will *not* be used in packet loss calculation. For example, one network always seemed to time out for the first couple pings, so this is set to 3 by default. | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_MAX_AP_CHECKS` | `"wifi_watchdog_max_ap_checks"` | The maximum number of access points (per network) to attempt to test. If this number is reached, the watchdog will no longer monitor the initial connection state for the network. This is a safeguard for networks containing multiple APs whose DNS does not respond to pings. | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_ON` | `"wifi_watchdog_on"` | No explicit description provided in source comments. | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_PING_COUNT` | `"wifi_watchdog_ping_count"` | The number of pings to test if an access point is a good connection. | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_PING_DELAY_MS` | `"wifi_watchdog_ping_delay_ms"` | The delay between pings. | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_PING_TIMEOUT_MS` | `"wifi_watchdog_ping_timeout_ms"` | The timeout per ping. | `@Readable` `@Deprecated` |
| `WIFI_WATCHDOG_WATCH_LIST` | `"wifi_watchdog_watch_list"` | A comma-separated list of SSIDs for which the Wi-Fi watchdog should be enabled. | `@Readable` `@Deprecated` |

</details>

<details open>
<summary><h3 style="display:inline;" id="developer-options-debugging">Developer Options & Debugging (2 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `LOGGING_ID` | `"logging_id"` | The Logging ID (a unique 64-bit value) as a hex string. Used as a pseudonymous identifier for logging. many collisions. It should not be used. | `@Readable` `@Deprecated` |
| `SHOW_FIRST_CRASH_DIALOG_DEV_OPTION` | `"show_first_crash_dialog_dev_option"` | If nonzero, crashes in foreground processes will bring up a dialog. Otherwise, the process will be silently killed. | `@Readable` `@TestApi` |

</details>

<details open>
<summary><h3 style="display:inline;" id="display-screen-appearance">Display, Screen & Appearance (67 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR` | `"accessibility_captioning_background_color"` | Integer property that specifes the background color for captions as a packed 32-bit color. | `@Readable` |
| `ACCESSIBILITY_CAPTIONING_EDGE_COLOR` | `"accessibility_captioning_edge_color"` | Integer property that specifes the edge color for captions as a packed 32-bit color. | `@Readable` |
| `ACCESSIBILITY_CAPTIONING_FONT_SCALE` | `"accessibility_captioning_font_scale"` | Floating point property that specifies font scaling for captions. | `@Readable` |
| `ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR` | `"accessibility_captioning_foreground_color"` | Integer property that specifes the foreground color for captions as a packed 32-bit color. | `@Readable` |
| `ACCESSIBILITY_CAPTIONING_WINDOW_COLOR` | `"accessibility_captioning_window_color"` | Integer property that specifes the window color for captions as a packed 32-bit color. | `@Readable` |
| `ACCESSIBILITY_DISPLAY_DALTONIZER` | `"accessibility_display_daltonizer"` | Integer property that specifies the type of color space adjustment to perform. Valid values are defined in AccessibilityManager and Settings arrays.xml: - AccessibilityManager.DALTONIZER_DISABLED = -1 - AccessibilityManager.DALTONIZER_SIMULATE_MONOCHROMACY = 0 - @string/daltonizer_mode_protanomaly = 11 - AccessibilityManager.DALTONIZER_CORRECT_DEUTERANOMALY and @string/daltonizer_mode_deuteranomaly = 12 - @string/daltonizer_mode_tritanomaly = 13 | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `ACCESSIBILITY_DISPLAY_DALTONIZER_SATURATION_LEVEL` | `"accessibility_display_daltonizer_saturation_level"` | Integer property that determines the saturation level of color correction. Default value is defined in Settings config.xml. [0-10] inclusive where 0 would look as if color space adustment is not applied at all. | `Standard` |
| `ACCESSIBILITY_DISPLAY_MAGNIFICATION_AUTO_UPDATE` | `"accessibility_display_magnification_auto_update"` | Unused mangnification setting | `@Readable` `@Deprecated` |
| `ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE` | `"accessibility_display_magnification_scale"` | Setting that specifies what the display magnification scale is. Display magnifications allows the user to zoom in the display content and is targeted to low vision users. Whether a display magnification is performed is controlled by {@link #ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED} and {@link #ACCESSIBILITY_DISPLAY_MAGNIFICATION_NAVBAR_ENABLED} | `@Readable` |
| `ACCESSIBILITY_FONT_SCALING_HAS_BEEN_CHANGED` | `"accessibility_font_scaling_has_been_changed"` | Flag that specifies whether font size has been changed. The flag will be set when users change the scaled value of font size for the first time. | `@Readable` |
| `ACCESSIBILITY_SHORTCUT_ON_LOCK_SCREEN` | `"accessibility_shortcut_on_lock_screen"` | Setting specifying if the accessibility shortcut is enabled. | `@Readable` |
| `ACCESSIBILITY_SHOW_WINDOW_MAGNIFICATION_PROMPT` | `"accessibility_show_window_magnification_prompt"` | Whether to show the window magnification prompt dialog when the user uses full-screen magnification first time after database is upgraded. | `Standard` |
| `CALL_SCREENING_DEFAULT_COMPONENT` | `"call_screening_default_component"` | Specifies the component name currently configured to be the default call screening application | `@Readable` |
| `DARK_MODE_DIALOG_SEEN` | `"dark_mode_dialog_seen"` | Boolean indicating if the dark mode dialog shown on first toggle has been seen. | `@Readable` |
| `DARK_THEME_CUSTOM_END_TIME` | `"dark_theme_custom_end_time"` | Custom time when Dark theme is scheduled to deactivate. Represented as milliseconds from midnight (e.g. 79200000 == 10pm). | `@Readable` |
| `DARK_THEME_CUSTOM_START_TIME` | `"dark_theme_custom_start_time"` | Custom time when Dark theme is scheduled to activate. Represented as milliseconds from midnight (e.g. 79200000 == 10pm). | `@Readable` |
| `DEVICE_STATE_ROTATION_LOCK` | `"device_state_rotation_lock"` | Rotation lock setting keyed on device state. This holds a serialized map using int keys that represent postures in {@link DeviceStateRotationLockKey} and value of {@link DeviceStateRotationLockSetting} representing the rotation lock setting for that posture. Serialized as key0:value0:key1:value1:...:keyN:valueN. Example: "0:1:1:2:2:1" This example represents a map of: DEVICE_STATE_ROTATION_KEY_FOLDED -> DEVICE_STATE_ROTATION_LOCK_LOCKED DEVICE_STATE_ROTATION_KEY_HALF_FOLDED -> DEVICE_STATE_ROTATION_LOCK_UNLOCKED DEVICE_STATE_ROTATION_KEY_UNFOLDED -> DEVICE_STATE_ROTATION_LOCK_IGNORED | `Standard` |
| `DISPLAY_DENSITY_FORCED` | `"display_density_forced"` | The saved value for WindowManagerService.setForcedDisplayDensity() formatted as a single integer representing DPI. If unset, then use the real display density. | `@Readable` |
| `DOZE_TAP_SCREEN_GESTURE` | `"doze_tap_gesture"` | Whether the device should respond to the SLPI tap gesture. | `@Readable` |
| `DOZE_WAKE_DISPLAY_GESTURE` | `"doze_wake_display_gesture"` | Gesture that wakes up the display, toggling between {@link Display.STATE_OFF} and {@link Display.STATE_DOZE}. | `@Readable` |
| `DOZE_WAKE_LOCK_SCREEN_GESTURE` | `"doze_wake_screen_gesture"` | Gesture that wakes up the display, showing some version of the lock screen. | `@Readable` |
| `EVEN_DIMMER_ACTIVATED` | `"even_dimmer_activated"` | Setting that specifies whether Even Dimmer - a feature that allows the brightness slider to go below what the display can conventionally do, should be enabled. | `Standard` |
| `EVEN_DIMMER_MIN_NITS` | `"even_dimmer_min_nits"` | Setting that specifies which nits level Even Dimmer should allow the screen brightness to go down to. | `Standard` |
| `FINGERPRINT_SIDE_FPS_BP_POWER_WINDOW` | `"fingerprint_side_fps_bp_power_window"` | The time (in millis) to wait for a power button before sending a successful auth in biometric prompt(for side fingerprint) | `@Readable` |
| `FINGERPRINT_SIDE_FPS_ENROLL_TAP_WINDOW` | `"fingerprint_side_fps_enroll_tap_window"` | The time (in millis) that a finger tap will wait for a power button before dismissing the power dialog during enrollment(for side fingerprint) | `@Readable` |
| `FINGERPRINT_SIDE_FPS_KG_POWER_WINDOW` | `"fingerprint_side_fps_kg_power_window"` | The time (in millis) to wait for a power button before sending a successful auth in to keyguard(for side fingerprint) | `@Readable` |
| `FONT_WEIGHT_ADJUSTMENT` | `"font_weight_adjustment"` | The adjustment in font weight. This is used to draw text in bold. This value can be negative. To display bolded text, the adjustment used is 300, which is the difference between {@link android.graphics.fonts.FontStyle#FONT_WEIGHT_NORMAL} and {@link android.graphics.fonts.FontStyle#FONT_WEIGHT_BOLD}. | `@Readable` |
| `LOCKSCREEN_ALLOW_TRIVIAL_CONTROLS` | `"lockscreen_allow_trivial_controls"` | Whether trivial home controls can be used without authentication | `Standard` |
| `LOCKSCREEN_SHOW_CONTROLS` | `"lockscreen_show_controls"` | Whether home controls should be accessible from the lockscreen | `Standard` |
| `LOCKSCREEN_SHOW_WALLET` | `"lockscreen_show_wallet"` | Whether wallet should be accessible from the lockscreen | `Standard` |
| `LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS` | `"lock_screen_allow_private_notifications"` | Indicates whether the user has allowed notifications to be shown atop a securely locked screen in their full "private" form (same as when the device is unlocked). Type: int (0 for false, 1 for true) | `@Readable` `@SystemApi` |
| `LOCK_SCREEN_ALLOW_REMOTE_INPUT` | `"lock_screen_allow_remote_input"` | When set by a user, allows notification remote input atop a securely locked screen without having to unlock | `@Readable` |
| `LOCK_SCREEN_APPWIDGET_IDS` | `"lock_screen_appwidget_ids"` | Ids of the user-selected appwidgets on the lockscreen (comma-delimited). | `@Readable` `@Deprecated` |
| `LOCK_SCREEN_CUSTOM_CLOCK_FACE` | `"lock_screen_custom_clock_face"` | Indicates which clock face to show on lock screen and AOD formatted as a serialized {@link org.json.JSONObject} with the format: {"clock": id, "_applied_timestamp": timestamp} | `@Readable` |
| `LOCK_SCREEN_FALLBACK_APPWIDGET_ID` | `"lock_screen_fallback_appwidget_id"` | Id of the appwidget shown on the lock screen when appwidgets are disabled. | `@Readable` `@Deprecated` |
| `LOCK_SCREEN_LOCK_AFTER_TIMEOUT` | `"lock_screen_lock_after_timeout"` | This preference allows the device to be locked given time after screen goes off, subject to current DeviceAdmin policy limits. | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `LOCK_SCREEN_NOTIFICATION_MINIMALISM` | `"lock_screen_notification_minimalism"` | Indicates whether to minimalize the number of notifications to show on the lockscreen. Type: int (0 for false, 1 for true) | `Standard` |
| `LOCK_SCREEN_OWNER_INFO` | `"lock_screen_owner_info"` | This preference contains the string that shows for owner info on LockScreen. | `@Readable` `@Deprecated` |
| `LOCK_SCREEN_SHOW_NOTIFICATIONS` | `"lock_screen_show_notifications"` | Indicates whether notification display on the lock screen is enabled. Type: int (0 for false, 1 for true) | `@Readable` `@SystemApi` |
| `LOCK_SCREEN_SHOW_ONLY_UNSEEN_NOTIFICATIONS` | `"lock_screen_show_only_unseen_notifications"` | Indicates whether "seen" notifications should be suppressed from the lockscreen. Type: int (0 for unset, 1 for true, 2 for false) | `Standard` |
| `LOCK_SCREEN_SHOW_QR_CODE_SCANNER` | `"lock_screen_show_qr_code_scanner"` | Whether qr code scanner should be accessible from the lockscreen | `Standard` |
| `LOCK_SCREEN_SHOW_SILENT_NOTIFICATIONS` | `"lock_screen_show_silent_notifications"` | Indicates whether the lock screen should display silent notifications. Type: int (0 for false, 1 for true) | `@Readable` |
| `LOCK_SCREEN_STICKY_APPWIDGET` | `"lock_screen_sticky_appwidget"` | Index of the lockscreen appwidget to restore, -1 if none. | `@Readable` `@Deprecated` |
| `MIRROR_BUILT_IN_DISPLAY` | `"mirror_built_in_display"` | Whether to mirror the built-in display on all connected displays. | `Standard` |
| `NIGHT_DISPLAY_ACTIVATED` | `"night_display_activated"` | Control whether Night display is currently activated. | `@Readable` |
| `NIGHT_DISPLAY_AUTO_MODE` | `"night_display_auto_mode"` | Control whether Night display will automatically activate/deactivate. | `@Readable` |
| `NIGHT_DISPLAY_COLOR_TEMPERATURE` | `"night_display_color_temperature"` | Control the color temperature of Night Display, represented in Kelvin. | `@Readable` |
| `NIGHT_DISPLAY_CUSTOM_END_TIME` | `"night_display_custom_end_time"` | Custom time when Night display is scheduled to deactivate. Represented as milliseconds from midnight (e.g. 21600000 == 6am). | `@Readable` |
| `NIGHT_DISPLAY_CUSTOM_START_TIME` | `"night_display_custom_start_time"` | Custom time when Night display is scheduled to activate. Represented as milliseconds from midnight (e.g. 79200000 == 10pm). | `@Readable` |
| `NIGHT_DISPLAY_LAST_ACTIVATED_TIME` | `"night_display_last_activated_time"` | A String representing the LocalDateTime when Night display was last activated. Use to decide whether to apply the current activated state after a reboot or user change. In legacy cases, this is represented by the time in milliseconds (since epoch). | `@Readable` |
| `NUM_ROTATION_SUGGESTIONS_ACCEPTED` | `"num_rotation_suggestions_accepted"` | The number of accepted rotation suggestions. Used to determine if the user has been introduced to rotation suggestions. | `@Readable` |
| `REDUCE_BRIGHT_COLORS_ACTIVATED` | `"reduce_bright_colors_activated"` | Setting that specifies whether Reduce Bright Colors, or brightness dimming by color adjustment, is enabled. | `Standard` |
| `REDUCE_BRIGHT_COLORS_LEVEL` | `"reduce_bright_colors_level"` | Setting that specifies the level of Reduce Bright Colors in intensity. The range is [0, 100]. | `Standard` |
| `REDUCE_BRIGHT_COLORS_PERSIST_ACROSS_REBOOTS` | `"reduce_bright_colors_persist_across_reboots"` | Setting that specifies whether Reduce Bright Colors should persist across reboots. | `Standard` |
| `SCREENSAVER_ACTIVATE_ON_DOCK` | `"screensaver_activate_on_dock"` | If screensavers are enabled, whether the screensaver should be automatically launched when the device is inserted into a (desk) dock. | `@Readable` |
| `SCREENSAVER_ACTIVATE_ON_SLEEP` | `"screensaver_activate_on_sleep"` | If screensavers are enabled, whether the screensaver should be automatically launched when the screen times out when not on battery. | `@Readable` |
| `SCREENSAVER_COMPONENTS` | `"screensaver_components"` | The user's chosen screensaver components. These will be launched by the PhoneWindowManager after a timeout when not on battery, or upon dock insertion (if SCREENSAVER_ACTIVATE_ON_DOCK is set to 1). | `@Readable` |
| `SCREENSAVER_DEFAULT_COMPONENT` | `"screensaver_default_component"` | If screensavers are enabled, the default screensaver component. | `@Readable` |
| `SCREEN_RESOLUTION_MODE` | `"screen_resolution_mode"` | Used by DisplayManager to backup/restore the user-selected resolution mode. | `@Readable` |
| `SEARCH_MAX_RESULTS_TO_DISPLAY` | `"search_max_results_to_display"` | The maximum number of suggestions returned by GlobalSearch. | `@Readable` |
| `SHOW_ROTATION_SUGGESTIONS` | `"show_rotation_suggestions"` | Control if rotation suggestions are sent to System UI when in rotation locked mode. Done to enable screen rotation while the screen rotation is locked. Enabling will poll the accelerometer in rotation locked mode. If 0, then rotation suggestions are not sent to System UI. If 1, suggestions are sent. | `@Readable` |
| `UI_NIGHT_MODE` | `"ui_night_mode"` | The current night mode that has been selected by the user. Owned and controlled by UiModeManagerService. Constants are as per UiModeManager. | `@Readable` |
| `UI_NIGHT_MODE_CUSTOM_TYPE` | `"ui_night_mode_custom_type"` | The current night mode custom type that has been selected by the user. Owned and controlled by UiModeManagerService. Constants are as per UiModeManager. | `@Readable` |
| `UI_NIGHT_MODE_LAST_COMPUTED` | `"ui_night_mode_last_computed"` | The last computed night mode bool the last time the phone was on | `Standard` |
| `UI_NIGHT_MODE_OVERRIDE_OFF` | `"ui_night_mode_override_off"` | The current night mode that has been overridden to turn off by the system. Owned and controlled by UiModeManagerService. Constants are as per UiModeManager. | `@Readable` |
| `UI_NIGHT_MODE_OVERRIDE_ON` | `"ui_night_mode_override_on"` | The current night mode that has been overridden to turn on by the system. Owned and controlled by UiModeManagerService. Constants are as per UiModeManager. | `@Readable` |
| `VR_DISPLAY_MODE` | `"vr_display_mode"` | Behavior of the display while in VR mode. One of {@link #VR_DISPLAY_MODE_LOW_PERSISTENCE} or {@link #VR_DISPLAY_MODE_OFF}. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="general-system-settings">General & System Settings (75 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ADVANCED_PROTECTION_MODE` | `"advanced_protection_mode"` | Inetger property which determines whether advanced protection is on or not. | `Standard` |
| `ALLOW_PRIMARY_GAIA_ACCOUNT_REMOVAL_FOR_TESTS` | `"allow_primary_gaia_account_removal_for_tests"` | 1 if it is allowed to remove the primary GAIA account. 0 by default. | `Standard` |
| `AMBIENT_CONTEXT_EVENT_ARRAY_EXTRA_KEY` | `"ambient_context_event_array_key"` | Current provider of the intent extra key for the event code int array while requesting ambient context consent. Default value in @string/config_ambientContextEventArrayExtraKey. No VALIDATOR as this setting will not be backed up. | `Standard` |
| `ANDROID_ID` | `"android_id"` | On Android 8.0 (API level 26) and higher versions of the platform, a 64-bit number (expressed as a hexadecimal string), unique to each combination of app-signing key, user, and device. Values of {@code ANDROID_ID} are scoped by signing key and user. The value may change if a factory reset is performed on the device or if an APK signing key changes. For more information about how the platform handles {@code ANDROID_ID} in Android 8.0 (API level 26) and higher, see <a href="{@docRoot}about/versions/oreo/android-8.0-changes.html#privacy-all"> Android 8.0 Behavior Changes . Note: For apps that were installed prior to updating the device to a version of Android 8.0 (API level 26) or higher, the value of {@code ANDROID_ID} changes if the app is uninstalled and then reinstalled after the OTA. To preserve values across uninstalls after an OTA to Android 8.0 or higher, developers can use Key/Value Backup . In versions of the platform lower than Android 8.0 (API level 26), a 64-bit number (expressed as a hexadecimal string) that is randomly generated when the user first sets up the device and should remain constant for the lifetime of the user's device. On devices that have multiple users , each user appears as a completely separate device, so the {@code ANDROID_ID} value is unique to each user. Note: If the caller is an Instant App the ID is scoped to the Instant App, it is generated when the Instant App is first installed and reset if the user clears the Instant App. | `@Readable` |
| `ANR_SHOW_BACKGROUND` | `"anr_show_background"` | If nonzero, ANRs in invisible background processes bring up a dialog. Otherwise, the process will be silently killed. Also prevents ANRs and crash dialogs from being suppressed. | `@Readable` `@TestApi` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `AUTOFILL_FEATURE_FIELD_CLASSIFICATION` | `"autofill_field_classification"` | Boolean indicating if Autofill supports field classification. | `@Readable` `@SystemApi` |
| `AUTOFILL_SERVICE` | `"autofill_service"` | The currently selected autofill service flattened ComponentName. | `@Readable` `@TestApi` |
| `AUTOFILL_SERVICE_SEARCH_URI` | `"autofill_service_search_uri"` | This is the query URI for finding a auto fill service to install. | `@Readable` |
| `CAMERA_AUTOROTATE` | `"camera_autorotate"` | Setting key to indicate whether camera-based autorotate is enabled. | `Standard` |
| `CAMERA_EXTENSIONS_FALLBACK` | `"camera_extensions_fallback"` | Whether to enable camera extensions software fallback. | `@Readable` |
| `CMAS_ADDITIONAL_BROADCAST_PKG` | `"cmas_additional_broadcast_pkg"` | Specifies additional package name for broadcasting the CMAS messages. | `@Readable` |
| `COMPAT_UI_EDUCATION_SHOWING` | `"compat_ui_education_showing"` | Whether any Compat UI Education is currently showing. 1 if true, 0 or unset otherwise. This setting is used to inform other components that the Compat UI Education is currently showing, which can prevent them from showing something else to the user. | `Standard` |
| `COMPLETED_CATEGORY_PREFIX` | `"suggested.completed_category."` | The prefix for a category name that indicates whether a suggested action from that category was marked as completed. Type: int (0 for false, 1 for true) | `@Readable` `@SystemApi` |
| `CONTRAST_LEVEL` | `"contrast_level"` | The color contrast, float in [-1, 1], 1 being the highest contrast. | `Standard` |
| `DEVICE_PAIRED` | `"device_paired"` | Has this pairable device been paired or upgraded from a previously paired system. | `@Readable` |
| `DND_CONFIGS_MIGRATED` | `"dnd_settings_migrated"` | If 1, DND default allowed packages have been updated | `Standard` |
| `DOCK_SETUP_STATE` | `"dock_setup_state"` | Defines the user's current state of dock setup. The possible states are defined in {@link DockSetupState}. | `Standard` |
| `DOZE_ALWAYS_ON` | `"doze_always_on"` | Indicates whether doze should be always on. Type: int (0 for false, 1 for true) | `@Readable` `@SystemApi` |
| `DOZE_PULSE_ON_LONG_PRESS` | `"doze_pulse_on_long_press"` | Whether the device should pulse on long press gesture. | `@Readable` |
| `GAME_DASHBOARD_ALWAYS_ON` | `"game_dashboard_always_on"` | Control whether Game Dashboard shortcut is always on for all games. | `@Readable` |
| `HBM_SETTING_KEY` | `"com.android.server.display.HBM_SETTING_KEY"` | Controls the 'Sunlight boost' toggle in wearable devices (high brightness mode). Valid values for this key are: '0' (disabled) or '1' (enabled). | `Standard` |
| `HDMI_CEC_SET_MENU_LANGUAGE_DENYLIST` | `"hdmi_cec_set_menu_language_denylist"` | Setting to store denylisted system languages by the CEC {@code } confirmation dialog. | `Standard` |
| `HIDE_PRIVATESPACE_ENTRY_POINT` | `"hide_privatespace_entry_point"` | Controls whether to hide private space entry point in All Apps | `Standard` |
| `HUB_MODE_TUTORIAL_STATE` | `"hub_mode_tutorial_state"` | Defines the user's current state of navigating through the hub mode tutorial. Some possible states are defined in {@link HubModeTutorialState}. Any value greater than or equal to {@link HUB_MODE_TUTORIAL_COMPLETED} indicates that the user has completed that version of the hub mode tutorial. And tutorial may be shown again when a new version becomes available. | `Standard` |
| `IMMERSIVE_MODE_CONFIRMATIONS` | `"immersive_mode_confirmations"` | No explicit description provided in source comments. | `@Readable` `@TestApi` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `INCALL_BACK_BUTTON_BEHAVIOR` | `"incall_back_button_behavior"` | What happens when the user presses the Back button while in-call and the screen is on. Values: 0 - The Back buttons does nothing different. 1 - The Back button hangs up the current call. | `@Readable` |
| `KEY_REPEAT_DELAY_MS` | `"key_repeat_delay"` | The duration between successive key repeats in milliseconds. | `@Readable` |
| `LAST_SETUP_SHOWN` | `"last_setup_shown"` | Indicates the version for which the setup wizard was last shown. The version gets bumped for each release when there is new setup information to show. | `@Readable` `@SystemApi` |
| `LAUNCHER_TASKBAR_EDUCATION_SHOWING` | `"launcher_taskbar_education_showing"` | Whether the Taskbar Education is about to be shown or is currently showing. 1 if true, 0 or unset otherwise. This setting is used to inform other components that the Taskbar Education is currently showing, which can prevent them from showing something else to the user. | `Standard` |
| `MANAGED_PROVISIONING_DPC_DOWNLOADED` | `"managed_provisioning_dpc_downloaded"` | Indicates whether a DPC has been downloaded during provisioning. Type: int (0 for false, 1 for true) If this is true, then any attempts to begin setup again should result in factory reset | `@Readable` |
| `MATCH_CONTENT_FRAME_RATE` | `"match_content_frame_rate"` | User's preference for refresh rate switching. Values: 0 - Never switch refresh rates. 1 - Switch refresh rates only when it can be done seamlessly. (Default behaviour) 2 - Always prefer refresh rate switching even if it's going to have visual interruptions for the user. | `Standard` |
| `MINIMAL_POST_PROCESSING_ALLOWED` | `"minimal_post_processing_allowed"` | Whether the user allows minimal post processing or not. Values: 0 - Not allowed. Any preferences set through the Window.setPreferMinimalPostProcessing API will be ignored. 1 - Allowed. Any preferences set through the Window.setPreferMinimalPostProcessing API will be respected and the appropriate signals will be sent to display. (Default behaviour) | `@Readable` |
| `MOUNT_UMS_AUTOSTART` | `"mount_ums_autostart"` | Whether or not UMS auto-starts on UMS host detection. (0 = false, 1 = true) | `@Readable` |
| `MOUNT_UMS_PROMPT` | `"mount_ums_prompt"` | Whether or not a notification is displayed on UMS host detection. (0 = false, 1 = true) | `@Readable` |
| `NAS_SETTINGS_UPDATED` | `"nas_settings_updated"` | If nonzero, nas has not been updated to reflect new changes. | `@Readable` |
| `NAVIGATION_MODE` | `"navigation_mode"` | Navigation bar mode. 0 = 3 button 1 = 2 button 2 = fully gestural | `@Readable` |
| `NAVIGATION_MODE_RESTORE` | `"navigation_mode_restore"` | The value is from another(source) device's {@link #NAVIGATION_MODE} during restore. It's supposed to be written only by {@link com.android.providers.settings.SettingsHelper}. This setting should not be added into backup array. Value: -1 = Can't get value from restore(default), 0 = 3 button, 1 = 2 button, 2 = fully gestural. | `Standard` |
| `NAV_BAR_KIDS_MODE` | `"nav_bar_kids_mode"` | Indicates whether the device is in kids nav mode. Type: int (0 for false, 1 for true) | `Standard` |
| `NEARBY_SHARING_SLICE_URI` | `"nearby_sharing_slice_uri"` | Nearby Sharing Slice URI for the SliceProvider to read Nearby Sharing scan results and then draw the UI. | `Standard` |
| `ONE_HANDED_MODE_ACTIVATED` | `"one_handed_mode_activated"` | Controls whether One-Handed mode is currently activated. | `Standard` |
| `ONE_HANDED_TUTORIAL_SHOW_COUNT` | `"one_handed_tutorial_show_count"` | Internal use, one handed mode tutorial showed times. | `Standard` |
| `PARENTAL_CONTROL_LAST_UPDATE` | `"parental_control_last_update"` | No longer supported. | `@Readable` |
| `PARENTAL_CONTROL_REDIRECT_URL` | `"parental_control_redirect_url"` | No longer supported. | `@Readable` |
| `PAYMENT_SERVICE_SEARCH_URI` | `"payment_service_search_uri"` | This is the query URI for finding a NFC payment service to install. | `@Readable` |
| `PEOPLE_STRIP` | `"people_strip"` | Controls whether the people strip is enabled. | `@Readable` |
| `PREFERRED_TTY_MODE` | `"preferred_tty_mode"` | The preferred TTY mode 0 = TTy Off, CDMA default 1 = TTY Full 2 = TTY HCO 3 = TTY VCO | `@Readable` |
| `PRINT_SERVICE_SEARCH_URI` | `"print_service_search_uri"` | This is the query URI for finding a print service to install. | `@Readable` |
| `QS_AUTO_ADDED_TILES` | `"qs_auto_tiles"` | Comma separated list of QS tiles that have been auto-added already. | `@Readable` |
| `QS_TILES` | `"sysui_qs_tiles"` | Holds comma separated list of ordering of QS tiles. | `@Readable(maxTargetSdk = VERSION_CODES.TIRAMISU)` |
| `RTT_CALLING_MODE` | `"rtt_calling_mode"` | User-selected RTT mode. When on, outgoing and incoming calls will be answered as RTT calls when supported by the device and carrier. Boolean value. 0 = OFF 1 = ON | `@Readable` |
| `SEARCH_GLOBAL_SEARCH_ACTIVITY` | `"search_global_search_activity"` | The global search provider chosen by the user (if multiple global search providers are installed). This will be the provider returned by {@link SearchManager#getGlobalSearchActivity()} if it's still installed. This setting is stored as a flattened component name as per {@link ComponentName#flattenToString()}. | `@Readable` |
| `SEARCH_MAX_RESULTS_PER_SOURCE` | `"search_max_results_per_source"` | The number of suggestions GlobalSearch will ask each non-web search source for. | `@Readable` |
| `SEARCH_MAX_SHORTCUTS_RETURNED` | `"search_max_shortcuts_returned"` | The maximum number of shortcuts shown by GlobalSearch. | `@Readable` |
| `SEARCH_MAX_SOURCE_EVENT_AGE_MILLIS` | `"search_max_source_event_age_millis"` | The maximum age of log data used for source ranking in GlobalSearch. | `@Readable` |
| `SEARCH_MAX_STAT_AGE_MILLIS` | `"search_max_stat_age_millis"` | The maximum age of log data used for shortcuts in GlobalSearch. | `@Readable` |
| `SEARCH_MIN_CLICKS_FOR_SOURCE_RANKING` | `"search_min_clicks_for_source_ranking"` | The minimum number of clicks needed to rank a source in GlobalSearch. | `@Readable` |
| `SEARCH_MIN_IMPRESSIONS_FOR_SOURCE_RANKING` | `"search_min_impressions_for_source_ranking"` | The minimum number of impressions needed to rank a source in GlobalSearch. | `@Readable` |
| `SEARCH_NUM_PROMOTED_SOURCES` | `"search_num_promoted_sources"` | The number of promoted sources in GlobalSearch. | `@Readable` |
| `SEARCH_PER_SOURCE_CONCURRENT_QUERY_LIMIT` | `"search_per_source_concurrent_query_limit"` | The maximum number of concurrent suggestion queries to each source. | `@Readable` |
| `SEARCH_PREFILL_MILLIS` | `"search_prefill_millis"` | The maximum number of milliseconds that GlobalSearch shows the previous results after receiving a new query. | `@Readable` |
| `SEARCH_PROMOTED_SOURCE_DEADLINE_MILLIS` | `"search_promoted_source_deadline_millis"` | The number of milliseconds that GlobalSearch will wait for suggestions from promoted sources before continuing with all other sources. | `@Readable` |
| `SEARCH_QUERY_THREAD_CORE_POOL_SIZE` | `"search_query_thread_core_pool_size"` | The size of the core thread pool for suggestion queries in GlobalSearch. | `@Readable` |
| `SEARCH_QUERY_THREAD_MAX_POOL_SIZE` | `"search_query_thread_max_pool_size"` | The maximum size of the thread pool for suggestion queries in GlobalSearch. | `@Readable` |
| `SEARCH_SHORTCUT_REFRESH_CORE_POOL_SIZE` | `"search_shortcut_refresh_core_pool_size"` | The size of the core thread pool for shortcut refreshing in GlobalSearch. | `@Readable` |
| `SEARCH_SHORTCUT_REFRESH_MAX_POOL_SIZE` | `"search_shortcut_refresh_max_pool_size"` | The maximum size of the thread pool for shortcut refreshing in GlobalSearch. | `@Readable` |
| `SEARCH_THREAD_KEEPALIVE_SECONDS` | `"search_thread_keepalive_seconds"` | The maximun time that excess threads in the GlobalSeach thread pools will wait before terminating. | `@Readable` |
| `SEARCH_WEB_RESULTS_OVERRIDE_LIMIT` | `"search_web_results_override_limit"` | The number of suggestions the GlobalSearch will ask the web search source for. | `@Readable` |
| `SETTINGS_CLASSNAME` | `"settings_classname"` | Settings classname to launch when Settings is clicked from All Applications. Needed because of user testing between the old and new Settings apps. | `@Readable` |
| `SHOW_QR_CODE_SCANNER_SETTING` | `"show_qr_code_scanner_setting"` | Whether or not to enable qr code code scanner setting to enable/disable lockscreen entry point. Any value apart from null means setting needs to be enabled | `Standard` |
| `SKIP_DIRECTION` | `"skip_gesture_direction"` | Direction to advance media for skip gesture | `@Readable` |
| `SKIP_FIRST_USE_HINTS` | `"skip_first_use_hints"` | If enabled, apps should try to skip any introductory hints on first launch. This might apply to users that are already familiar with the environment or temporary users. Type : int (0 to show hints, 1 to skip showing hints) | `@Readable` |
| `SUPPRESS_DOZE` | `"suppress_doze"` | Whether the device should suppress the current doze configuration and disable dozing. | `@Readable` |
| `UNKNOWN_SOURCES_DEFAULT_REVERSED` | `"unknown_sources_default_reversed"` | A flag to tell {@link com.android.server.devicepolicy.DevicePolicyManagerService} that the default for {@link #INSTALL_NON_MARKET_APPS} is reversed for this user on OTA. So it can set the restriction {@link android.os.UserManager#DISALLOW_INSTALL_UNKNOWN_SOURCES} on behalf of the profile owner if needed to make the change transparent for profile owners. | `@Readable` |
| `V_TO_U_RESTORE_ALLOWLIST` | `"v_to_u_restore_allowlist"` | List of system components that support restore in a V-> U OS downgrade but do not have RestoreAnyVersion set to true. Value set before system restore. This setting is not B&Rd List is stored as a comma-separated string of package names e.g. "a,b,c" | `Standard` |
| `V_TO_U_RESTORE_DENYLIST` | `"v_to_u_restore_denylist"` | List of system components that have RestoreAnyVersion set to true but do not support restore in a V-> U OS downgrade. Value set before system restore. This setting is not B&Rd List is stored as a comma-separated string of package names e.g. "a,b,c" | `Standard` |

</details>

<details open>
<summary><h3 style="display:inline;" id="input-methods-voice-gestures">Input Methods, Voice & Gestures (49 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ASSISTANT` | `"assistant"` | The current assistant component. It could be a voice interaction service, or an activity that handles ACTION_ASSIST, or empty which means using the default handling. This should be set indirectly by setting the {@link android.app.role.RoleManager#ROLE_ASSISTANT assistant role}. | `@Readable` `@UnsupportedAppUsage` |
| `ASSIST_GESTURE_SENSITIVITY` | `"assist_gesture_sensitivity"` | Sensitivity control for the assist gesture. | `@Readable` |
| `ASSIST_GESTURE_SETUP_COMPLETE` | `"assist_gesture_setup_complete"` | Indicates whether the Assist Gesture Deferred Setup has been completed. Type: int (0 for false, 1 for true) | `@Readable` `@SystemApi` |
| `ASSIST_HANDLES_LEARNING_EVENT_COUNT` | `"reminder_exp_learning_event_count"` | How many times the Assistant has been triggered using the touch gesture. | `Standard` |
| `ASSIST_HANDLES_LEARNING_TIME_ELAPSED_MILLIS` | `"reminder_exp_learning_time_elapsed"` | How long Assistant handles have enabled in milliseconds. | `Standard` |
| `ATTENTIVE_TIMEOUT` | `"attentive_timeout"` | The timeout in milliseconds before the device goes to sleep due to user inattentiveness, even if the system is holding wakelocks. It should generally be longer than {@code config_attentiveWarningDuration}, as otherwise the device will show the attentive warning constantly. Small timeouts are discouraged, as they will cause the device to go to sleep quickly after waking up. Use -1 to disable this timeout. | `@Readable` |
| `AWARE_TAP_PAUSE_GESTURE_COUNT` | `"aware_tap_pause_gesture_count"` | Number of successful "Motion Sense" tap gestures to pause media. | `@Readable` |
| `AWARE_TAP_PAUSE_TOUCH_COUNT` | `"aware_tap_pause_touch_count"` | Number of touch interactions to pause media when a "Motion Sense" gesture could have been used. | `@Readable` |
| `BACK_GESTURE_INSET_SCALE_LEFT` | `"back_gesture_inset_scale_left"` | Scale factor for the back gesture inset size on the left side of the screen. | `@Readable` |
| `BACK_GESTURE_INSET_SCALE_RIGHT` | `"back_gesture_inset_scale_right"` | Scale factor for the back gesture inset size on the right side of the screen. | `@Readable` |
| `DEFAULT_DEVICE_INPUT_METHOD` | `"default_device_input_method"` | Used only by {@link com.android.server.inputmethod.InputMethodManagerService} as a temporary data store of {@link #DEFAULT_INPUT_METHOD} while a virtual-device-specific input method is set as default. This should be considered to be an implementation detail of {@link com.android.server.inputmethod.InputMethodManagerService}. Other system components should never rely on this value. | `Standard` |
| `DEFAULT_INPUT_METHOD` | `"default_input_method"` | Stores {@link android.view.inputmethod.InputMethodInfo#getId()} of the input method service that is currently selected. Although the name {@link #DEFAULT_INPUT_METHOD} implies that there is a concept of default input method, in reality this setting is no more or less than the currently selected input method. This setting can be updated at any time as a result of user-initiated and system-initiated input method switching. Use {@link ComponentName#unflattenFromString(String)} to parse the stored value. | `@Readable` |
| `DEFAULT_VOICE_INPUT_METHOD` | `"default_voice_input_method"` | The {@link android.view.inputmethod.InputMethodInfo.InputMethodInfo#getId() ID} of the default voice input method. This stores the last known default voice IME. If the related system config value changes, this is reset by InputMethodManagerService. This IME is not necessarily in the enabled IME list. That state is still stored in {@link #ENABLED_INPUT_METHODS}. | `Standard` |
| `DOZE_PICK_UP_GESTURE` | `"doze_pulse_on_pick_up"` | Whether the device should pulse on pick up gesture. | `@Readable` |
| `DOZE_QUICK_PICKUP_GESTURE` | `"doze_quick_pickup_gesture"` | Gesture that wakes up the display on quick pickup, toggling between {@link Display.STATE_OFF} and {@link Display.STATE_DOZE}. | `Standard` |
| `EMERGENCY_ASSISTANCE_APPLICATION` | `"emergency_assistance_application"` | Specifies the package name currently configured to be the emergency assistance application | `@Readable` |
| `EMERGENCY_GESTURE_UI_LAST_STARTED_MILLIS` | `"emergency_gesture_ui_last_started_millis"` | The last time the emergency gesture UI was started. | `Standard` |
| `EMERGENCY_GESTURE_UI_SHOWING` | `"emergency_gesture_ui_showing"` | Whether the emergency gesture UI is currently showing. | `Standard` |
| `HUSH_GESTURE_USED` | `"hush_gesture_used"` | Whether the hush gesture has ever been used | `@Readable` `@SystemApi` |
| `INPUT_METHODS_SUBTYPE_HISTORY` | `"input_methods_subtype_history"` | Setting to record the history of input method subtype, holding the pair of ID of IME and its last used subtype. | `@Readable` |
| `INPUT_METHOD_SELECTOR_VISIBILITY` | `"input_method_selector_visibility"` | Setting to record the visibility of input method selector | `@Readable` |
| `KEY_REPEAT_TIMEOUT_MS` | `"key_repeat_timeout"` | The duration before a key repeat begins in milliseconds. | `@Readable` |
| `LONG_PRESS_TIMEOUT` | `"long_press_timeout"` | The timeout for considering a press to be a long press in milliseconds. | `@Readable` `@UnsupportedAppUsage` |
| `MULTI_PRESS_TIMEOUT` | `"multi_press_timeout"` | The duration in milliseconds between the first tap's up event and the second tap's down event for an interaction to be considered part of the same multi-press. | `@Readable` |
| `ONE_HANDED_MODE_TIMEOUT` | `"one_handed_mode_timeout"` | For user preference if One-Handed Mode timeout. | `Standard` |
| `ON_DEVICE_INFERENCE_UNBIND_TIMEOUT_MS` | `"on_device_inference_unbind_timeout_ms"` | Timeout to be used for unbinding to the configured remote {@link android.service.ondeviceintelligence.OnDeviceSandboxedInferenceService} if there are no requests in the queue. A value of -1 represents to never unbind. | `Standard` |
| `ON_DEVICE_INTELLIGENCE_UNBIND_TIMEOUT_MS` | `"on_device_intelligence_unbind_timeout_ms"` | Timeout to be used for unbinding to the configured remote {@link android.service.ondeviceintelligence.OnDeviceIntelligenceService} if there are no requests in the queue. A value of -1 represents to never unbind. | `Standard` |
| `SEARCH_SOURCE_TIMEOUT_MILLIS` | `"search_source_timeout_millis"` | The number of milliseconds before GlobalSearch aborts search suggesiton queries. | `@Readable` |
| `SELECTED_INPUT_METHOD_SUBTYPE` | `"selected_input_method_subtype"` | Setting to record the input method subtype used by default, holding the ID of the desired method. | `@Readable` |
| `SELECTED_SPELL_CHECKER` | `"selected_spell_checker"` | The {@link ComponentName} string of the selected spell checker service which is one of the services managed by the text service manager. | `@Readable` `@TestApi` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `SELECTED_SPELL_CHECKER_SUBTYPE` | `"selected_spell_checker_subtype"` | {@link android.view.textservice.SpellCheckerSubtype#hashCode()} of the selected subtype of the selected spell checker service which is one of the services managed by the text service manager. | `@Readable` `@TestApi` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `SHOW_IME_WITH_HARD_KEYBOARD` | `"show_ime_with_hard_keyboard"` | Whether to show the IME when a hard keyboard is connected. This is a boolean that determines if the IME should be shown when a hard keyboard is attached. | `@Readable` `@TestApi` |
| `SILENCE_ALARMS_GESTURE_COUNT` | `"silence_alarms_gesture_count"` | Count of successful silence alarms gestures. | `@Readable` |
| `SILENCE_ALARMS_TOUCH_COUNT` | `"silence_alarms_touch_count"` | Count of non-gesture interaction. | `@Readable` |
| `SILENCE_CALL_GESTURE_COUNT` | `"silence_call_gesture_count"` | Count of successful silence call gestures. | `@Readable` |
| `SILENCE_CALL_TOUCH_COUNT` | `"silence_call_touch_count"` | Count of non-gesture interaction. | `@Readable` |
| `SILENCE_GESTURE` | `"silence_gesture"` | Gesture that silences sound (alarms, notification, calls). | `@Readable` |
| `SILENCE_TIMER_GESTURE_COUNT` | `"silence_timer_gesture_count"` | Count of successful silence timer gestures. | `@Readable` |
| `SILENCE_TIMER_TOUCH_COUNT` | `"silence_timer_touch_count"` | Count of non-gesture interaction. | `@Readable` |
| `SKIP_GESTURE` | `"skip_gesture"` | Gesture that skips media. | `@Readable` |
| `SKIP_GESTURE_COUNT` | `"skip_gesture_count"` | Count of successful gestures. | `@Readable` |
| `SKIP_TOUCH_COUNT` | `"skip_touch_count"` | Count of non-gesture interaction. | `@Readable` |
| `TAP_GESTURE` | `"tap_gesture"` | Controls whether tap gesture is enabled. | `@Readable` |
| `TIMEOUT_TO_DOCK_USER` | `"timeout_to_dock_user"` | The duration of timeout, in milliseconds, to switch from a non-Dock User to the Dock User when the device is docked. | `Standard` |
| `TV_APP_USES_NON_SYSTEM_INPUTS` | `"tv_app_uses_non_system_inputs"` | Whether TV app uses non-system inputs. The value is boolean (1 or 0), where 1 means non-system TV inputs are allowed, and 0 means non-system TV inputs are not allowed. Devices such as sound bars may have changed the system property allow_third_party_inputs to false so the TV Application only uses HDMI and other built in inputs. This setting allows user to override the default and have the TV Application use third party TV inputs available on play store. | `@Readable` |
| `TV_INPUT_CUSTOM_LABELS` | `"tv_input_custom_labels"` | List of custom TV input labels. This is a string containing pairs. TV input id and custom name are encoded by {@link android.net.Uri#encode(String)} and separated by ','. Each pair is separated by ':'. | `@Readable` |
| `TV_INPUT_HIDDEN_INPUTS` | `"tv_input_hidden_inputs"` | List of TV inputs that are currently hidden. This is a string containing the IDs of all hidden TV inputs. Each ID is encoded by {@link android.net.Uri#encode(String)} and separated by ':'. | `@Readable` |
| `VOICE_INTERACTION_SERVICE` | `"voice_interaction_service"` | The currently selected voice interaction service flattened ComponentName. | `@Readable` `@TestApi` |
| `VOICE_RECOGNITION_SERVICE` | `"voice_recognition_service"` | The {@link ComponentName} string of the service to be used as the voice recognition service. | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |

</details>

<details open>
<summary><h3 style="display:inline;" id="location-positioning">Location & Positioning (10 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ALLOWED_GEOLOCATION_ORIGINS` | `"allowed_geolocation_origins"` | Origins for which browsers should allow geolocation by default. The value is a space-separated list of origins. | `@Readable` |
| `ALLOW_MOCK_LOCATION` | `"mock_location"` | Setting to allow mock locations and location provider status to be injected into the LocationManager service for testing purposes during application development. These locations and status values override actual location and status information generated by network, gps, or other location providers. | `@Readable` `@Deprecated` |
| `LOCATION_ACCESS_CHECK_DELAY_MILLIS` | `"location_access_check_delay_millis"` | Delay between granting location access and checking it. {@link LocationAccessCheck#PROPERTY_LOCATION_ACCESS_CHECK_DELAY_MILLIS} in a T module update Before Android T set this property to control the delay for the check On Android T set this and the DeviceConfig property After Android T set the DeviceConfig property | `@Readable` `@Deprecated` `@SystemApi` |
| `LOCATION_ACCESS_CHECK_INTERVAL_MILLIS` | `"location_access_check_interval_millis"` | How often to check for location access. {@link LocationAccessCheck#PROPERTY_LOCATION_ACCESS_PERIODIC_INTERVAL_MILLIS} in a T module update Before Android T set this property to control the interval for the check On Android T set this and the DeviceConfig property After Android T set the DeviceConfig property | `@Readable` `@Deprecated` `@SystemApi` |
| `LOCATION_CHANGER` | `"location_changer"` | The App or module that changes the location mode. | `@Readable` |
| `LOCATION_COARSE_ACCURACY_M` | `"locationCoarseAccuracy"` | The accuracy in meters used for coarsening location for clients with only the coarse location permission. | `@Readable` |
| `LOCATION_MODE` | `"location_mode"` | The current location mode of the device. Do not rely on this value being present or on ContentObserver notifications on the corresponding Uri. are via {@link LocationManager#isLocationEnabled()} and {@link LocationManager#MODE_CHANGED_ACTION}. | `@Readable` `@Deprecated` |
| `LOCATION_PERMISSIONS_UPGRADE_TO_Q_MODE` | `"location_permissions_upgrade_to_q_mode"` | No explicit description provided in source comments. | `@Readable` `@Deprecated` `@SystemApi` |
| `LOCATION_PROVIDERS_ALLOWED` | `"location_providers_allowed"` | Comma-separated list of location providers that are enabled. Do not rely on this value being present or correct, or on ContentObserver notifications on the corresponding Uri. capable of realistically reflecting location settings. Use {@link LocationManager#isProviderEnabled(String)} or {@link LocationManager#isLocationEnabled()} instead. | `@Readable` `@Deprecated` |
| `LOCATION_SHOW_SYSTEM_OPS` | `"locationShowSystemOps"` | Whether or not to show display system location accesses. | `Standard` |

</details>

<details open>
<summary><h3 style="display:inline;" id="multi-user-accounts-profiles">Multi-User, Accounts & Profiles (6 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `CUSTOM_BUGREPORT_HANDLER_USER` | `"custom_bugreport_handler_user"` | The user id for the custom bugreport handler app. This is currently used only by Power Menu short press. | `Standard` |
| `DEFAULT_NOTE_TASK_PROFILE` | `"default_note_task_profile"` | Preferred default user profile to use with the notes task button shortcut. | `Standard` |
| `MANAGED_PROFILE_CONTACT_REMOTE_SEARCH` | `"managed_profile_contact_remote_search"` | Whether parent user can access remote contact in managed profile. | `@Readable` |
| `TV_USER_SETUP_COMPLETE` | `"tv_user_setup_complete"` | Whether the current user has been set up via setup wizard (0 = false, 1 = true) This value differs from USER_SETUP_COMPLETE in that it can be reset back to 0 in case SetupWizard has been re-enabled on TV devices. | `@Readable` |
| `USER_SETUP_COMPLETE` | `"user_setup_complete"` | Indicates whether the current user has completed setup via the setup wizard. Type: int (0 for false, 1 for true) | `@Readable` `@SystemApi` |
| `USER_SETUP_PERSONALIZATION_STATE` | `"user_setup_personalization_state"` | Defines the user's current state of device personalization. The possible states are defined in {@link UserSetupPersonalization}. | `@Readable` `@SystemApi` |

</details>

<details open>
<summary><h3 style="display:inline;" id="notifications-do-not-disturb-(zen)">Notifications & Do Not Disturb (Zen) (7 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `CLIPBOARD_SHOW_ACCESS_NOTIFICATIONS` | `"clipboard_show_access_notifications"` | Whether to show clipboard access notifications. | `Standard` |
| `MOUNT_PLAY_NOTIFICATION_SND` | `"mount_play_not_snd"` | Whether or not alert sounds are played on StorageManagerService events. (0 = false, 1 = true) | `@Readable` |
| `NOTIFICATION_BADGING` | `"notification_badging"` | Whether the launcher should show any notification badges. The value is boolean (1 or 0). | `@Readable` `@TestApi` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `NOTIFICATION_DISMISS_RTL` | `"notification_dismiss_rtl"` | Whether notifications are dismissed by a right-to-left swipe (instead of a left-to-right swipe). | `@Readable` |
| `SHOW_NOTE_ABOUT_NOTIFICATION_HIDING` | `"show_note_about_notification_hiding"` | Set by the system to track if the user needs to see the call to action for the lockscreen notification policy. | `@Readable` |
| `SHOW_NOTIFICATION_SNOOZE` | `"show_notification_snooze"` | Indicates whether snooze options should be shown on notifications Type: int (0 for false, 1 for true) | `@Readable` |
| `ZEN_DURATION` | `"zen_duration"` | If 0, turning on dnd manually will last indefinitely. Else if non-negative, turning on dnd manually will last for this many minutes. Else (if negative), turning on dnd manually will surface a dialog that prompts user to specify a duration. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="power-battery-thermal">Power, Battery & Thermal (12 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ADAPTIVE_SLEEP` | `"adaptive_sleep"` | Control whether to enable adaptive sleep mode. | `@Readable` |
| `BUGREPORT_IN_POWER_MENU` | `"bugreport_in_power_menu"` | When the user has enable the option to have a "bug report" command in the power menu. | `@Readable` |
| `CHARGE_OPTIMIZATION_MODE` | `"charge_optimization_mode"` | Integer property that determines which charging optimization mode is applied. [0-10] inclusive representing different modes, where 0 is the default indicating no optimization mode is applied. | `Standard` |
| `EXTRA_AUTOMATIC_POWER_SAVE_MODE` | `"extra_automatic_power_save_mode"` | Whether battery saver is currently set to different schedule mode. | `Standard` |
| `EXTRA_LOW_POWER_WARNING_ACKNOWLEDGED` | `"extra_low_power_warning_acknowledged"` | Whether the "first time extra battery saver warning" dialog needs to be shown (0: default) or not (1). | `Standard` |
| `INCALL_POWER_BUTTON_BEHAVIOR` | `"incall_power_button_behavior"` | What happens when the user presses the Power button while in-call and the screen is on. Values: 1 - The Power button turns off the screen and locks the device. (Default behavior) 2 - The Power button hangs up the current call. | `@Readable` `@UnsupportedAppUsage` |
| `LOW_POWER_MANUAL_ACTIVATION_COUNT` | `"low_power_manual_activation_count"` | The number of times (integer) the user has manually enabled battery saver. | `@Readable` |
| `LOW_POWER_WARNING_ACKNOWLEDGED` | `"low_power_warning_acknowledged"` | Whether the "first time battery saver warning" dialog needs to be shown (0: default) or not (1). | `@Readable` |
| `ON_DEVICE_INTELLIGENCE_IDLE_TIMEOUT_MS` | `"on_device_intelligence_idle_timeout_ms"` | Timeout that represents maximum idle time before which a callback should be populated. | `Standard` |
| `POWER_MENU_LOCKED_SHOW_CONTENT` | `"power_menu_locked_show_content"` | Whether power menu content (cards, passes, controls) will be shown when device is locked. 0 indicates hide and 1 indicates show. A non existent value will be treated as hide. | `@Readable` `@TestApi` |
| `SLEEP_TIMEOUT` | `"sleep_timeout"` | The timeout in milliseconds before the device fully goes to sleep after a period of inactivity. This value sets an upper bound on how long the device will stay awake or dreaming without user activity. It should generally be longer than {@link Settings.System#SCREEN_OFF_TIMEOUT} as otherwise the device will sleep before it ever has a chance to dream. Use -1 to disable this timeout. | `@Readable` |
| `SUPPRESS_AUTO_BATTERY_SAVER_SUGGESTION` | `"suppress_auto_battery_saver_suggestion"` | 0 (default) Auto battery saver suggestion has not been suppressed. 1) it has been suppressed. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="security-privacy-lockscreen">Security, Privacy & Lockscreen (30 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACTIVE_UNLOCK_ON_BIOMETRIC_FAIL` | `"active_unlock_on_biometric_fail"` | Whether or not active unlock triggers on biometric failure. | `Standard` |
| `ACTIVE_UNLOCK_ON_FACE_ACQUIRE_INFO` | `"active_unlock_on_face_acquire_info"` | If active unlock triggers on biometric failures, include the following acquired info as a "biometric failure". See {@link android.hardware.biometrics.BiometricFaceConstants}. Acquired codes should be separated by a pipe. For example: "1\|4\|5". If active unlock should never on trigger on any acquired info messages, this should be set to an empty string. A null value will use the system default value (none). | `Standard` |
| `ACTIVE_UNLOCK_ON_FACE_ERRORS` | `"active_unlock_on_face_errors"` | If active unlock triggers on biometric failures, include the following error codes as a biometric failure. See {@link android.hardware.biometrics.BiometricFaceConstants}. Error codes should be separated by a pipe. For example: "1\|4\|5". If active unlock should never trigger on any face errors, this should be set to an empty string. A null value will use the system default value (TIMEOUT). | `Standard` |
| `ACTIVE_UNLOCK_ON_UNLOCK_INTENT` | `"active_unlock_on_unlock_intent"` | Whether or not active unlock triggers on unlock intent. | `Standard` |
| `ACTIVE_UNLOCK_ON_UNLOCK_INTENT_LEGACY` | `"active_unlock_on_unlock_intent_legacy"` | Whether or not active unlock triggers on legacy unlock intents. | `Standard` |
| `ACTIVE_UNLOCK_ON_UNLOCK_INTENT_WHEN_BIOMETRIC_ENROLLED` | `"active_unlock_on_unlock_intent_when_biometric_enrolled"` | If active unlock triggers on biometric failures, then also request active unlock on unlock intent when each setting (BiometricType) is the only biometric type enrolled. Biometric types should be separated by a pipe. For example: "0\|3" or "0". If this setting should be disabled, then this should be set to an empty string. A null value will use the system default value (0 / None). 0 = None, 1 = Any face, 2 = Any fingerprint, 3 = Under display fingerprint | `Standard` |
| `ACTIVE_UNLOCK_ON_WAKE` | `"active_unlock_on_wake"` | Whether or not active unlock triggers on wake. | `Standard` |
| `ACTIVE_UNLOCK_WAKEUPS_CONSIDERED_UNLOCK_INTENTS` | `"active_unlock_wakeups_considered_unlock_intents"` | If active unlock triggers on unlock intents, then also request active unlock on these wake-up reasons. See {@link PowerManager.WakeReason} for value mappings. WakeReasons should be separated by a pipe. For example: "0\|3" or "0". If this setting should be disabled, then this should be set to an empty string. A null value will use the system default value (WAKE_REASON_UNFOLD_DEVICE). | `Standard` |
| `ACTIVE_UNLOCK_WAKEUPS_TO_FORCE_DISMISS_KEYGUARD` | `"active_unlock_wakeups_to_force_dismiss_keyguard"` | If active unlock triggers and succeeds on these wakeups, force dismiss keyguard on these wake reasons. See {@link PowerManager#WakeReason} for value mappings. WakeReasons should be separated by a pipe. For example: "0\|3" or "0". If this setting should be disabled, then this should be set to an empty string. A null value will use the system default value (WAKE_REASON_UNFOLD_DEVICE). | `Standard` |
| `ALWAYS_ON_VPN_LOCKDOWN` | `"always_on_vpn_lockdown"` | Whether to block networking outside of VPN connections while always-on is set. | `@Readable` |
| `ALWAYS_ON_VPN_LOCKDOWN_WHITELIST` | `"always_on_vpn_lockdown_whitelist"` | Comma separated list of packages that are allowed to access the network when VPN is in lockdown mode but not running. | `@Readable(maxTargetSdk = Build.VERSION_CODES.S)` |
| `CREDENTIAL_SERVICE` | `"credential_service"` | The currently selected credential service(s) flattened ComponentName. | `Standard` |
| `CREDENTIAL_SERVICE_PRIMARY` | `"credential_service_primary"` | The currently selected primary credential service flattened ComponentName. | `Standard` |
| `DOCKED_CLOCK_FACE` | `"docked_clock_face"` | Indicates which clock face to show on lock screen and AOD while docked. | `@Readable` |
| `FACE_UNLOCK_ALWAYS_REQUIRE_CONFIRMATION` | `"face_unlock_always_require_confirmation"` | Whether or not face unlock always requires user confirmation, meaning {@link android.hardware.biometrics.BiometricPrompt.Builder#setConfirmationRequired(boolean)} is always 'true'. This overrides the behavior that apps choose in the setConfirmationRequired API. | `@Readable` |
| `FACE_UNLOCK_ATTENTION_REQUIRED` | `"face_unlock_attention_required"` | Whether or not face unlock requires attention. This is a cached value, the source of truth is obtained through the HAL. | `@Readable` |
| `FACE_UNLOCK_DISMISSES_KEYGUARD` | `"face_unlock_dismisses_keyguard"` | Whether or not face unlock dismisses the keyguard. | `@Readable` |
| `FACE_UNLOCK_DIVERSITY_REQUIRED` | `"face_unlock_diversity_required"` | Whether or not face unlock requires a diverse set of poses during enrollment. This is a cached value, the source of truth is obtained through the HAL. | `@Readable` |
| `FACE_UNLOCK_RE_ENROLL` | `"face_unlock_re_enroll"` | Whether or not a user should re enroll their face. Face unlock re enroll. 0 = No re enrollment. 1 = Re enrollment is required. | `@Readable` |
| `FINGERPRINT_SIDE_FPS_AUTH_DOWNTIME` | `"fingerprint_side_fps_auth_downtime"` | The time (in millis) that a power event will ignore future authentications (for side fingerprint) | `@Readable` |
| `KEYGUARD_SLICE_URI` | `"keyguard_slice_uri"` | Uri of the slice that's presented on the keyguard. Defaults to a slice with the date and next alarm. | `@Readable` |
| `KNOWN_TRUST_AGENTS_INITIALIZED` | `"known_trust_agents_initialized"` | Set to 1 by the system after the list of known trust agents have been initialized. | `Standard` |
| `LOCK_BIOMETRIC_WEAK_FLAGS` | `"lock_biometric_weak_flags"` | A flag containing settings used for biometric weak | `@Readable` `@Deprecated` |
| `LOCK_TO_APP_EXIT_LOCKED` | `"lock_to_app_exit_locked"` | Whether lock-to-app will lock the keyguard when exiting. | `@Readable` |
| `MANDATORY_BIOMETRICS` | `"mandatory_biometrics"` | Whether or not mandatory biometrics is enabled. | `Standard` |
| `MANDATORY_BIOMETRICS_REQUIREMENTS_SATISFIED` | `"mandatory_biometrics_requirements_satisfied"` | Whether or not requirements for mandatory biometrics is satisfied. | `Standard` |
| `PRIVATE_SPACE_AUTO_LOCK` | `"private_space_auto_lock"` | Store auto lock value for private space. The possible values are defined in {@link PrivateSpaceAutoLockOption}. | `Standard` |
| `RELEASE_COMPRESS_BLOCKS_ON_INSTALL` | `"release_compress_blocks_on_install"` | Whether or not compress blocks should be released on install. The setting only determines if the platform will attempt to release compress blocks; it does not guarantee that the files will have their compress blocks released. Compression is currently only supported on some f2fs filesystems. Type: int (0 for false, 1 for true) | `Standard` |
| `SECURE_FRP_MODE` | `"secure_frp_mode"` | Indicates whether the device is under restricted secure FRP mode. Secure FRP mode is enabled when the device is under FRP. On solving of FRP challenge, device is removed from this mode. Type: int (0 for false, 1 for true) | `@Readable` `@Deprecated` |
| `TRUST_AGENTS_INITIALIZED` | `"trust_agents_initialized"` | Set to 1 by the system after trust agents have been initialized. | `@Readable` |

</details>

---

## 3. Settings.Global (685 Settings)

### Categories in this section:
- [Accessibility & Inclusion (3 settings)](#accessibility-inclusion)
- [Apps, Storage & Package Management (26 settings)](#apps-storage-package-management)
- [Audio, Sound, Vibration & Volume (29 settings)](#audio-sound-vibration-volume)
- [Connectivity, Wi-Fi & Network (308 settings)](#connectivity-wi-fi-network)
- [Developer Options & Debugging (24 settings)](#developer-options-debugging)
- [Display, Screen & Appearance (16 settings)](#display-screen-appearance)
- [General & System Settings (163 settings)](#general-system-settings)
- [Input Methods, Voice & Gestures (30 settings)](#input-methods-voice-gestures)
- [Location & Positioning (8 settings)](#location-positioning)
- [Multi-User, Accounts & Profiles (8 settings)](#multi-user-accounts-profiles)
- [Notifications & Do Not Disturb (Zen) (9 settings)](#notifications-do-not-disturb-(zen))
- [Power, Battery & Thermal (32 settings)](#power-battery-thermal)
- [Security, Privacy & Lockscreen (18 settings)](#security-privacy-lockscreen)
- [Wearables & Wear OS (11 settings)](#wearables-wear-os)

<details open>
<summary><h3 style="display:inline;" id="accessibility-inclusion">Accessibility & Inclusion (3 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACCESSIBILITY_VIBRATION_WATCH_SPEED` | `"vibration_speed"` | Stores current accessibility vibration watch speed (0 = {@link #ACCESSIBILITY_VIBRATION_WATCH_SPEED_VERY_SLOW}, 1 = {@link #ACCESSIBILITY_VIBRATION_WATCH_SPEED_SLOW}, 2 = {@link #ACCESSIBILITY_VIBRATION_WATCH_SPEED_MEDIUM}, 3 = {@link #ACCESSIBILITY_VIBRATION_WATCH_SPEED_FAST}, 4 = {@link #ACCESSIBILITY_VIBRATION_WATCH_SPEED_VERY_FAST}) | `Standard` |
| `ACCESSIBILITY_VIBRATION_WATCH_TYPE` | `"a11y_vibration_watch_type"` | Stores current type of accessibility vibration (0 = {@link #ACCESSIBILITY_VIBRATION_WATCH_TYPE_DIGIT}, 1 = {@link #ACCESSIBILITY_VIBRATION_WATCH_TYPE_TERSE) | `Standard` |
| `HEARING_DEVICE_LOCAL_NOTIFICATION` | `"hearing_device_local_notification"` | A semi-colon separated list of Bluetooth hearing devices' notification data. Each entry is encoded as a key=value list, separated by commas. Ex: "addr=XX:XX:XX:00:11,input_changes=1" The following keys are supported: addr (String) input_changes (boolean) Each entry must contains "addr" attribute, otherwise it'll be ignored. | `Standard` |

</details>

<details open>
<summary><h3 style="display:inline;" id="apps-storage-package-management">Apps, Storage & Package Management (26 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `APPOP_HISTORY_BASE_INTERVAL_MILLIS` | `"baseIntervalMillis"` | Parameter for {@link #APPOP_HISTORY_PARAMETERS} that controls how long is the interval between snapshots in the base case i.e. the most recent part of the history. | `@Readable` |
| `APPOP_HISTORY_INTERVAL_MULTIPLIER` | `"intervalMultiplier"` | Parameter for {@link #APPOP_HISTORY_PARAMETERS} that controls the base for the logarithmic step when building app op history. | `@Readable` |
| `APPOP_HISTORY_MODE` | `"mode"` | Parameter for {@link #APPOP_HISTORY_PARAMETERS} that controls the mode in which the historical registry operates. | `@Readable` |
| `APPOP_HISTORY_PARAMETERS` | `"appop_history_parameters"` | Appop history parameters. These parameters are represented by a comma-delimited key-value list. The following strings are supported as keys: mode (int) baseIntervalMillis (long) intervalMultiplier (int) Ex: "mode=HISTORICAL_MODE_ENABLED_ACTIVE,baseIntervalMillis=1000,intervalMultiplier=10" | `@Readable` |
| `APP_BINDING_CONSTANTS` | `"app_binding_constants"` | No explicit description provided in source comments. | `@Readable` |
| `APP_OPS_CONSTANTS` | `"app_ops_constants"` | App ops specific settings. This is encoded as a key=value list, separated by commas. Ex: "state_settle_time=10000" The following keys are supported: top_state_settle_time (long) fg_service_state_settle_time (long) bg_state_settle_time (long) Type: string | `@Readable` `@TestApi` |
| `AUTOFILL_COMPAT_MODE_ALLOWED_PACKAGES` | `"autofill_compat_mode_allowed_packages"` | The packages allowlisted to be run in autofill compatibility mode. The list of packages is {@code ":"} colon delimited, and each entry has the name of the package and an optional list of url bar resource ids (the list is delimited by brackets&mdash{@code [} and {@code ]}&mdash and is also comma delimited). For example, a list with 3 packages {@code p1}, {@code p2}, and {@code p3}, where package {@code p1} have one id ({@code url_bar}, {@code p2} has none, and {@code p3 } have 2 ids {@code url_foo} and {@code url_bas}) would be {@code p1[url_bar]:p2:p3[url_foo,url_bas]} #DEVICE_CONFIG_AUTOFILL_COMPAT_MODE_ALLOWED_PACKAGES} instead. | `@Readable` `@SystemApi` `@Deprecated` |
| `CARRIER_APP_NAMES` | `"carrier_app_names"` | Map of package name to application names. The application names cannot and will not be localized. App names may not contain colons or semicolons. The value is "packageName1:appName1;packageName2:appName2;..." | `@Readable` `@SystemApi` |
| `CARRIER_APP_WHITELIST` | `"carrier_app_whitelist"` | List of certificate (hex string representation of the application's certificate - SHA-1 or SHA-256) and carrier app package pairs which are allowlisted to prompt the user for install when a sim card with matching UICC carrier privilege rules is inserted. The certificate is used as a key, so the certificate encoding here must be the same as the certificate encoding used on the SIM. The value is "cert1:package1;cert2:package2;..." | `@Readable` `@SystemApi` |
| `COMPANION_APP_NAME` | `"wear_companion_app_name"` | The companion App name. | `@Readable` |
| `CONNECTED_APPS_ALLOWED_PACKAGES` | `"connected_apps_allowed_packages"` | An allow list of packages for which the user has granted the permission to communicate across profiles. | `@Readable` `@FlaggedApi(android.app.admin.flags.Flags.FLAG_BACKUP_CONNECTED_APPS_SETTINGS)` |
| `CONNECTED_APPS_DISALLOWED_PACKAGES` | `"connected_apps_disallowed_packages"` | A block list of packages for which the user has denied the permission to communicate across profiles. | `@Readable` `@FlaggedApi(android.app.admin.flags.Flags.FLAG_BACKUP_CONNECTED_APPS_SETTINGS)` |
| `CUSTOM_BUGREPORT_HANDLER_APP` | `"custom_bugreport_handler_app"` | The package name for the custom bugreport handler app. This app must be allowlisted. This is currently used only by Power Menu short press. instead | `@Readable` `@Deprecated` |
| `INSTALLED_INSTANT_APP_MAX_CACHE_PERIOD` | `"installed_instant_app_max_cache_period"` | The max period for caching installed instant apps in milliseconds. Type: long | `@Readable` |
| `INSTALLED_INSTANT_APP_MIN_CACHE_PERIOD` | `"installed_instant_app_min_cache_period"` | The min period for caching installed instant apps in milliseconds. Type: long | `@Readable` |
| `PACKAGE_VERIFIER_DEFAULT_RESPONSE` | `"verifier_default_response"` | Default response code for package verification. | `@Readable` |
| `READ_EXTERNAL_STORAGE_ENFORCED_DEFAULT` | `"read_external_storage_enforced_default"` | {@hide} | `@Readable` |
| `SEND_ACTION_APP_ERROR` | `"send_action_app_error"` | Flag for allowing ActivityManagerService to send ACTION_APP_ERROR intents on application crashes and ANRs. If this is disabled, the crash/ANR dialog will never display the "Report" button. Type: int (0 = disallow, 1 = allow) | `@Readable` |
| `STORAGE_BENCHMARK_INTERVAL` | `"storage_benchmark_interval"` | {@hide} | `@Readable` |
| `STORAGE_SETTINGS_CLOBBER_THRESHOLD` | `"storage_settings_clobber_threshold"` | Flag to set the timeout for when to refresh the storage settings cached data. Type: long | `@Readable` |
| `SYS_STORAGE_CACHE_PERCENTAGE` | `"sys_storage_cache_percentage"` | Minimum percentage of storage on the device that is reserved for cached data. | `@Readable` |
| `SYS_STORAGE_FULL_THRESHOLD_BYTES` | `"sys_storage_full_threshold_bytes"` | Minimum bytes of free storage on the device before the data partition is considered full. By default, 1 MB is reserved to avoid system-wide SQLite disk full exceptions. | `@Readable` |
| `SYS_STORAGE_THRESHOLD_MAX_BYTES` | `"sys_storage_threshold_max_bytes"` | Maximum byte size of the low storage threshold. This is to ensure that {@link #SYS_STORAGE_THRESHOLD_PERCENTAGE} does not result in an overly large threshold for large storage devices. Currently this must be less than 2GB. This default is 500MB. | `@Readable` |
| `SYS_STORAGE_THRESHOLD_PERCENTAGE` | `"sys_storage_threshold_percentage"` | Minimum percentage of free storage on the device that is used to determine if the device is running low on storage. The default is 10. Say this value is set to 10, the device is considered running low on storage if 90% or more of the device storage is filled up. | `@Readable` |
| `UNINSTALLED_INSTANT_APP_MAX_CACHE_PERIOD` | `"uninstalled_instant_app_max_cache_period"` | The max period for caching uninstalled instant apps in milliseconds. Type: long | `@Readable` |
| `UNINSTALLED_INSTANT_APP_MIN_CACHE_PERIOD` | `"uninstalled_instant_app_min_cache_period"` | The min period for caching uninstalled instant apps in milliseconds. Type: long | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="audio-sound-vibration-volume">Audio, Sound, Vibration & Volume (29 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACTION_VOLUME` | `"android.settings.panel.action.VOLUME"` | Activity Action: Show a settings dialog containing all volume streams. Input: Nothing. Output: Nothing. | `@SdkConstant(SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)` |
| `APPLY_RAMPING_RINGER` | `"apply_ramping_ringer"` | Whether applying ramping ringer on incoming phone call ringtone. 1 = apply ramping ringer 0 = do not apply ramping ringer | `@Readable` `@Deprecated` |
| `AUDIO_SAFE_CSD_CURRENT_VALUE` | `"audio_safe_csd_current_value"` | Persisted safe hearding current CSD value. Values are stored as float percentages where 1.f represents 100% sound dose has been reached. | `Standard` |
| `AUDIO_SAFE_CSD_DOSE_RECORDS` | `"audio_safe_csd_dose_records"` | Persisted safe hearding dose records (see {@link android.media.SoundDoseRecord}) | `Standard` |
| `AUDIO_SAFE_CSD_NEXT_WARNING` | `"audio_safe_csd_next_warning"` | Persisted safe hearding next CSD warning value. Values are stored as float percentages. | `Standard` |
| `AUDIO_SAFE_VOLUME_STATE` | `"audio_safe_volume_state"` | Persisted safe headphone volume management state by AudioService | `@Readable` |
| `CAR_DOCK_SOUND` | `"car_dock_sound"` | URI for the car dock "in" event sound. | `@Readable` |
| `CAR_UNDOCK_SOUND` | `"car_undock_sound"` | URI for the car dock "out" event sound. | `@Readable` |
| `CHARGING_STARTED_SOUND` | `"charging_started_sound"` | URI for "wired charging started" sound. | `@Readable` |
| `DESK_DOCK_SOUND` | `"desk_dock_sound"` | URI for the desk dock "in" event sound. | `@Readable` |
| `DESK_UNDOCK_SOUND` | `"desk_undock_sound"` | URI for the desk dock "out" event sound. | `@Readable` |
| `HEARING_DEVICE_LOCAL_AMBIENT_VOLUME` | `"hearing_device_local_ambient_volume"` | A semi-colon separated list of Bluetooth hearing devices' local ambient volume data. Each entry is encoded as a key=value list, separated by commas. Ex: "addr=XX:XX:XX:00:11,ambient=20,group_ambient=30;addr=XX:XX:XX:00:22,ambient=50" The following keys are supported: addr (String) ambient (int) group_ambient (int) control_expanded (boolean) Each entry must contains "addr" attribute, otherwise it'll be ignored. | `Standard` |
| `KEY_CHORD_POWER_VOLUME_UP` | `"key_chord_power_volume_up"` | Overrides internal R.integer.config_keyChordPowerVolumeUp. Allowable values detailed in frameworks/base/core/res/res/values/config.xml. Used by PhoneWindowManager. | `@Readable` |
| `LOCK_SOUND` | `"lock_sound"` | URI for the "device locked" (keyguard shown) sound. | `@Readable` |
| `LOW_BATTERY_SOUND` | `"low_battery_sound"` | URI for the low battery sound file. | `@Readable` |
| `LOW_BATTERY_SOUND_TIMEOUT` | `"low_battery_sound_timeout"` | Milliseconds after screen-off after which low battery sounds will be silenced. If zero, battery sounds will always play. Defaults to @integer/def_low_battery_sound_timeout in SettingsProvider. | `@Readable` |
| `MAX_SOUND_TRIGGER_DETECTION_SERVICE_OPS_PER_DAY` | `"max_sound_trigger_detection_service_ops_per_day"` | Maximum number of {@link android.media.soundtrigger.SoundTriggerDetectionService} operations per day. | `@Readable` |
| `MODE_RINGER` | `"mode_ringer"` | Ringer mode. This is used internally, changing this value will not change the ringer mode. See AudioManager. | `@Readable` |
| `MUTE_ALARM_STREAM_WITH_RINGER_MODE` | `"mute_alarm_stream_with_ringer_mode"` | Whether or not Alarm stream should always be muted with Ringer. | `@Readable` |
| `MUTE_ALARM_STREAM_WITH_RINGER_MODE_USER_PREFERENCE` | `"mute_alarm_stream_with_ringer_mode_user_preference"` | The user's choice for whether or not Alarm stream should always be muted with Ringer. Note that this is different from {@link #MUTE_ALARM_STREAM_WITH_RINGER_MODE}, which controls the real state of whether or not the Alarm stream and Ringer association occurs. The two Settings are not necessarily equal, if the final decision for the association depends on factors beyond the user's preference. | `Standard` |
| `SHOW_MEDIA_ON_QUICK_SETTINGS` | `"qs_media_controls"` | Whether or not media is shown automatically when bypassing as a heads up. | `@Readable` |
| `SOUND_TRIGGER_DETECTION_SERVICE_OP_TIMEOUT` | `"sound_trigger_detection_service_op_timeout"` | Timeout for a single {@link android.media.soundtrigger.SoundTriggerDetectionService} operation (in ms). | `@Readable` |
| `TRUSTED_SOUND` | `"trusted_sound"` | URI for the "device is trusted" sound, which is played when the device enters the trusted state without unlocking. | `@Readable` |
| `UNLOCK_SOUND` | `"unlock_sound"` | URI for the "device unlocked" sound. | `@Readable` |
| `VIBRATE_FOR_ACTIVE_UNLOCK` | `"wear_vibrate_for_active_unlock"` | Whether haptics are enabled for Active Unlock on wear. | `Standard` |
| `WEAR_MEDIA_CONTROLS_PACKAGE` | `"wear_media_controls_package"` | Setting indicating the name of the Wear OS package that hosts the Media Controls UI. | `@Readable` |
| `WEAR_MEDIA_SESSIONS_PACKAGE` | `"wear_media_sessions_package"` | Setting indicating the name of the Wear OS package responsible for bridging media. | `@Readable` |
| `WIRELESS_CHARGING_STARTED_SOUND` | `"wireless_charging_started_sound"` | URI for the "wireless charging started" sound. | `@Readable` |
| `ZEN_MODE_RINGER_LEVEL` | `"zen_mode_ringer_level"` | Value of the ringer before entering zen mode. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="connectivity-wi-fi-network">Connectivity, Wi-Fi & Network (308 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACCESSIBILITY_VIBRATION_WATCH_ENABLED` | `"a11y_vibration_watch_enabled"` | Current state of accessibility vibration watch feature (0 = false, 1 = true) | `Standard` |
| `ACTION_INTERNET_CONNECTIVITY` | `"android.settings.panel.action.INTERNET_CONNECTIVITY"` | Activity Action: Show a settings dialog containing settings to enable internet connection. Input: Nothing. Output: Nothing. | `@SdkConstant(SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)` |
| `ACTION_NFC` | `"android.settings.panel.action.NFC"` | Activity Action: Show a settings dialog containing NFC-related settings. Input: Nothing. Output: Nothing. | `@SdkConstant(SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)` |
| `ACTION_WIFI` | `"android.settings.panel.action.WIFI"` | Activity Action: Show a settings dialog containing controls for Wifi. Input: Nothing. Output: Nothing. | `@SdkConstant(SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)` |
| `ACTION_WIFI_ADD_NETWORKS` | `"android.settings.WIFI_ADD_NETWORKS"` | Activity Action: Show setting page to process the addition of Wi-Fi networks to the user's saved network list. The app should send a new intent with an extra that holds a maximum of five {@link android.net.wifi.WifiNetworkSuggestion} that specify credentials for the networks to be added to the user's database. The Intent should be sent via the {@link android.app.Activity#startActivityForResult(Intent, int)} API. Note: The app sending the Intent to add the credentials doesn't get any ownership over the newly added network(s). For the Wi-Fi stack, these networks will look like the user manually added them from the Settings UI. Input: The app should put parcelable array list of {@link android.net.wifi.WifiNetworkSuggestion} into the {@link #EXTRA_WIFI_NETWORK_LIST} extra. Output: After {@link android.app.Activity#startActivityForResult(Intent, int)}, the callback {@link android.app.Activity#onActivityResult(int, int, Intent)} will have a result code {@link android.app.Activity#RESULT_OK} to indicate user pressed the save button to save the networks or {@link android.app.Activity#RESULT_CANCELED} to indicate that the user rejected the request. Additionally, an integer array list, stored in {@link #EXTRA_WIFI_NETWORK_RESULT_LIST}, will indicate the process result of each network. | `@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)` |
| `ACTIVITY_STARTS_LOGGING_ENABLED` | `"activity_starts_logging_enabled"` | Feature flag to enable or disable the activity starts logging feature. Type: int (0 for false, 1 for true) Default: 1 | `@Readable` |
| `ADAPTIVE_BATTERY_MANAGEMENT_ENABLED` | `"adaptive_battery_management_enabled"` | Whether or not adaptive battery feature is enabled by user. Platform will turn on this feature if both this flag and {@link #APP_STANDBY_ENABLED} is on. Type: int (0 for false, 1 for true) Default: 1 | `@Readable` |
| `ADB_ENABLED` | `"adb_enabled"` | Whether ADB over USB is enabled. | `@Readable` |
| `ADB_WIFI_ENABLED` | `"adb_wifi_enabled"` | Whether ADB over Wifi is enabled. | `@Readable` |
| `AIRPLANE_MODE_RADIOS` | `"airplane_mode_radios"` | A comma separated list of radios that need to be disabled when airplane mode is on. This overrides WIFI_ON and BLUETOOTH_ON, if Wi-Fi and bluetooth are included in the comma separated list. | `@Readable` |
| `AIRPLANE_MODE_TOGGLEABLE_RADIOS` | `"airplane_mode_toggleable_radios"` | A comma separated list of radios that should to be disabled when airplane mode is on, but can be manually reenabled by the user. For example, if RADIO_WIFI is added to both AIRPLANE_MODE_RADIOS and AIRPLANE_MODE_TOGGLEABLE_RADIOS, then Wifi will be turned off when entering airplane mode, but the user will be able to reenable Wifi in the Settings app. | `@Readable` `@SystemApi` |
| `ALT_BYPASS_WIFI_REQUIREMENT_TIME_MILLIS` | `"alt_bypass_wifi_requirement_time_millis"` | The time at which we should no longer skip the wifi requirement check (we skip the wifi requirement until this time). The time is in millis since epoch. | `@Readable` |
| `AMBIENT_ENABLED` | `"ambient_enabled"` | Whether ambient is currently enabled. | `@Readable` |
| `AMBIENT_LOW_BIT_ENABLED` | `"ambient_low_bit_enabled"` | Whether the ambient low bit mode is enabled. | `@Readable` |
| `AMBIENT_LOW_BIT_ENABLED_DEV` | `"ambient_low_bit_enabled_dev"` | Whether ambient low bit mode is enabled by developer options. | `@Readable` |
| `APN_DB_UPDATE_CONTENT_URL` | `"apn_db_content_url"` | URL for apn_db updates | `@Readable` |
| `APN_DB_UPDATE_METADATA_URL` | `"apn_db_metadata_url"` | URL for apn_db update metadata | `@Readable` |
| `APP_AUTO_RESTRICTION_ENABLED` | `"app_auto_restriction_enabled"` | Whether or not app auto restriction is enabled. When it is enabled, settings app will auto restrict the app if it has bad behavior (e.g. hold wakelock for long time). Type: boolean (0 for false, 1 for true) Default: 1 | `@Readable` |
| `APP_STANDBY_ENABLED` | `"app_standby_enabled"` | Whether or not App Standby feature is enabled by system. This controls throttling of apps based on usage patterns and predictions. Platform will turn on this feature if both this flag and {@link #ADAPTIVE_BATTERY_MANAGEMENT_ENABLED} is on. Type: int (0 for false, 1 for true) Default: 1 | `@Readable` `@SystemApi` |
| `ARE_USER_DISABLED_HDR_FORMATS_ALLOWED` | `"are_user_disabled_hdr_formats_allowed"` | Whether or not user-disabled HDR formats are allowed. The value is boolean (1 or 0). The value '1' means the user preference for disabling a format is ignored, and the disabled formats are still reported to apps (if supported by the display). The value '0' means the user-disabled formats are not reported to apps, even if the display supports them. The list of formats disabled by the user are contained in the USER_DISABLED_HDR_FORMATS setting. This list is treated as empty when the value of this setting is '1'. | `@Readable` `@TestApi` |
| `ART_VERIFIER_VERIFY_DEBUGGABLE` | `"art_verifier_verify_debuggable"` | Enable ART bytecode verification verifications for debuggable apps. 0 = disable, 1 = enable. | `@Readable` |
| `ASSISTED_GPS_ENABLED` | `"assisted_gps_enabled"` | Whether assisted GPS should be enabled or not. | `@Readable` |
| `AUTOFILL_MAX_VISIBLE_DATASETS` | `"autofill_max_visible_datasets"` | Maximum number of visible datasets in the Autofill dataset picker UI, or {@code 0} to use the default value from resources. | `@Readable` |
| `AUTO_WIFI` | `"auto_wifi"` | Whether the auto wifi toggle setting is enabled. | `@Readable` |
| `BLE_SCAN_ALWAYS_AVAILABLE` | `"ble_scan_always_enabled"` | Settings to allow BLE scans to be enabled even when Bluetooth is turned off for connectivity. | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLE_SCAN_BACKGROUND_MODE` | `"ble_scan_background_mode"` | The mode that BLE scanning clients will be moved to when in the background. | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLE_SCAN_BALANCED_INTERVAL_MS` | `"ble_scan_balanced_interval_ms"` | The length in milliseconds of a BLE scan interval in a balanced scan mode. | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLE_SCAN_BALANCED_WINDOW_MS` | `"ble_scan_balanced_window_ms"` | The length in milliseconds of a BLE scan window in a balanced scan mode. | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLE_SCAN_LOW_LATENCY_INTERVAL_MS` | `"ble_scan_low_latency_interval_ms"` | The length in milliseconds of a BLE scan interval in a low-latency scan mode. | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLE_SCAN_LOW_LATENCY_WINDOW_MS` | `"ble_scan_low_latency_window_ms"` | The length in milliseconds of a BLE scan window in a low-latency scan mode. | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLE_SCAN_LOW_POWER_INTERVAL_MS` | `"ble_scan_low_power_interval_ms"` | The length in milliseconds of a BLE scan interval in a low-power scan mode. | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLE_SCAN_LOW_POWER_WINDOW_MS` | `"ble_scan_low_power_window_ms"` | The length in milliseconds of a BLE scan window in a low-power scan mode. | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLUETOOTH_A2DP_OPTIONAL_CODECS_ENABLED_PREFIX` | `"bluetooth_a2dp_optional_codecs_enabled_"` | {@hide} | `@Readable` |
| `BLUETOOTH_A2DP_SINK_PRIORITY_PREFIX` | `"bluetooth_a2dp_sink_priority_"` | {@hide} | `@Readable` |
| `BLUETOOTH_A2DP_SRC_PRIORITY_PREFIX` | `"bluetooth_a2dp_src_priority_"` | {@hide} | `@Readable` |
| `BLUETOOTH_A2DP_SUPPORTS_OPTIONAL_CODECS_PREFIX` | `"bluetooth_a2dp_supports_optional_codecs_"` | {@hide} | `@Readable` |
| `BLUETOOTH_BTSNOOP_DEFAULT_MODE` | `"bluetooth_btsnoop_default_mode"` | {@hide} | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLUETOOTH_CLASS_OF_DEVICE` | `"bluetooth_class_of_device"` | An integer representing the Bluetooth Class of Device (CoD). | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLUETOOTH_DISABLED_PROFILES` | `"bluetooth_disabled_profiles"` | A Long representing a bitmap of profiles that should be disabled when bluetooth starts. See {@link android.bluetooth.BluetoothProfile}. {@hide} | `@Readable` `@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)` |
| `BLUETOOTH_HEADSET_PRIORITY_PREFIX` | `"bluetooth_headset_priority_"` | {@hide} | `@Readable` |
| `BLUETOOTH_HEARING_AID_PRIORITY_PREFIX` | `"bluetooth_hearing_aid_priority_"` | {@hide} | `@Readable` |
| `BLUETOOTH_INPUT_DEVICE_PRIORITY_PREFIX` | `"bluetooth_input_device_priority_"` | {@hide} | `@Readable` |
| `BLUETOOTH_INTEROPERABILITY_LIST` | `"bluetooth_interoperability_list"` | A semi-colon separated list of Bluetooth interoperability workarounds. Each entry is a partial Bluetooth device address string and an integer representing the feature to be disabled, separated by a comma. The integer must correspond to a interoperability feature as defined in "interop.h" in /system/bt. Example: "00:11:22,0;01:02:03:04,2" | `@Readable` |
| `BLUETOOTH_MAP_CLIENT_PRIORITY_PREFIX` | `"bluetooth_map_client_priority_"` | {@hide} | `@Readable` |
| `BLUETOOTH_MAP_PRIORITY_PREFIX` | `"bluetooth_map_priority_"` | {@hide} | `@Readable` |
| `BLUETOOTH_ON` | `"bluetooth_on"` | Whether bluetooth is enabled/disabled 0=disabled. 1=enabled. | `@Readable` |
| `BLUETOOTH_PAN_PRIORITY_PREFIX` | `"bluetooth_pan_priority_"` | {@hide} | `@Readable` |
| `BLUETOOTH_PBAP_CLIENT_PRIORITY_PREFIX` | `"bluetooth_pbap_client_priority_"` | {@hide} | `@Readable` |
| `BLUETOOTH_SAP_PRIORITY_PREFIX` | `"bluetooth_sap_priority_"` | {@hide} | `@Readable` |
| `BURN_IN_PROTECTION_ENABLED` | `"burn_in_protection"` | If burn in protection is enabled. | `@Readable` |
| `CACHED_APPS_FREEZER_ENABLED` | `"cached_apps_freezer"` | Whether the app freezer is enabled on this device. The value of "enabled" enables the app freezer, "disabled" disables it and "device_default" will let the system decide whether to enable the freezer or not | `@Readable` |
| `CAPTIVE_PORTAL_DETECTION_ENABLED` | `"captive_portal_detection_enabled"` | Setting to turn off captive portal detection. Feature is enabled by default and the setting needs to be set to 0 to disable it. | `@Readable` `@Deprecated` |
| `CAPTIVE_PORTAL_FALLBACK_PROBE_SPECS` | `"captive_portal_fallback_probe_specs"` | A list of captive portal detection specifications used in addition to the fallback URLs. Each spec has the format url@@/@@statusCodeRegex@@/@@contentRegex. Specs are separated by "@@,@@". | `@Readable` |
| `CAPTIVE_PORTAL_FALLBACK_URL` | `"captive_portal_fallback_url"` | The URL used for fallback HTTP captive portal detection when previous HTTP and HTTPS captive portal detection attemps did not return a conclusive answer. | `@Readable` |
| `CAPTIVE_PORTAL_HTTPS_URL` | `"captive_portal_https_url"` | The URL used for HTTPS captive portal detection upon a new connection. A 204 response code from the server is used for validation. | `@Readable` |
| `CAPTIVE_PORTAL_HTTP_URL` | `"captive_portal_http_url"` | The URL used for HTTP captive portal detection upon a new connection. A 204 response code from the server is used for validation. | `@Readable` |
| `CAPTIVE_PORTAL_MODE` | `"captive_portal_mode"` | What to do when connecting a network that presents a captive portal. Must be one of the CAPTIVE_PORTAL_MODE_* constants above. The default for this setting is CAPTIVE_PORTAL_MODE_PROMPT. | `@Readable` |
| `CAPTIVE_PORTAL_OTHER_FALLBACK_URLS` | `"captive_portal_other_fallback_urls"` | A comma separated list of URLs used for captive portal detection in addition to the fallback HTTP url associated with the CAPTIVE_PORTAL_FALLBACK_URL settings. | `@Readable` |
| `CAPTIVE_PORTAL_SERVER` | `"captive_portal_server"` | The server used for captive portal detection upon a new conection. A 204 response code from the server is used for validation. TODO: remove this deprecated symbol. | `@Readable` |
| `CAPTIVE_PORTAL_USER_AGENT` | `"captive_portal_user_agent"` | Which User-Agent string to use in the header of the captive portal detection probes. The User-Agent field is unset when this setting has no value (HttpUrlConnection default). | `@Readable` |
| `CAPTIVE_PORTAL_USE_HTTPS` | `"captive_portal_use_https"` | Whether to use HTTPS for network validation. This is enabled by default and the setting needs to be set to 0 to disable it. This setting is a misnomer because captive portals don't actually use HTTPS, but it's consistent with the other settings. | `@Readable` |
| `CERT_PIN_UPDATE_METADATA_URL` | `"cert_pin_metadata_url"` | URL for cert pinlist updates | `@Readable` |
| `CHAINED_BATTERY_ATTRIBUTION_ENABLED` | `"chained_battery_attribution_enabled"` | Flag to toggle whether system services report attribution chains when they attribute battery use via a {@code WorkSource}. Type: int (0 to disable, 1 to enable) | `@Readable` |
| `CHARGING_SOUNDS_ENABLED` | `"charging_sounds_enabled"` | Whether to play a sound for charging events. | `@Deprecated` |
| `CHARGING_SOUNDS_ENABLED` | `"wear_charging_sounds_enabled"` | Whether charging sounds are enabled. | `@Readable` |
| `CHARGING_VIBRATION_ENABLED` | `"charging_vibration_enabled"` | Whether to vibrate for wireless charging events. | `@Deprecated` |
| `CLOCKWORK_LONG_PRESS_TO_ASSISTANT_ENABLED` | `"clockwork_long_press_to_assistant_enabled"` | Setting to disable power button long press launching Assistant. It's boolean, i.e. enabled = 1, disabled = 0. By default, this setting is enabled. | `Standard` |
| `COMBINED_LOCATION_ENABLE` | `"combined_location_enable"` | Whether the device has combined location setting enabled. | `@Deprecated` |
| `COMPANION_BLE_ROLE` | `"companion_ble_role"` | The bluetooth settings selected BLE role for the companion. | `Standard` |
| `CONNECTIVITY_CHANGE_DELAY` | `"connectivity_change_delay"` | The number of milliseconds to delay before sending out {@link ConnectivityManager#CONNECTIVITY_ACTION} broadcasts. Ignored. | `@Readable` |
| `CONNECTIVITY_KEEP_DATA_ON` | `"wear_connectivity_keep_data_on"` | Whether 1P apps vote for enabling data during different modes, i.e. BTM, BBSM | `@Readable(maxTargetSdk = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)` |
| `CONNECTIVITY_METRICS_BUFFER_SIZE` | `"connectivity_metrics_buffer_size"` | Size of the event buffer for IP connectivity metrics. | `@Readable` |
| `CONNECTIVITY_SAMPLING_INTERVAL_IN_SECONDS` | `"connectivity_sampling_interval_in_seconds"` | Network sampling interval, in seconds. We'll generate link information about bytes/packets sent and error rates based on data sampled in this interval | `@Readable` |
| `CONSISTENT_NOTIFICATION_BLOCKING_ENABLED` | `"consistent_notification_blocking_enabled"` | Whether consistent notification blocking experience is enabled. | `@Readable` |
| `CONTACTS_DATABASE_WAL_ENABLED` | `"contacts_database_wal_enabled"` | Flag to toggle journal mode WAL on or off for the contacts database. WAL is enabled by default. Set to 0 to disable. | `@Readable` |
| `CONTACT_METADATA_SYNC` | `"contact_metadata_sync"` | Whether to enable contacts metadata syncing or not The value 1 - enable, 0 - disable | `@Readable` `@Deprecated` |
| `CONTACT_METADATA_SYNC_ENABLED` | `"contact_metadata_sync_enabled"` | Whether to enable contacts metadata syncing or not The value 1 - enable, 0 - disable | `@Readable` |
| `CONVERSATION_ACTIONS_UPDATE_METADATA_URL` | `"conversation_actions_metadata_url"` | URL for conversation actions model update metadata | `@Readable` |
| `DATABASE_CREATION_BUILDID` | `"database_creation_buildid"` | The build id of when the settings database was first created (or re-created due it being missing). Type: string | `@Readable` |
| `DATABASE_DOWNGRADE_REASON` | `"database_downgrade_reason"` | The reason for the settings database being downgraded. This is only for troubleshooting purposes and its value should not be interpreted in any way. Type: string | `@Readable` |
| `DATA_ACTIVITY_TIMEOUT_MOBILE` | `"data_activity_timeout_mobile"` | Inactivity timeout to track mobile data activity. If set to a positive integer, it indicates the inactivity timeout value in seconds to infer the data activity of mobile network. After a period of no activity on mobile networks with length specified by the timeout, an {@code ACTION_DATA_ACTIVITY_CHANGE} intent is fired to indicate a transition of network status from "active" to "idle". Any subsequent activity on mobile networks triggers the firing of {@code ACTION_DATA_ACTIVITY_CHANGE} intent indicating transition from "idle" to "active". Network activity refers to transmitting or receiving data on the network interfaces. Tracking is disabled if set to zero or negative value. | `@Readable` |
| `DATA_ACTIVITY_TIMEOUT_WIFI` | `"data_activity_timeout_wifi"` | Timeout to tracking Wifi data activity. Same as {@code DATA_ACTIVITY_TIMEOUT_MOBILE} but for Wifi network. | `@Readable` |
| `DATA_ROAMING` | `"data_roaming"` | Whether or not data roaming is enabled. (0 = false, 1 = true) Use {@link TelephonyManager#isDataRoamingEnabled} instead of calling via settings. | `@Readable(maxTargetSdk = Build.VERSION_CODES.S_V2)` |
| `DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS` | `"data_stall_alarm_aggressive_delay_in_ms"` | The number of milliseconds to delay when checking for data stalls during aggressive detection. (screen on or suspected data stall) | `@Readable` |
| `DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS` | `"data_stall_alarm_non_aggressive_delay_in_ms"` | The number of milliseconds to delay when checking for data stalls during non-aggressive detection. (screen is turned off.) | `@Readable` |
| `DATA_STALL_RECOVERY_ON_BAD_NETWORK` | `"data_stall_recovery_on_bad_network"` | Whether to try cellular data recovery when a bad network is reported. | `@Readable` |
| `DECOMPOSABLE_WATCHFACE` | `"current_watchface_decomposable"` | Whether the current watchface is decomposable. | `@Readable` |
| `DEFAULT_DNS_SERVER` | `"default_dns_server"` | Setting for default DNS in case nobody suggests one | `@Readable` |
| `DEFAULT_RESTRICT_BACKGROUND_DATA` | `"default_restrict_background_data"` | The default value for whether background data is enabled or not. Used by {@code NetworkPolicyManagerService}. | `@Readable` |
| `DEVELOPMENT_ENABLE_FREEFORM_WINDOWS_SUPPORT` | `"enable_freeform_support"` | Whether to enable experimental freeform support for windows. | `@Readable` |
| `DEVELOPMENT_ENABLE_NON_RESIZABLE_MULTI_WINDOW` | `"enable_non_resizable_multi_window"` | Whether to allow non-resizable apps to be shown in multi-window. The app will be letterboxed if the request orientation is not met, and will be shown in size-compat mode if the container size has changed. | `@Readable` `@TestApi` |
| `DEVELOPMENT_FORCE_RESIZABLE_ACTIVITIES` | `"force_resizable_activities"` | Whether any activity can be resized. When this is true, any activity, regardless of manifest values, can be resized for multi-window. (0 = false, 1 = true) | `@Readable` |
| `DEVELOPMENT_SETTINGS_ENABLED` | `"development_settings_enabled"` | Whether user has enabled development settings. | `@Readable` |
| `DEVICE_CONFIG_SYNC_DISABLED` | `"device_config_sync_disabled"` | Whether or not syncs (bulk set operations) for {@link DeviceConfig} are currently persistently disabled. This is only used for the {@link Config#SYNC_DISABLED_MODE_PERSISTENT persistent} mode, {@link Config#SYNC_DISABLED_MODE_UNTIL_REBOOT until_reboot} mode is not stored in settings. The value is boolean (1 or 0). The value '1' means that {@link DeviceConfig#setProperties(DeviceConfig.Properties)} will return {@code false}. | `Standard` |
| `DEVICE_PROVISIONING_MOBILE_DATA_ENABLED` | `"device_provisioning_mobile_data"` | Indicates whether mobile data should be allowed while the device is being provisioned. This allows the provisioning process to turn off mobile data before the user has an opportunity to set things up, preventing other processes from burning precious bytes before wifi is setup. Type: int (0 for false, 1 for true) | `@Readable` `@SystemApi` |
| `DISABLE_AOD_WHILE_PLUGGED` | `"disable_aod_while_plugged"` | Whether to disable AOD while plugged. (0 = false, 1 = true) | `Standard` |
| `DISABLE_SCREEN_SHARE_PROTECTIONS_FOR_APPS_AND_NOTIFICATIONS` | `"disable_screen_share_protections_for_apps_and_notifications"` | Whether to disable app and notification screen share protections. The value 1 - enable, 0 - disable | `@Readable` |
| `DISABLE_WINDOW_BLURS` | `"disable_window_blurs"` | Setting to disable cross-window blurs. This includes window blur behind, (see {@link LayoutParams#setBlurBehindRadius}) and window background blur (see {@link Window#setBackgroundBlurRadius}). The value is a boolean (1 or 0). | `@Readable` `@TestApi` |
| `DNS_RESOLVER_MAX_SAMPLES` | `"dns_resolver_max_samples"` | Maximum number taken into account for statistics purposes in the system DNS resolver. {@hide} | `@Readable` |
| `DNS_RESOLVER_MIN_SAMPLES` | `"dns_resolver_min_samples"` | Minimum number of samples needed for statistics to be considered meaningful in the system DNS resolver. {@hide} | `@Readable` |
| `DNS_RESOLVER_SAMPLE_VALIDITY_SECONDS` | `"dns_resolver_sample_validity_seconds"` | Sample validity in seconds to configure for the system DNS resolver. {@hide} | `@Readable` |
| `DNS_RESOLVER_SUCCESS_THRESHOLD_PERCENT` | `"dns_resolver_success_threshold_percent"` | Success threshold in percent for use with the system DNS resolver. {@hide} | `@Readable` |
| `DOCK_AUDIO_MEDIA_ENABLED` | `"dock_audio_media_enabled"` | Use Dock audio output for media: 0 = disabled 1 = enabled | `@Readable` |
| `DOCK_SOUNDS_ENABLED` | `"dock_sounds_enabled"` | Whether to play a sound for dock events. | `@Readable` |
| `DOCK_SOUNDS_ENABLED_WHEN_ACCESSIBILITY` | `"dock_sounds_enabled_when_accessbility"` | Whether to play a sound for dock events, only when an accessibility service is on. | `@Readable` |
| `DSRM_ENABLED_ACTIONS` | `"dsrm_enabled_actions"` | The list of DSRM enabled actions, separated by commas. Ex: "true,true,false,true,true" See com.android.internal.telephony.data.DataStallRecoveryManager for more info | `Standard` |
| `DYNAMIC_COLOR_THEME_ENABLED` | `"dynamic_color_theme_enabled"` | Whether dynamic color theming (e.g. Material You) is enabled for apps which support it. | `@Readable` |
| `DYNAMIC_POWER_SAVINGS_DISABLE_THRESHOLD` | `"dynamic_power_savings_disable_threshold"` | The setting that backs the disable threshold for the setPowerSavingsWarning api in PowerManager | `@Readable` `@TestApi` |
| `DYNAMIC_POWER_SAVINGS_ENABLED` | `"dynamic_power_savings_enabled"` | The setting which backs the setDynamicPowerSaveHint api in PowerManager. | `@Readable` `@TestApi` |
| `ENABLED_SUBSCRIPTION_FOR_SLOT` | `"enabled_subscription_for_slot"` | Which subscription is enabled for a physical slot. | `@Readable` |
| `ENABLE_16K_PAGES` | `"enable_16k_pages"` | Whether to boot with 16K page size compatible kernel 1 = Boot with 16K kernel 0 = Boot with 4K kernel (default) | `@Readable` |
| `ENABLE_ACCESSIBILITY_GLOBAL_GESTURE_ENABLED` | `"enable_accessibility_global_gesture_enabled"` | Setting whether the global gesture for enabling accessibility is enabled. If this gesture is enabled the user will be able to perfrom it to enable the accessibility state without visiting the settings app. No longer used. Should be removed once all dependencies have been updated. | `@Readable` `@UnsupportedAppUsage` |
| `ENABLE_ADB_INCREMENTAL_INSTALL_DEFAULT` | `"enable_adb_incremental_install_default"` | Toggle to enable/disable the incremental ADB installation by default. If not set, default adb installations are incremental; set to zero to use full ones. Note: only ADB uses it, no usages in the Framework code. Type: int (0 to disable, 1 to enable) | `@Readable` |
| `ENABLE_ALL_LANGUAGES` | `"enable_all_languages"` | A boolean value to indicate if we want to support all languages in LE edition on wear. 1 for supporting, 0 for not supporting. | `@Readable` |
| `ENABLE_AUTOMATIC_SYSTEM_SERVER_HEAP_DUMPS` | `"enable_automatic_system_server_heap_dumps"` | Whether to enable automatic system server heap dumps. This only works on userdebug or eng builds, not on user builds. This is set by the user and overrides the config value. 1 means enable, 0 means disable. | `@Readable` |
| `ENABLE_BACK_ANIMATION` | `"enable_back_animation"` | Whether back preview animations are played when user does a back gesture or presses the back button. | `Standard` |
| `ENABLE_CACHE_QUOTA_CALCULATION` | `"enable_cache_quota_calculation"` | Whether the cache quota calculation task is enabled/disabled. | `@Readable` |
| `ENABLE_CELLULAR_ON_BOOT` | `"enable_cellular_on_boot"` | Whether to enable cellular on boot. The value 1 - enable, 0 - disable | `@Readable` |
| `ENABLE_DELETION_HELPER_NO_THRESHOLD_TOGGLE` | `"enable_deletion_helper_no_threshold_toggle"` | Whether the Deletion Helper no threshold toggle is available. | `@Readable` |
| `ENABLE_DISKSTATS_LOGGING` | `"enable_diskstats_logging"` | Whether the diskstats logging task is enabled/disabled. | `@Readable` |
| `ENABLE_EPHEMERAL_FEATURE` | `"enable_ephemeral_feature"` | Toggle to enable/disable the entire ephemeral feature. By default, ephemeral is enabled. Set to zero to disable. Type: int (0 for false, 1 for true) | `@Readable` |
| `ENABLE_GNSS_RAW_MEAS_FULL_TRACKING` | `"enable_gnss_raw_meas_full_tracking"` | Enable GNSS Raw Measurements Full Tracking? 0 = no 1 = yes | `@Readable` |
| `ENABLE_GPU_DEBUG_LAYERS` | `"enable_gpu_debug_layers"` | Allow GPU debug layers? 0 = no 1 = yes | `@Readable` |
| `ENABLE_MULTI_SLOT_TIMEOUT_MILLIS` | `"enable_multi_slot_timeout_millis"` | Flag to set the waiting time for enabling multi SIM slot. Type: long | `Standard` |
| `ENABLE_RADIO_BUG_DETECTION` | `"enable_radio_bug_detection"` | Enable/disable radio bug detection {@hide} | `@Readable` |
| `ENCODED_SURROUND_OUTPUT_ENABLED_FORMATS` | `"encoded_surround_output_enabled_formats"` | Surround sounds formats that are enabled when ENCODED_SURROUND_OUTPUT is set to ENCODED_SURROUND_OUTPUT_MANUAL. Encoded as comma separated list. Allowed values are the format constants defined in AudioFormat.java. Ex: "5,6" | `@Readable` |
| `EUICC_REMOVING_INVISIBLE_PROFILES_TIMEOUT_MILLIS` | `"euicc_removing_invisible_profiles_timeout_millis"` | Flag to set the waiting time for removing invisible euicc profiles inside System > Settings. Type: long | `@Readable` |
| `EXTRA_WIFI_NETWORK_LIST` | `"android.provider.extra.WIFI_NETWORK_LIST"` | A bundle extra of {@link #ACTION_WIFI_ADD_NETWORKS} intent action that indicates the list of the {@link android.net.wifi.WifiNetworkSuggestion} elements. The maximum count of the {@link android.net.wifi.WifiNetworkSuggestion} elements in the list will be five. For example: To provide credentials for one open and one WPA2 networks: {@code final WifiNetworkSuggestion suggestion1 = new WifiNetworkSuggestion.Builder() .setSsid("test111111") .build(); final WifiNetworkSuggestion suggestion2 = new WifiNetworkSuggestion.Builder() .setSsid("test222222") .setWpa2Passphrase("test123456") .build(); final List suggestionsList = new ArrayList<>; suggestionsList.add(suggestion1); suggestionsList.add(suggestion2); Bundle bundle = new Bundle(); bundle.putParcelableArrayList(Settings.EXTRA_WIFI_NETWORK_LIST,(ArrayList<? extends Parcelable>) suggestionsList); final Intent intent = new Intent(Settings.ACTION_WIFI_ADD_NETWORKS); intent.putExtras(bundle); startActivityForResult(intent, 0); } | `Standard` |
| `EXTRA_WIFI_NETWORK_RESULT_LIST` | `"android.provider.extra.WIFI_NETWORK_RESULT_LIST"` | A bundle extra of the result of {@link #ACTION_WIFI_ADD_NETWORKS} intent action that indicates the action result of the saved {@link android.net.wifi.WifiNetworkSuggestion}. Its value is a list of integers, and all the elements will be 1:1 mapping to the elements in {@link #EXTRA_WIFI_NETWORK_LIST}, if user press cancel to cancel the add networks request, then its value will be null. Note: The integer value will be one of the {@link #ADD_WIFI_RESULT_SUCCESS}, {@link #ADD_WIFI_RESULT_ADD_OR_UPDATE_FAILED}, or {@link #ADD_WIFI_RESULT_ALREADY_EXISTS}}. | `Standard` |
| `FORCED_APP_STANDBY_FOR_SMALL_BATTERY_ENABLED` | `"forced_app_standby_for_small_battery_enabled"` | Whether or not to enable Forced App Standby on small battery devices. Type: int (0 for false, 1 for true) Default: 0 | `@Readable` |
| `FORCE_ENABLE_PSS_PROFILING` | `"force_enable_pss_profiling"` | Describes whether AM's AppProfiler should collect PSS even if RSS is the default. This can be set by a user in developer settings. Default: 0 | `@Readable` |
| `FORCE_NON_DEBUGGABLE_FINAL_BUILD_FOR_COMPAT` | `"force_non_debuggable_final_build_for_compat"` | Flag for forcing {@link com.android.server.compat.OverrideValidatorImpl} to consider this a non-debuggable build. | `Standard` |
| `FOREGROUND_SERVICE_STARTS_LOGGING_ENABLED` | `"foreground_service_starts_logging_enabled"` | Feature flag to enable or disable the foreground service starts logging feature. Type: int (0 for false, 1 for true) Default: 1 | `@Readable` |
| `GESTURE_TOUCH_AND_HOLD_WATCH_FACE_ENABLED` | `"gesture_touch_and_hold_watchface_enabled"` | Whether touch and hold to edit WF is enabled | `@Readable` |
| `GLOBAL_HTTP_PROXY_EXCLUSION_LIST` | `"global_http_proxy_exclusion_list"` | Exclusion list for global proxy. This string contains a list of comma-separated domains where the global proxy does not apply. Domains should be listed in a comma- separated list. Example of acceptable formats: ".domain1.com,my.domain2.com" Use ConnectivityManager to set/get. | `@Readable` |
| `GLOBAL_HTTP_PROXY_HOST` | `"global_http_proxy_host"` | Host name for global http proxy. Set via ConnectivityManager. | `@Readable` |
| `GLOBAL_HTTP_PROXY_PAC` | `"global_proxy_pac_url"` | The location PAC File for the proxy. | `@Readable` |
| `GLOBAL_HTTP_PROXY_PORT` | `"global_http_proxy_port"` | Integer host port for global http proxy. Set via ConnectivityManager. | `@Readable` |
| `GNSS_SATELLITE_BLOCKLIST` | `"gnss_satellite_blocklist"` | Blocklist of GNSS satellites. This is a list of integers separated by commas to represent pairs of (constellation, svid). Thus, the number of integers should be even. E.g.: "3,0,5,24" denotes (constellation=3, svid=0) and (constellation=5, svid=24) are blocklisted. Note that svid=0 denotes all svids in the constellation are blocklisted. | `Standard` |
| `HEADS_UP_NOTIFICATIONS_ENABLED` | `"heads_up_notifications_enabled"` | Defines global heads up toggle. One of HEADS_UP_OFF, HEADS_UP_ON. | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `HOTWORD_DETECTION_ENABLED` | `"hotword_detection_enabled"` | If hotword detection should be enabled. | `@Readable` |
| `HTTP_PROXY` | `"http_proxy"` | Host name and port for global http proxy. Uses ':' seperator for between host and port. | `@Readable` |
| `INSTANT_APP_DEXOPT_ENABLED` | `"instant_app_dexopt_enabled"` | Toggle to enable/disable dexopt for instant applications. The default is for dexopt to be disabled. Type: int (0 to disable, 1 to enable) | `@Readable` |
| `INTENT_FIREWALL_UPDATE_METADATA_URL` | `"intent_firewall_metadata_url"` | URL for intent firewall update metadata | `@Readable` |
| `LANG_ID_UPDATE_METADATA_URL` | `"lang_id_metadata_url"` | URL for lang id model update metadata | `@Readable` |
| `LOCATION_ENABLE_STATIONARY_THROTTLE` | `"location_enable_stationary_throttle"` | Whether to throttle location when the device is in doze and still. | `Standard` |
| `LOCATION_SETTINGS_LINK_TO_PERMISSIONS_ENABLED` | `"location_settings_link_to_permissions_enabled"` | Flag to enable the link to location permissions in location setting. Set to 0 to disable. | `@Readable` |
| `LOW_POWER_MODE_REMINDER_ENABLED` | `"low_power_mode_reminder_enabled"` | Whether low power mode reminder is enabled. If this value is 0, the device will not receive low power notification. | `Standard` |
| `LOW_POWER_MODE_STICKY_AUTO_DISABLE_ENABLED` | `"low_power_sticky_auto_disable_enabled"` | Whether sticky battery saver should be deactivated once the battery level has reached the threshold specified by {@link #LOW_POWER_MODE_STICKY_AUTO_DISABLE_LEVEL}. | `@Readable` |
| `LOW_POWER_MODE_STICKY_AUTO_DISABLE_LEVEL` | `"low_power_sticky_auto_disable_level"` | When a device is unplugged from a changer (or is rebooted), do not re-activate battery saver even if {@link #LOW_POWER_MODE_STICKY} is 1, if the battery level is equal to or above this threshold. | `@Readable` |
| `LOW_POWER_STANDBY_ENABLED` | `"low_power_standby_enabled"` | Setting indicating whether Low Power Standby is enabled, if supported. Values are: 0: disabled 1: enabled | `Standard` |
| `MASTER_GESTURES_ENABLED` | `"master_gestures_enabled"` | Controls the gestures feature. | `Standard` |
| `MHL_INPUT_SWITCHING_ENABLED` | `"mhl_input_switching_enabled"` | Whether TV will switch to MHL port when a mobile device is plugged in. (0 = false, 1 = true) | `@Readable` |
| `MHL_POWER_CHARGE_ENABLED` | `"mhl_power_charge_enabled"` | Whether TV will charge the mobile device connected at MHL port. (0 = false, 1 = true) | `@Readable` |
| `MOBILE_DATA` | `"mobile_data"` | Whether mobile data connections are allowed by the user. See ConnectivityManager for more info. | `@Readable` `@UnsupportedAppUsage` |
| `MOBILE_DATA_ALWAYS_ON` | `"mobile_data_always_on"` | Whether the mobile data connection should remain active even when higher priority networks like WiFi are active, to help make network switching faster. See ConnectivityService for more info. (0 = disabled, 1 = enabled) | `@Readable` |
| `MODEM_STACK_ENABLED_FOR_SLOT` | `"modem_stack_enabled_for_slot"` | Whether corresponding logical modem is enabled for a physical slot. The value 1 - enable, 0 - disable | `@Readable` |
| `MULTI_SIM_DATA_CALL_SUBSCRIPTION` | `"multi_sim_data_call"` | Subscription Id to be used for data call on a multi sim device. | `@Readable` |
| `MUTE_WHEN_OFF_BODY_ENABLED` | `"obtain_mute_when_off_body"` | If the device should be muted when off body. | `@Readable` |
| `NATIVE_FLAGS_HEALTH_CHECK_ENABLED` | `"native_flags_health_check_enabled"` | Whether we've enabled native flags health check on this device. Takes effect on reboot. The value "1" enables native flags health check; otherwise it's disabled. | `@Readable` |
| `NETPOLICY_OVERRIDE_ENABLED` | `"netpolicy_override_enabled"` | {@hide} | `@Readable` |
| `NETPOLICY_QUOTA_ENABLED` | `"netpolicy_quota_enabled"` | {@hide} | `@Readable` |
| `NETSTATS_AUGMENT_ENABLED` | `"netstats_augment_enabled"` | {@hide} | `@Readable` |
| `NETSTATS_COMBINE_SUBTYPE_ENABLED` | `"netstats_combine_subtype_enabled"` | {@hide} | `@Readable` |
| `NETSTATS_DEV_BUCKET_DURATION` | `"netstats_dev_bucket_duration"` | {@hide} | `@Readable` |
| `NETSTATS_DEV_DELETE_AGE` | `"netstats_dev_delete_age"` | {@hide} | `@Readable` |
| `NETSTATS_DEV_PERSIST_BYTES` | `"netstats_dev_persist_bytes"` | {@hide} | `@Readable` |
| `NETSTATS_DEV_ROTATE_AGE` | `"netstats_dev_rotate_age"` | {@hide} | `@Readable` |
| `NETSTATS_ENABLED` | `"netstats_enabled"` | {@hide} | `@Readable` |
| `NETSTATS_GLOBAL_ALERT_BYTES` | `"netstats_global_alert_bytes"` | {@hide} | `@Readable` |
| `NETSTATS_POLL_INTERVAL` | `"netstats_poll_interval"` | {@hide} | `@Readable` |
| `NETSTATS_SAMPLE_ENABLED` | `"netstats_sample_enabled"` | {@hide} | `@Readable` |
| `NETSTATS_TIME_CACHE_MAX_AGE` | `"netstats_time_cache_max_age"` | {@hide} | `@Readable` `@Deprecated` |
| `NETSTATS_UID_BUCKET_DURATION` | `"netstats_uid_bucket_duration"` | {@hide} | `@Readable` |
| `NETSTATS_UID_DELETE_AGE` | `"netstats_uid_delete_age"` | {@hide} | `@Readable` |
| `NETSTATS_UID_PERSIST_BYTES` | `"netstats_uid_persist_bytes"` | {@hide} | `@Readable` |
| `NETSTATS_UID_ROTATE_AGE` | `"netstats_uid_rotate_age"` | {@hide} | `@Readable` |
| `NETSTATS_UID_TAG_BUCKET_DURATION` | `"netstats_uid_tag_bucket_duration"` | {@hide} | `@Readable` |
| `NETSTATS_UID_TAG_DELETE_AGE` | `"netstats_uid_tag_delete_age"` | {@hide} | `@Readable` |
| `NETSTATS_UID_TAG_PERSIST_BYTES` | `"netstats_uid_tag_persist_bytes"` | {@hide} | `@Readable` |
| `NETSTATS_UID_TAG_ROTATE_AGE` | `"netstats_uid_tag_rotate_age"` | {@hide} | `@Readable` |
| `NETWORK_AVOID_BAD_WIFI` | `"network_avoid_bad_wifi"` | Whether to automatically switch away from wifi networks that lose Internet access. Only meaningful if config_networkAvoidBadWifi is set to 0, otherwise the system always avoids such networks. Valid values are: 0: Don't avoid bad wifi, don't prompt the user. Get stuck on bad wifi like it's 2013. null: Ask the user whether to switch away from bad wifi. 1: Avoid bad wifi. | `@Readable` |
| `NETWORK_DEFAULT_DAILY_MULTIPATH_QUOTA_BYTES` | `"network_default_daily_multipath_quota_bytes"` | Default daily multipath budget used by ConnectivityManager.getMultipathPreference() on metered networks. This default quota is only used if quota could not be determined from data plan or data limit/warning set by the user. | `@Readable` |
| `NETWORK_LOCATION_OPT_IN` | `"network_location_opt_in"` | Whether the user has consented for network location provider (NLP). This setting key will only be used once during OOBE to set NLP initial value through the companion app ToS. This setting key will be synced over from Companion and corresponding toggle in GMS will be enabled. | `Standard` |
| `NETWORK_METERED_MULTIPATH_PREFERENCE` | `"network_metered_multipath_preference"` | User setting for ConnectivityManager.getMeteredMultipathPreference(). This value may be overridden by the system based on device or application state. If null, the value specified by config_networkMeteredMultipathPreference is used. | `@Readable` |
| `NETWORK_PREFERENCE` | `"network_preference"` | User preference for which network(s) should be used. Only the connectivity service should touch this. | `@Readable` |
| `NETWORK_RECOMMENDATIONS_ENABLED` | `"network_recommendations_enabled"` | Value to specify if network recommendations from {@link com.android.server.NetworkScoreService} are enabled. Type: int Valid values: -1 = Forced off 0 = Disabled 1 = Enabled Most readers of this setting should simply check if value == 1 to determine the enabled state. | `@Readable` `@Deprecated` |
| `NETWORK_RECOMMENDATIONS_PACKAGE` | `"network_recommendations_package"` | Which package name to use for network recommendations. If null, network recommendations will neither be requested nor accepted. Use {@link NetworkScoreManager#getActiveScorerPackage()} to read this value and {@link NetworkScoreManager#setActiveScorer(String)} to write it. Type: string - package name | `@Readable` `@Deprecated` |
| `NETWORK_SCORER_APP` | `"network_scorer_app"` | Which package name to use for network scoring. If null, or if the package is not a valid scorer app, external network scores will neither be requested nor accepted. | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `NETWORK_SCORING_PROVISIONED` | `"network_scoring_provisioned"` | Whether the NetworkScoringService has been first initialized. Type: int (0 for false, 1 for true) | `@Readable` |
| `NETWORK_SCORING_UI_ENABLED` | `"network_scoring_ui_enabled"` | Value to specify whether network quality scores and badging should be shown in the UI. Type: int (0 for false, 1 for true) | `@Readable` `@Deprecated` |
| `NETWORK_SWITCH_NOTIFICATION_DAILY_LIMIT` | `"network_switch_notification_daily_limit"` | The maximum number of notifications shown in 24 hours when switching networks. | `@Readable` |
| `NETWORK_SWITCH_NOTIFICATION_RATE_LIMIT_MILLIS` | `"network_switch_notification_rate_limit_millis"` | The minimum time in milliseconds between notifications when switching networks. | `@Readable` |
| `NETWORK_WATCHLIST_ENABLED` | `"network_watchlist_enabled"` | Whether or not Network Watchlist feature is enabled. Type: int (0 for false, 1 for true) Default: 0 | `@Readable` |
| `NETWORK_WATCHLIST_LAST_REPORT_TIME` | `"network_watchlist_last_report_time"` | Network watchlist last report time. | `@Readable` |
| `NIGHT_DISPLAY_FORCED_AUTO_MODE_AVAILABLE` | `"night_display_forced_auto_mode_available"` | Whether night display forced auto mode is available. 0 = unavailable, 1 = available. | `@Readable` |
| `NITZ_NETWORK_DISCONNECT_RETENTION` | `"nitz_network_disconnect_retention"` | If the device connects to a telephony network and was disconnected from a telephony network for less than this time, a previously received NITZ signal can be restored. This value is in milliseconds. It is used for telephony-based time and time zone detection. | `Standard` |
| `NOTIFICATION_BUBBLES` | `"notification_bubbles"` | Whether the notification bubbles are globally enabled The value is boolean (1 or 0). | `@Readable` `@TestApi` `@Deprecated` |
| `NOTIFICATION_FEEDBACK_ENABLED` | `"notification_feedback_enabled"` | When enabled, notifications the notification assistant service has modified will show an indicator. When tapped, this indicator will describe the adjustment made and solicit feedback. This flag will also add a "automatic" option to the long press menu. The value 1 - enable, 0 - disable | `Standard` |
| `OTA_DISABLE_AUTOMATIC_UPDATE` | `"ota_disable_automatic_update"` | Whether to disable the automatic scheduling of system updates. 1 = system updates won't be automatically scheduled (will always present notification instead). 0 = system updates will be automatically scheduled. (default) | `@Readable` `@SystemApi` |
| `PACKAGE_VERIFIER_SETTING_VISIBLE` | `"verifier_setting_visible"` | Show package verification setting in the Settings app. 1 = show (default) 0 = hide | `@Readable` |
| `POWER_BUTTON_DOUBLE_PRESS` | `"power_button_double_press"` | Overrides internal R.integer.config_doublePressOnPowerBehavior. Allowable values detailed in frameworks/base/core/res/res/values/config.xml. Used by PhoneWindowManager. | `Standard` |
| `POWER_SOUNDS_ENABLED` | `"power_sounds_enabled"` | Whether to play a sound for low-battery alerts. | `@Readable` |
| `PREFERRED_NETWORK_MODE` | `"preferred_network_mode"` | See RIL_PreferredNetworkType in ril.h | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `PRIVATE_DNS_DEFAULT_MODE` | `"private_dns_default_mode"` | Forced override of the default mode (hardcoded as "automatic", nee "opportunistic"). This allows changing the default mode without effectively disabling other modes, all of which require explicit user action to enable/configure. See also b/79719289. Value is a string, suitable for assignment to PRIVATE_DNS_MODE above. {@hide} | `@Readable` |
| `PRIVATE_DNS_MODE` | `"private_dns_mode"` | The requested Private DNS mode (string), and an accompanying specifier (string). Currently, the specifier holds the chosen provider name when the mode requests a specific provider. It may be used to store the provider name even when the mode changes so that temporarily disabling and re-enabling the specific provider mode does not necessitate retyping the provider hostname. | `@Readable` |
| `PRIVATE_DNS_SPECIFIER` | `"private_dns_specifier"` | No explicit description provided in source comments. | `@Readable` |
| `PROVISIONING_APN_ALARM_DELAY_IN_MS` | `"provisioning_apn_alarm_delay_in_ms"` | The number of milliseconds to allow the provisioning apn to remain active | `@Readable` |
| `RADIO_BLUETOOTH` | `"bluetooth"` | Constant for use in AIRPLANE_MODE_RADIOS or SATELLITE_MODE_RADIOS to specify Bluetooth radio. | `@Readable` |
| `RADIO_BUG_SYSTEM_ERROR_COUNT_THRESHOLD` | `"radio_bug_system_error_count_threshold"` | Count threshold of RIL system error for radio bug detection {@hide} | `@Readable` |
| `RADIO_BUG_WAKELOCK_TIMEOUT_COUNT_THRESHOLD` | `"radio_bug_wakelock_timeout_count_threshold"` | Count threshold of RIL wakelock timeout for radio bug detection {@hide} | `@Readable` |
| `RADIO_CELL` | `"cell"` | Constant for use in AIRPLANE_MODE_RADIOS to specify Cellular radio. | `@Readable` |
| `RADIO_NFC` | `"nfc"` | Constant for use in AIRPLANE_MODE_RADIOS or SATELLITE_MODE_RADIOS to specify NFC radio. | `@Readable` |
| `RADIO_UWB` | `"uwb"` | Constant for use in SATELLITE_MODE_RADIOS to specify UWB radio. {@hide} | `Standard` |
| `RADIO_WIFI` | `"wifi"` | Constant for use in AIRPLANE_MODE_RADIOS or SATELLITE_MODE_RADIOS to specify Wi-Fi radio. | `@Readable` |
| `RADIO_WIMAX` | `"wimax"` | {@hide} | `@Readable` |
| `RECEIVE_EXPLICIT_USER_INTERACTION_AUDIO_ENABLED` | `"receive_explicit_user_interaction_audio_enabled"` | Record audio from near-field microphone (ie. TV remote) Allows audio recording regardless of sensor privacy state, as it is an intentional user interaction: hold-to-talk Type: int (0 to disable, 1 to enable) | `Standard` |
| `RECOMMENDED_NETWORK_EVALUATOR_CACHE_EXPIRY_MS` | `"recommended_network_evaluator_cache_expiry_ms"` | The expiration time in milliseconds for the {@link android.net.WifiKey} request cache in {@link com.android.server.wifi.RecommendedNetworkEvaluator}. Type: long | `@Readable` `@Deprecated` |
| `RESTRICTED_NETWORKING_MODE` | `"restricted_networking_mode"` | Used to enable / disable the Restricted Networking Mode in which network access is restricted to apps holding the CONNECTIVITY_USE_RESTRICTED_NETWORKS permission. Values are: 0: disabled 1: enabled | `Standard` |
| `RSB_WAKE_ENABLED` | `"rsb_wake_enabled"` | Whether the RSB wake feature is enabled. | `Standard` |
| `RTL_SWIPE_TO_DISMISS_ENABLED_DEV` | `"rtl_swipe_to_dismiss_enabled_dev"` | Whether RTL swipe-to-dismiss is enabled by developer options. (0 = false, 1 = true) | `Standard` |
| `SATELLITE_MODE_ENABLED` | `"satellite_mode_enabled"` | The satellite mode is enabled for the user. When the satellite mode is enabled, the satellite radio will be turned on and all other radios will be turned off. When the satellite mode is disabled, the satellite radio will be turned off and the states of other radios will be restored. When this setting is set to 0, it means the satellite mode is disabled. When this setting is set to 1, it means the satellite mode is enabled. {@hide} | `@Readable` |
| `SATELLITE_MODE_RADIOS` | `"satellite_mode_radios"` | A comma separated list of radios that need to be disabled when satellite mode is on. {@hide} | `@Readable` |
| `SCREENSHOT_ENABLED` | `"screenshot_enabled"` | Whether screenshot is enabled. | `Standard` |
| `SCREEN_UNLOCK_SOUND_ENABLED` | `"screen_unlock_sound_enabled"` | Whether the screen-unlock (keyguard) sound is enabled. | `Standard` |
| `SELINUX_UPDATE_METADATA_URL` | `"selinux_metadata_url"` | URL for selinux (mandatory access control) update metadata | `@Readable` |
| `SETUP_PREPAID_DATA_SERVICE_URL` | `"setup_prepaid_data_service_url"` | URL to open browser on to allow user to manage a prepay account | `@Readable` |
| `SET_GLOBAL_HTTP_PROXY` | `"set_global_http_proxy"` | Enables the UI setting to allow the user to specify the global HTTP proxy and associated exclusion list. | `@Readable` |
| `SHOW_HIDDEN_LAUNCHER_ICON_APPS_ENABLED` | `"show_hidden_icon_apps_enabled"` | Whether or not show hidden launcher icon apps feature is enabled. Type: int (0 for false, 1 for true) Default: 1 | `@Readable` |
| `SHOW_NEW_APP_INSTALLED_NOTIFICATION_ENABLED` | `"show_new_app_installed_notification_enabled"` | Whether or not show new app installed notification is enabled. Type: int (0 for false, 1 for true) Default: 0 | `@Readable` |
| `SMART_ILLUMINATE_ENABLED` | `"smart_illuminate_enabled"` | The enabled/disabled state of the SmartIlluminate. | `@Readable` |
| `SMART_REPLIES_ENABLED` | `"smart_replies_enabled"` | Whether Smart Replies are enabled within Wear. | `@Readable` |
| `SMART_SELECTION_UPDATE_METADATA_URL` | `"smart_selection_metadata_url"` | URL for smart selection model update metadata | `@Readable` |
| `SMS_SHORT_CODES_UPDATE_METADATA_URL` | `"sms_short_codes_metadata_url"` | URL for sms short code update metadata | `@Readable` |
| `SOFT_AP_TIMEOUT_ENABLED` | `"soft_ap_timeout_enabled"` | Whether soft AP will shut down after a timeout period when no devices are connected. Type: int (0 for false, 1 for true) setAutoShutdownEnabled(boolean)} for setting the value and {@link SoftApConfiguration# isAutoShutdownEnabled()} for query. | `@Readable` `@Deprecated` |
| `STEM_1_DATA` | `"STEM_1_DATA"` | No explicit description provided in source comments. | `@Readable` |
| `STEM_1_DEFAULT_DATA` | `"STEM_1_DEFAULT_DATA"` | No explicit description provided in source comments. | `@Readable` |
| `STEM_2_DATA` | `"STEM_2_DATA"` | No explicit description provided in source comments. | `@Readable` |
| `STEM_2_DEFAULT_DATA` | `"STEM_2_DEFAULT_DATA"` | No explicit description provided in source comments. | `@Readable` |
| `STEM_3_DATA` | `"STEM_3_DATA"` | No explicit description provided in source comments. | `@Readable` |
| `STEM_3_DEFAULT_DATA` | `"STEM_3_DEFAULT_DATA"` | No explicit description provided in source comments. | `@Readable` |
| `STEM_PRIMARY_BUTTON_DOUBLE_PRESS` | `"stem_primary_button_double_press"` | Overrides internal R.integer.config_doublePressOnStemPrimaryBehavior. Allowable values detailed in frameworks/base/core/res/res/values/config.xml. Used by PhoneWindowManager. | `Standard` |
| `TETHER_CONFIG_STATE` | `"tethered_config_state"` | Tethered Configuration state. | `Standard` |
| `TETHER_DUN_APN` | `"tether_dun_apn"` | Used to hold a gservices-provisioned apn value for DUN. If set, or the corresponding build config values are set it will override the APN DB values. Consists of a comma separated list of strings: "name,apn,proxy,port,username,password,server,mmsc,mmsproxy,mmsport,mcc,mnc,auth,type" note that empty fields can be omitted: "name,apn,,,,,,,,,310,260,,DUN" | `@Readable` |
| `TETHER_DUN_REQUIRED` | `"tether_dun_required"` | Used to require DUN APN on the device or not - defaults to a build config value which defaults to false. | `@Readable` |
| `TETHER_ENABLE_LEGACY_DHCP_SERVER` | `"tether_enable_legacy_dhcp_server"` | Use the old dnsmasq DHCP server for tethering instead of the framework implementation. Integer values are interpreted as boolean, and the absence of an explicit setting is interpreted as \|false\|. | `@Readable` |
| `TETHER_OFFLOAD_DISABLED` | `"tether_offload_disabled"` | Used to disable trying to talk to any available tethering offload HAL. Integer values are interpreted as boolean, and the absence of an explicit setting is interpreted as \|false\|. | `@Readable` `@SystemApi` |
| `TETHER_SUPPORTED` | `"tether_supported"` | Used to disable Tethering on a device - defaults to true. | `@Readable` `@SystemApi` |
| `TZINFO_UPDATE_METADATA_URL` | `"tzinfo_metadata_url"` | URL for tzinfo (time zone) update metadata | `@Readable` |
| `UNGAZE_ENABLED` | `"ungaze_enabled"` | Whether or not ungaze is enabled. | `Standard` |
| `UNGAZE_SLEEP_ENABLED` | `"ungaze_sleep_enabled"` | Whether of not to send keycode sleep for ungaze when Home is the foreground activity on watch type devices. Type: int (0 for false, 1 for true) Default: 1 | `@Readable` |
| `UPDATABLE_DRIVER_ALL_APPS` | `"updatable_driver_all_apps"` | Updatable driver global preference for all Apps. 0 = Default 1 = All Apps use updatable production driver 2 = All apps use updatable prerelease driver 3 = All Apps use system graphics driver | `@Readable` |
| `UPDATABLE_DRIVER_PRERELEASE_OPT_IN_APPS` | `"updatable_driver_prerelease_opt_in_apps"` | List of Apps selected to use updatable prerelease driver. i.e. , ,..., | `@Readable` |
| `UPDATABLE_DRIVER_PRODUCTION_ALLOWLIST` | `"updatable_driver_production_allowlist"` | Apps on the allowlist that are allowed to use updatable production driver. The string is a list of application package names, seperated by comma. i.e. , ,..., | `@Readable` |
| `UPDATABLE_DRIVER_PRODUCTION_DENYLIST` | `"updatable_driver_production_denylist"` | Apps on the denylist that are forbidden to use updatable production driver. | `@Readable` |
| `UPDATABLE_DRIVER_PRODUCTION_DENYLISTS` | `"updatable_driver_production_denylists"` | List of denylists, each denylist is a denylist for a specific version of updatable production driver. | `@Readable` |
| `UPDATABLE_DRIVER_PRODUCTION_OPT_IN_APPS` | `"updatable_driver_production_opt_in_apps"` | List of Apps selected to use updatable production driver. i.e. , ,..., | `@Readable` |
| `UPDATABLE_DRIVER_PRODUCTION_OPT_OUT_APPS` | `"updatable_driver_production_opt_out_apps"` | List of Apps selected not to use updatable production driver. i.e. , ,..., | `@Readable` |
| `UPDATABLE_DRIVER_SPHAL_LIBRARIES` | `"updatable_driver_sphal_libraries"` | List of libraries in sphal accessible by updatable driver The string is a list of library names, separated by colon. i.e. : :...: | `@Readable` |
| `UPGRADE_DATA_MIGRATION_STATUS` | `"upgrade_data_migration_status"` | The key to indicate the data migration status on device upgrade in Wear Services. | `@Readable` |
| `USB_MASS_STORAGE_ENABLED` | `"usb_mass_storage_enabled"` | USB Mass Storage Enabled | `@Readable` |
| `USER_ABSENT_RADIOS_OFF_FOR_SMALL_BATTERY_ENABLED` | `"user_absent_radios_off_for_small_battery_enabled"` | Whether or not to enable the User Absent, Radios Off feature on small battery devices. Type: int (0 for false, 1 for true) Default: 0 | `@Readable` |
| `USER_ABSENT_TOUCH_OFF_FOR_SMALL_BATTERY_ENABLED` | `"user_absent_touch_off_for_small_battery_enabled"` | Whether or not to enable the User Absent, Touch Off feature on small battery devices. Type: int (0 for false, 1 for true) Default: 0 | `@Readable` |
| `USER_DISABLED_HDR_FORMATS` | `"user_disabled_hdr_formats"` | A comma-separated list of HDR formats that have been disabled by the user. If present, these formats will not be reported to apps, even if the display supports them. This list is treated as empty if the ARE_USER_DISABLED_HDR_FORMATS_ALLOWED setting is '1'. | `@Readable` `@TestApi` |
| `USER_SWITCHER_ENABLED` | `"user_switcher_enabled"` | Whether or not switching/creating users is enabled by user. | `@Readable` |
| `USE_OPEN_WIFI_PACKAGE` | `"use_open_wifi_package"` | The package name of the application that connect and secures high quality open wifi networks automatically. Type: string package name or null if the feature is either not provided or disabled. | `@Readable` `@TestApi` `@Deprecated` |
| `UWB_ENABLED` | `"uwb_enabled"` | Whether UWB should be enabled. | `Standard` |
| `WEAR_POWER_ANOMALY_SERVICE_ENABLED` | `"wear_power_anomaly_service_enabled"` | Whether Wear Power Anomaly Service is enabled. (0 = false, 1 = true) | `Standard` |
| `WEBVIEW_DATA_REDUCTION_PROXY_KEY` | `"webview_data_reduction_proxy_key"` | Webview Data reduction proxy key. | `@Readable` |
| `WIFI_ALWAYS_REQUESTED` | `"wifi_always_requested"` | Whether the wifi data connection should remain active even when higher priority networks like Ethernet are active, to keep both networks. In the case where higher priority networks are connected, wifi will be unused unless an application explicitly requests to use it. See ConnectivityService for more info. (0 = disabled, 1 = enabled) | `@Readable` |
| `WIFI_BADGING_THRESHOLDS` | `"wifi_badging_thresholds"` | The thresholds of the wifi throughput badging (SD, HD etc.) as a comma-delimited list of colon-delimited key-value pairs. The key is the badging enum value defined in android.net.ScoredNetwork and the value is the minimum sustained network throughput in kbps required for the badge. For example: "10:3000,20:5000,30:25000" | `@Readable` `@SystemApi` |
| `WIFI_BOUNCE_DELAY_OVERRIDE_MS` | `"wifi_bounce_delay_override_ms"` | Milliseconds to wait before bouncing Wi-Fi after settings is restored. Note that after the caller is done with this, they should call {@link ContentResolver#delete} to clean up any value that they may have written. | `@Readable` |
| `WIFI_CONNECTED_MAC_RANDOMIZATION_ENABLED` | `"wifi_connected_mac_randomization_enabled"` | Setting to enable connected MAC randomization in Wi-Fi; disabled by default, and setting to 1 will enable it. In the future, additional values may be supported. | `@Readable` `@Deprecated` |
| `WIFI_COUNTRY_CODE` | `"wifi_country_code"` | 802.11 country code in ISO 3166 format | `@Readable` |
| `WIFI_DEVICE_OWNER_CONFIGS_LOCKDOWN` | `"wifi_device_owner_configs_lockdown"` | This setting controls whether WiFi configurations created by a Device Owner app should be locked down (that is, be editable or removable only by the Device Owner App, not even by Settings app). This setting takes integer values. Non-zero values mean DO created configurations are locked down. Value of zero means they are not. Default value in the absence of actual value to this setting is 0. | `@Readable` |
| `WIFI_DISPLAY_CERTIFICATION_ON` | `"wifi_display_certification_on"` | Whether Wifi display certification mode is enabled/disabled 0=disabled. 1=enabled. | `@Readable` |
| `WIFI_DISPLAY_ON` | `"wifi_display_on"` | Whether Wifi display is enabled/disabled 0=disabled. 1=enabled. | `@Readable` |
| `WIFI_DISPLAY_WPS_CONFIG` | `"wifi_display_wps_config"` | WPS Configuration method used by Wifi display, this setting only takes effect when WIFI_DISPLAY_CERTIFICATION_ON is 1 (enabled). Possible values are: WpsInfo.INVALID: use default WPS method chosen by framework WpsInfo.PBC : use Push button WpsInfo.KEYPAD : use Keypad WpsInfo.DISPLAY: use Display | `@Readable` |
| `WIFI_ENHANCED_AUTO_JOIN` | `"wifi_enhanced_auto_join"` | whether frameworks handles wifi auto-join | `@Readable` |
| `WIFI_EPHEMERAL_OUT_OF_RANGE_TIMEOUT_MS` | `"wifi_ephemeral_out_of_range_timeout_ms"` | Timeout for ephemeral networks when all known BSSIDs go out of range. We will disconnect from an ephemeral network if there is no BSSID for that network with a non-null score that has been seen in this time period. If this is less than or equal to zero, we use a more conservative behavior and only check for a non-null score from the currently connected or target BSSID. | `@Readable` |
| `WIFI_FRAMEWORK_SCAN_INTERVAL_MS` | `"wifi_framework_scan_interval_ms"` | The interval in milliseconds to issue wake up scans when wifi needs to connect. This is necessary to connect to an access point when device is on the move and the screen is off. | `@Readable` |
| `WIFI_FREQUENCY_BAND` | `"wifi_frequency_band"` | The operational wifi frequency band Set to one of {@link WifiManager#WIFI_FREQUENCY_BAND_AUTO}, {@link WifiManager#WIFI_FREQUENCY_BAND_5GHZ} or {@link WifiManager#WIFI_FREQUENCY_BAND_2GHZ} | `@Readable` |
| `WIFI_IDLE_MS` | `"wifi_idle_ms"` | The interval in milliseconds after which Wi-Fi is considered idle. When idle, it is possible for the device to be switched from Wi-Fi to the mobile data network. | `@Readable` |
| `WIFI_MAX_DHCP_RETRY_COUNT` | `"wifi_max_dhcp_retry_count"` | The maximum number of times we will retry a connection to an access point for which we have failed in acquiring an IP address from DHCP. A value of N means that we will make N+1 connection attempts in all. | `@Readable` |
| `WIFI_MIGRATION_COMPLETED` | `"wifi_migration_completed"` | Value to specify if wifi settings migration is complete or not. Note: This should only be used from within {@link android.net.wifi.WifiMigration} class. Type: int (0 for false, 1 for true) | `@Readable` |
| `WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS` | `"wifi_mobile_data_transition_wakelock_timeout_ms"` | Maximum amount of time in milliseconds to hold a wakelock while waiting for mobile data connectivity to be established after a disconnect from Wi-Fi. | `@Readable` |
| `WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON` | `"wifi_networks_available_notification_on"` | Whether to notify the user of open networks. If not connected and the scan results have an open network, we will put this notification up. If we attempt to connect to a network or the open network(s) disappear, we remove the notification. When we show the notification, we will not show it again for {@link android.provider.Settings.Secure#WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY} time. {@link android.os.Build.VERSION_CODES#O}. | `@Readable` `@Deprecated` |
| `WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY` | `"wifi_networks_available_repeat_delay"` | Delay (in seconds) before repeating the Wi-Fi networks available notification. Connecting to a network will reset the timer. | `@Readable` `@Deprecated` |
| `WIFI_NETWORK_SHOW_RSSI` | `"wifi_network_show_rssi"` | whether settings show RSSI | `@Readable` |
| `WIFI_NUM_OPEN_NETWORKS_KEPT` | `"wifi_num_open_networks_kept"` | When the number of open networks exceeds this number, the least-recently-used excess networks will be removed. | `@Readable` `@Deprecated` |
| `WIFI_ON` | `"wifi_on"` | Whether the Wi-Fi should be on. Only the Wi-Fi service should touch this. | `@Readable` |
| `WIFI_ON_WHEN_PROXY_DISCONNECTED` | `"wifi_on_when_proxy_disconnected"` | Whether or not to turn on Wifi when proxy is disconnected. Type: int (0 for false, 1 for true) Default: 1 | `@Readable` |
| `WIFI_P2P_DEVICE_NAME` | `"wifi_p2p_device_name"` | The Wi-Fi peer-to-peer device name WifiP2pManager.ActionListener)} for setting the value and {@link android.net.wifi.p2p.WifiP2pDevice#deviceName} for query. | `@Readable` `@Deprecated` |
| `WIFI_P2P_PENDING_FACTORY_RESET` | `"wifi_p2p_pending_factory_reset"` | Indicate whether factory reset request is pending. Type: int (0 for false, 1 for true) | `@Readable` `@Deprecated` |
| `WIFI_POWER_SAVE` | `"wifi_power_save"` | The number of minutes after the WiFi enters power save mode. | `Standard` |
| `WIFI_SCAN_ALWAYS_AVAILABLE` | `"wifi_scan_always_enabled"` | Setting to allow scans to be enabled even wifi is turned off for connectivity. setting the value and {@link WifiManager#isScanAlwaysAvailable()} for query. | `@Readable` `@Deprecated` |
| `WIFI_SCAN_INTERVAL_WHEN_P2P_CONNECTED_MS` | `"wifi_scan_interval_p2p_connected_ms"` | The interval in milliseconds to scan at supplicant when p2p is connected | `@Readable` |
| `WIFI_SCAN_THROTTLE_ENABLED` | `"wifi_scan_throttle_enabled"` | Whether wifi scan throttle is enabled or not. Type: int (0 for false, 1 for true) and {@link WifiManager#isScanThrottleEnabled()} for query. | `@Readable` `@Deprecated` |
| `WIFI_SCORE_PARAMS` | `"wifi_score_params"` | Parameters to adjust the performance of framework wifi scoring methods. Encoded as a comma-separated key=value list, for example: "rssi5=-80:-77:-70:-57,rssi2=-83:-80:-73:-60,horizon=15" This is intended for experimenting with new parameter values, and is normally unset or empty. The example does not include all parameters that may be honored. Default values are provided by code or device configurations. Errors in the parameters will cause the entire setting to be ignored. | `@Readable` `@Deprecated` |
| `WIFI_SLEEP_POLICY` | `"wifi_sleep_policy"` | The policy for deciding when Wi-Fi should go to sleep (which will in turn switch to using the mobile data as an Internet connection). Set to one of {@link #WIFI_SLEEP_POLICY_DEFAULT}, {@link #WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED}, or {@link #WIFI_SLEEP_POLICY_NEVER}. | `@Readable` `@Deprecated` |
| `WIFI_SUPPLICANT_SCAN_INTERVAL_MS` | `"wifi_supplicant_scan_interval_ms"` | The interval in milliseconds to scan as used by the wifi supplicant | `@Readable` |
| `WIFI_VERBOSE_LOGGING_ENABLED` | `"wifi_verbose_logging_enabled"` | Setting to enable verbose logging in Wi-Fi; disabled by default, and setting to 1 will enable it. In the future, additional values may be supported. value and {@link WifiManager#isVerboseLoggingEnabled()} for query. | `@Readable` `@Deprecated` |
| `WIFI_WAKEUP_ENABLED` | `"wifi_wakeup_enabled"` | Value to specify if Wi-Fi Wakeup feature is enabled. Type: int (0 for false, 1 for true) and {@link WifiManager#isAutoWakeupEnabled()} for query. | `@Readable` `@SystemApi` `@Deprecated` |
| `WIFI_WATCHDOG_ON` | `"wifi_watchdog_on"` | Whether the Wi-Fi watchdog is enabled. | `@Readable` |
| `WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED` | `"wifi_watchdog_poor_network_test_enabled"` | Setting to turn off poor network avoidance on Wi-Fi. Feature is enabled by default and the setting needs to be set to 0 to disable it. | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `WIMAX_NETWORKS_AVAILABLE_NOTIFICATION_ON` | `"wimax_networks_available_notification_on"` | {@hide} | `@Readable` |
| `WRIST_DETECTION_AUTO_LOCKING_ENABLED` | `"wear_wrist_detection_auto_locking_enabled"` | A boolean that tracks whether Wrist Detection Auto-Locking is enabled. | `@Readable` |
| `ZRAM_ENABLED` | `"zram_enabled"` | Whether we've enabled zram on this device. Takes effect on reboot. The value "1" enables zram; "0" disables it, and everything else is unspecified. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="developer-options-debugging">Developer Options & Debugging (24 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ADB_DISCONNECT_SESSIONS_ON_REVOKE` | `"adb_disconnect_sessions_on_revoke"` | Whether existing ADB sessions over both USB and Wifi should be terminated when the user revokes debugging authorizations. | `Standard` |
| `ANGLE_DEBUG_PACKAGE` | `"angle_debug_package"` | Package containing ANGLE libraries other than system, which are only available to dumpable apps that opt-in. | `@Readable` |
| `AUTOFILL_LOGGING_LEVEL` | `"autofill_logging_level"` | Level of autofill logging. Valid values are {@link android.view.autofill.AutofillManager#NO_LOGGING}, {@link android.view.autofill.AutofillManager#FLAG_ADD_CLIENT_DEBUG}, or {@link android.view.autofill.AutofillManager#FLAG_ADD_CLIENT_VERBOSE}. | `@Readable` |
| `DEBUG_APP` | `"debug_app"` | Name of an application package to be debugged. | `@Readable` |
| `DEBUG_VIEW_ATTRIBUTES` | `"debug_view_attributes"` | Whether Views are allowed to save their attribute data. | `@Readable` |
| `DEBUG_VIEW_ATTRIBUTES_APPLICATION_PACKAGE` | `"debug_view_attributes_application_package"` | Which application package is allowed to save View attribute data. | `@Readable` |
| `DEVELOPMENT_FORCE_RTL` | `"debug.force_rtl"` | Developer setting to force RTL layout. | `@Readable` |
| `DEVELOPMENT_OVERRIDE_DESKTOP_MODE_FEATURES` | `"override_desktop_mode_features"` | Whether to override the availability of the desktop mode on the main display of the device. If on, users can make move an app to the desktop, allowing a freeform windowing experience. | `@Readable` |
| `DEVELOPMENT_RENDER_SHADOWS_IN_COMPOSITOR` | `"render_shadows_in_compositor"` | If true, shadows drawn around the window will be rendered by the system compositor. If false, shadows will be drawn by the client by setting an elevation on the root view and the contents will be inset by the surface insets. (0 = false, 1 = true) | `@Readable` |
| `ERROR_KERNEL_LOG_PREFIX` | `"kernel_logs_for_"` | Lines of kernel logs to include with system crash/ANR/etc. reports, as a prefix of the dropbox tag of the report type. For example, "kernel_logs_for_system_server_anr" controls the lines of kernel logs captured with system server ANR reports. 0 to disable. | `@Readable` |
| `ERROR_LOGCAT_PREFIX` | `"logcat_for_"` | Lines of logcat to include with system crash/ANR/etc. reports, as a prefix of the dropbox tag of the report type. For example, "logcat_for_system_server_anr" controls the lines of logcat captured with system server ANR reports. 0 to disable. | `@Readable` |
| `GPU_DEBUG_APP` | `"gpu_debug_app"` | App allowed to load GPU debug layers | `@Readable` |
| `GPU_DEBUG_LAYERS` | `"gpu_debug_layers"` | Ordered GPU debug layer list for Vulkan i.e. : :...: | `@Readable` |
| `GPU_DEBUG_LAYERS_GLES` | `"gpu_debug_layers_gles"` | Ordered GPU debug layer list for GLES i.e. : :...: | `@Readable` |
| `GPU_DEBUG_LAYER_APP` | `"gpu_debug_layer_app"` | Addition app for GPU layer discovery | `@Readable` |
| `HIDE_ERROR_DIALOGS` | `"hide_error_dialogs"` | If nonzero, all system error dialogs will be hidden. For example, the crash and ANR dialogs will not be shown, and the system will just proceed as if they had been accepted by the user. | `@Readable` `@TestApi` |
| `PACKAGE_VERIFIER_INCLUDE_ADB` | `"verifier_verify_adb_installs"` | Run package verification on apps installed through ADB/ADT/USB 1 = perform package verification on ADB installs (default) 0 = bypass package verification on ADB installs | `@Readable` |
| `SHOW_ANGLE_IN_USE_DIALOG_BOX` | `"show_angle_in_use_dialog_box"` | Show the "ANGLE In Use" dialog box to the user when ANGLE is the OpenGL driver. The value is a boolean (1 or 0). | `@Readable` |
| `SHOW_FIRST_CRASH_DIALOG` | `"show_first_crash_dialog"` | If nonzero, crashes in foreground processes will bring up a dialog. Otherwise, the process will be silently killed. | `@Readable` `@TestApi` |
| `SHOW_MUTE_IN_CRASH_DIALOG` | `"show_mute_in_crash_dialog"` | If nonzero, crash dialogs will show an option to mute all future crash dialogs for this app. | `@Readable` |
| `SHOW_RESTART_IN_CRASH_DIALOG` | `"show_restart_in_crash_dialog"` | If nonzero, crash dialogs will show an option to restart the app. | `@Readable` |
| `SHOW_USB_TEMPERATURE_ALARM` | `"show_usb_temperature_alarm"` | Whether to show the usb high temperature alarm notification. | `@Readable` |
| `SYS_FREE_STORAGE_LOG_INTERVAL` | `"sys_free_storage_log_interval"` | The interval in minutes after which the amount of free storage left on the device is logged to the event log | `@Readable` |
| `WAIT_FOR_DEBUGGER` | `"wait_for_debugger"` | If 1, when launching DEBUG_APP it will wait for the debugger before starting user code. If 0, it will run normally. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="display-screen-appearance">Display, Screen & Appearance (16 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT` | `"android.settings.MANAGE_APP_USE_FULL_SCREEN_INTENT"` | Activity Action: Show screen for controlling whether an app can send full screen intents. Input: the intent's data URI must specify the application package name for which you want to manage full screen intents. Output: Nothing. | `@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)` |
| `ALWAYS_ON_DISPLAY_CONSTANTS` | `"always_on_display_constants"` | Always on display(AOD) specific settings This is encoded as a key=value list, separated by commas. Ex: "prox_screen_off_delay=10000,screen_brightness_array=0:1:2:3:4" The following keys are supported: screen_brightness_array (int[], values in range [1, 255]) dimming_scrim_array (int[]) prox_screen_off_delay (long) prox_cooldown_trigger (long) prox_cooldown_period (long) | `@Readable` |
| `CUSTOM_COLOR_BACKGROUND` | `"custom_background_color"` | The custom background color. | `@Readable` |
| `CUSTOM_COLOR_FOREGROUND` | `"custom_foreground_color"` | The custom foreground color. | `@Readable` |
| `DEVELOPMENT_FORCE_DESKTOP_MODE_ON_EXTERNAL_DISPLAYS` | `"force_desktop_mode_on_external_displays"` | Whether to enable the legacy freeform support on secondary displays. If enabled, the SECONDARY_HOME of the launcher is started on any secondary display, allowing for a desktop experience. | `@Readable` |
| `DEVELOPMENT_WM_DISPLAY_SETTINGS_PATH` | `"wm_display_settings_path"` | Path to the WindowManager display settings file. If unset, the default file path will be used. | `Standard` |
| `DISPLAY_PANEL_LPM` | `"display_panel_lpm"` | Flag to enable or disable display panel low power mode (lpm) false -> Display panel power saving mode is disabled. true -> Display panel power saving mode is enabled. | `@Readable` |
| `DISPLAY_SCALING_FORCE` | `"display_scaling_force"` | The saved value for WindowManagerService.setForcedDisplayScalingMode(). 0 or unset if scaling is automatic, 1 if scaling is disabled. | `@Readable` |
| `DISPLAY_SIZE_FORCED` | `"display_size_forced"` | The saved value for WindowManagerService.setForcedDisplaySize(). Two integers separated by a comma. If unset, then use the real display size. | `@Readable` |
| `EMULATE_DISPLAY_CUTOUT` | `"emulate_display_cutout"` | {@link android.view.DisplayCutout DisplayCutout} emulation mode. | `@Readable` |
| `FANCY_IME_ANIMATIONS` | `"fancy_ime_animations"` | Scaling factor for normal window animations. Setting to 0 will disable window animations. | `@Readable` |
| `LOCK_SCREEN_STATE` | `"lock_screen_state"` | Current lock screen state of the device (null = default value of this setting (lockscreen is never set), 0 = {@link #LOCK_SCREEN_STATE_NONE}, 1 = {@link #LOCK_SCREEN_STATE_PIN}, 2 = {@link #LOCK_SCREEN_STATE_PATTERN}) | `Standard` |
| `NR_NSA_TRACKING_SCREEN_OFF_MODE` | `"nr_nsa_tracking_screen_off_mode"` | For 5G NSA capable devices, determines whether NR tracking indications are on when the screen is off. Values are: 0: off - All 5G NSA tracking indications are off when the screen is off. 1: extended - All 5G NSA tracking indications are on when the screen is off as long as the device is camped on 5G NSA (5G icon is showing in status bar). If the device is not camped on 5G NSA, tracking indications are off. 2: always on - All 5G NSA tracking indications are on whether the screen is on or off. | `@Readable` |
| `OVERLAY_DISPLAY_DEVICES` | `"overlay_display_devices"` | Overlay display devices setting. The associated value is a specially formatted string that describes the size and density of simulated secondary display devices. Format: [display1];[display2];... with each display specified as: [mode1]\|[mode2]\|...,[flag1],[flag2],... with each mode specified as: [width]x[height]/[densityDpi] Supported flags: secure : creates a secure display own_content_only : only shows this display's own content should_show_system_decorations : supports system decorations Example: 1280x720/213 : make one overlay that is 1280x720 at 213dpi. 1920x1080/320,secure;1280x720/213 : make two overlays, the first at 1080p and secure; the second at 720p. 1920x1080/320\|3840x2160/640 : make one overlay that is 1920x1080 at 213dpi by default, but can also be upscaled to 3840x2160 at 640dpi by the system if the display device allows. If the value is empty, then no overlay display devices are created. | `@Readable` `@TestApi` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `TRANSITION_ANIMATION_SCALE` | `"transition_animation_scale"` | Scaling factor for activity transition animations. The value is a float. Setting to 0.0f will disable window animations. | `@Readable` |
| `WINDOW_ANIMATION_SCALE` | `"window_animation_scale"` | Scaling factor for normal window animations. The value is a float. Setting to 0.0f will disable window animations. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="general-system-settings">General & System Settings (163 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACTION_BUGREPORT_HANDLER_SETTINGS` | `"android.settings.BUGREPORT_HANDLER_SETTINGS"` | Activity Action: Allows user to select current bug report handler. Input: Nothing. Output: Nothing. | `@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)` `@SystemApi` |
| `ACTION_SETTINGS_EMBED_DEEP_LINK_ACTIVITY` | `"android.settings.SETTINGS_EMBED_DEEP_LINK_ACTIVITY"` | Activity Action: For system or preinstalled apps to show their {@link Activity} embedded in Settings app on large screen devices. Developers should resolve the Intent action before using it. Input: {@link #EXTRA_SETTINGS_EMBEDDED_DEEP_LINK_INTENT_URI} must be included to specify the intent for the activity which will be embedded in Settings app. It's an intent URI string from {@code intent.toUri(Intent.URI_INTENT_SCHEME)}. Input: {@link #EXTRA_SETTINGS_EMBEDDED_DEEP_LINK_HIGHLIGHT_MENU_KEY} must be included to specify a key that indicates the menu item which will be highlighted on settings home menu. Output: Nothing. | `@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)` |
| `ACTIVITY_MANAGER_CONSTANTS` | `"activity_manager_constants"` | Activity manager specific settings. This is encoded as a key=value list, separated by commas. Ex: "gc_timeout=5000,max_cached_processes=24" The following keys are supported: max_cached_processes (int) background_settle_time (long) fgservice_min_shown_time (long) fgservice_min_report_time (long) fgservice_screen_on_before_time (long) fgservice_screen_on_after_time (long) content_provider_retain_time (long) gc_timeout (long) gc_min_interval (long) full_pss_min_interval (long) full_pss_lowered_interval (long) power_check_interval (long) power_check_max_cpu_1 (int) power_check_max_cpu_2 (int) power_check_max_cpu_3 (int) power_check_max_cpu_4 (int) service_usage_interaction_time (long) usage_stats_interaction_interval (long) service_restart_duration (long) service_reset_run_duration (long) service_restart_duration_factor (int) service_min_restart_time_between (long) service_max_inactivity (long) service_bg_start_timeout (long) service_bg_activity_start_timeout (long) process_start_async (boolean) Type: string | `@Readable` |
| `AIRPLANE_MODE_ON` | `"airplane_mode_on"` | Whether Airplane Mode is on. | `@Readable` |
| `ALWAYS_FINISH_ACTIVITIES` | `"always_finish_activities"` | If not 0, the activity manager will aggressively finish activities and processes as soon as they are no longer needed. If 0, the normal extended lifetime is used. | `@Readable` |
| `AMBIENT_FORCE_WHEN_DOCKED` | `"ambient_force_when_docked"` | Whether to force ambient when docked. | `@Readable` |
| `AMBIENT_TILT_TO_BRIGHT` | `"ambient_tilt_to_bright"` | Whether ambient tilt to bright is enabled. | `@Readable` |
| `AMBIENT_TILT_TO_WAKE` | `"ambient_tilt_to_wake"` | Whether ambient tilt to wake is enabled. | `@Readable` |
| `ANGLE_EGL_FEATURES` | `"angle_egl_features"` | Lists of ANGLE EGL features for debugging. Each list of features is separated by a comma, each feature in each list is separated by a colon. e.g. feature1:feature2:feature3,feature1:feature3:feature5 | `@Readable` |
| `ANGLE_GL_DRIVER_ALL_ANGLE` | `"angle_gl_driver_all_angle"` | Force all PKGs to use ANGLE, regardless of any other settings The value is a boolean (1 or 0). | `@Readable` |
| `ANGLE_GL_DRIVER_SELECTION_PKGS` | `"angle_gl_driver_selection_pkgs"` | List of PKGs that have an OpenGL driver selected | `@Readable` |
| `ANGLE_GL_DRIVER_SELECTION_VALUES` | `"angle_gl_driver_selection_values"` | List of selected OpenGL drivers, corresponding to the PKGs in GLOBAL_SETTINGS_DRIVER_PKGS | `@Readable` |
| `ANIMATOR_DURATION_SCALE` | `"animator_duration_scale"` | Scaling factor for Animator-based animations. This affects both the start delay and duration of all such animations. The value is a float. Setting to 0.0f will cause animations to end immediately. The default value is 1.0f. | `@Readable` |
| `ANOMALY_CONFIG` | `"anomaly_config"` | A base64-encoded string represents anomaly stats config, used for {@link android.app.StatsManager}. | `@Readable` |
| `ANOMALY_CONFIG_VERSION` | `"anomaly_config_version"` | An integer to show the version of the anomaly config. Ex: 1, which means current version is 1. | `@Readable` |
| `ANOMALY_DETECTION_CONSTANTS` | `"anomaly_detection_constants"` | Battery anomaly detection specific settings This is encoded as a key=value list, separated by commas. wakeup_blacklisted_tags is a string, encoded as a set of tags, encoded via {@link Uri#encode(String)}, separated by colons. Ex: "anomaly_detection_enabled=true,wakelock_threshold=2000,wakeup_alarm_enabled=true," "wakeup_alarm_threshold=10,wakeup_blacklisted_tags=tag1:tag2:with%2Ccomma:with%3Acolon" The following keys are supported: anomaly_detection_enabled (boolean) wakelock_enabled (boolean) wakelock_threshold (long) wakeup_alarm_enabled (boolean) wakeup_alarm_threshold (long) wakeup_blacklisted_tags (string) bluetooth_scan_enabled (boolean) bluetooth_scan_threshold (long) | `@Readable` |
| `AUTOFILL_MAX_PARTITIONS_SIZE` | `"autofill_max_partitions_size"` | Maximum number of partitions that can be allowed in an autofill session. | `@Readable` |
| `AUTO_REVOKE_PARAMETERS` | `"auto_revoke_parameters"` | Auto revoke parameters. These parameters are represented by a comma-delimited key-value list. enabledForPreRApps (bolean) unusedThresholdMs (long) checkFrequencyMs (long) Ex: "enabledForPreRApps=false,unusedThresholdMs=7776000000,checkFrequencyMs=1296000000" | `@Readable` |
| `AWARE_ALLOWED` | `"aware_allowed"` | Indicates whether aware is available in the current location. | `@Readable` |
| `BINDER_CALLS_STATS` | `"binder_calls_stats"` | Binder call stats settings. The following strings are supported as keys: enabled (boolean) detailed_tracking (boolean) upload_data (boolean) sampling_interval (int) | `@Readable` |
| `BOOT_COUNT` | `"boot_count"` | Boot count since the device starts running API level 24. Type: int | `@Readable` |
| `BROADCAST_BG_CONSTANTS` | `"bcast_bg_constants"` | Broadcast dispatch tuning parameters specific to background broadcasts. This is encoded as a key=value list, separated by commas. Ex: "foo=1,bar=true". See {@link #BROADCAST_FG_CONSTANTS} for the list of supported keys. | `@Readable` |
| `BROADCAST_FG_CONSTANTS` | `"bcast_fg_constants"` | Broadcast dispatch tuning parameters specific to foreground broadcasts. This is encoded as a key=value list, separated by commas. Ex: "foo=1,bar=true" The following keys are supported: bcast_timeout (long) bcast_slow_time (long) bcast_deferral (long) bcast_deferral_decay_factor (float) bcast_deferral_floor (long) bcast_allow_bg_activity_start_timeout (long) | `@Readable` |
| `BROADCAST_OFFLOAD_CONSTANTS` | `"bcast_offload_constants"` | Broadcast dispatch tuning parameters specific to specific "offline" broadcasts. This is encoded as a key=value list, separated by commas. Ex: "foo=1,bar=true". See {@link #BROADCAST_FG_CONSTANTS} for the list of supported keys. | `@Readable` |
| `BUG_REPORT` | `"bug_report"` | Whether the bug report is enabled. | `@Readable` |
| `BYPASS_DEVICE_POLICY_MANAGEMENT_ROLE_QUALIFICATIONS` | `"bypass_device_policy_management_role_qualifications"` | Whether bypassing the device policy management role holder qualification is allowed, (0 = false, 1 = true). | `Standard` |
| `CALL_AUTO_RETRY` | `"call_auto_retry"` | CDMA only settings Whether the auto retry is enabled. The value is boolean (1 or 0). | `@Readable` |
| `CDMA_CELL_BROADCAST_SMS` | `"cdma_cell_broadcast_sms"` | CDMA Cell Broadcast SMS 0 = CDMA Cell Broadcast SMS disabled 1 = CDMA Cell Broadcast SMS enabled | `@Readable` |
| `CDMA_ROAMING_MODE` | `"roaming_settings"` | The CDMA roaming mode 0 = Home Networks, CDMA default 1 = Roaming on Affiliated networks 2 = Roaming on any networks | `@Readable` |
| `CDMA_SUBSCRIPTION_MODE` | `"subscription_mode"` | The CDMA subscription mode 0 = RUIM/SIM (default) 1 = NV | `@Readable` |
| `CELL_ON` | `"cell_on"` | Whether cell is enabled/disabled | `@Readable` |
| `COMPANION_NAME` | `"companion_bt_name"` | The bluetooth settings stored companion device name. | `Standard` |
| `COMPANION_OS_VERSION` | `"wear_companion_os_version"` | The companion phone's android version. | `Standard` |
| `COMPATIBILITY_MODE` | `"compatibility_mode"` | If 0, the compatibility mode is off for all applications. If 1, older applications run under compatibility mode. TODO: remove this settings before code freeze (bug/1907571) | `@Readable` |
| `CONVERSATION_ACTIONS_UPDATE_CONTENT_URL` | `"conversation_actions_content_url"` | URL for conversation actions model updates | `@Readable` |
| `COOLDOWN_MODE_ON` | `"cooldown_mode_on"` | Whether the device has Cooldown Mode enabled. | `Standard` |
| `DEFAULT_SM_DP_PLUS` | `"default_sm_dp_plus"` | The default SM-DP+ configured for this device. An SM-DP+ is used by an LPA (see {@link android.service.euicc.EuiccService}) to download profiles. If this value is set, the LPA will query this server for any profiles available to this device. If any are available, they may be downloaded during device provisioning or in settings without needing the user to enter an activation code. | `@Readable` `@SystemApi` |
| `DEFAULT_VIBRATION` | `"default_vibration"` | The default vibration pattern. | `@Readable(maxTargetSdk = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)` |
| `DEVICE_NAME` | `"device_name"` | The name of the device | `@Readable` |
| `DEVICE_POLICY_CONSTANTS` | `"device_policy_constants"` | DevicePolicyManager specific settings. This is encoded as a key=value list, separated by commas. Ex: das_died_service_reconnect_backoff_sec (long) das_died_service_reconnect_backoff_increase (float) das_died_service_reconnect_max_backoff_sec (long) Type: string see also com.android.server.devicepolicy.DevicePolicyConstants | `@Readable` |
| `DEVICE_PROVISIONED` | `"device_provisioned"` | Whether the device has been provisioned (0 = false, 1 = true). On a multiuser device with a separate system user, the screen may be locked as soon as this is set to true and further activities cannot be launched on the system user unless they are marked to show over keyguard. | `@Readable` |
| `DISK_FREE_CHANGE_REPORTING_THRESHOLD` | `"disk_free_change_reporting_threshold"` | Threshold for the amount of change in disk free space required to report the amount of free space. Used to prevent spamming the logs when the disk free space isn't changing frequently. | `@Readable` |
| `DOWNLOAD_MAX_BYTES_OVER_MOBILE` | `"download_manager_max_bytes_over_mobile"` | The maximum size, in bytes, of a download that the download manager will transfer over a non-wifi connection. | `@Readable` |
| `DOWNLOAD_RECOMMENDED_MAX_BYTES_OVER_MOBILE` | `"download_manager_recommended_max_bytes_over_mobile"` | The recommended maximum size, in bytes, of a download that the download manager should transfer over a non-wifi connection. Over this size, the use will be warned, but will have the option to start the download over the mobile connection anyway. | `@Readable` |
| `DROPBOX_AGE_SECONDS` | `"dropbox_age_seconds"` | Maximum age of entries kept by {@link DropBoxManager}. | `@Readable` |
| `DROPBOX_MAX_FILES` | `"dropbox_max_files"` | Maximum number of entry files which {@link DropBoxManager} will keep around. | `@Readable` |
| `DROPBOX_QUOTA_KB` | `"dropbox_quota_kb"` | Maximum amount of disk space used by {@link DropBoxManager} no matter what. | `@Readable` |
| `DROPBOX_QUOTA_PERCENT` | `"dropbox_quota_percent"` | Percent of free disk (excluding reserve) which {@link DropBoxManager} will use. | `@Readable` |
| `DROPBOX_RESERVE_PERCENT` | `"dropbox_reserve_percent"` | Percent of total disk which {@link DropBoxManager} will never dip into. | `@Readable` |
| `DROPBOX_TAG_PREFIX` | `"dropbox:"` | Prefix for per-tag dropbox disable/enable settings. | `@Readable` |
| `DSRM_DURATION_MILLIS` | `"dsrm_duration_millis"` | The duration in milliseconds of each action, separated by commas. Ex: "18000,18000,18000,18000,0" See com.android.internal.telephony.data.DataStallRecoveryManager for more info | `Standard` |
| `EMERGENCY_AFFORDANCE_NEEDED` | `"emergency_affordance_needed"` | A setting that can be read whether the emergency affordance is currently needed. The value is a boolean (1 or 0). | `@Readable` |
| `EMERGENCY_TONE` | `"emergency_tone"` | CDMA only settings Emergency Tone 0 = Off 1 = Alert 2 = Vibrate | `@Readable` |
| `ENCODED_SURROUND_OUTPUT` | `"encoded_surround_output"` | Set to ENCODED_SURROUND_OUTPUT_AUTO, ENCODED_SURROUND_OUTPUT_NEVER, ENCODED_SURROUND_OUTPUT_ALWAYS or ENCODED_SURROUND_OUTPUT_MANUAL | `@Readable` |
| `EPHEMERAL_COOKIE_MAX_SIZE_BYTES` | `"ephemeral_cookie_max_size_bytes"` | Ephemeral app cookie max size in bytes. Type: int | `@Readable` |
| `EUICC_PROVISIONED` | `"euicc_provisioned"` | Whether any profile has ever been downloaded onto a eUICC on the device. Used to hide eUICC UI from users who have never made use of it and would only be confused by seeing references to it in settings. (0 = false, 1 = true) | `@Readable` `@SystemApi` |
| `EUICC_SUPPORTED_COUNTRIES` | `"euicc_supported_countries"` | List of ISO country codes in which eUICC UI is shown. Country codes should be separated by comma. Note: if {@link #EUICC_SUPPORTED_COUNTRIES} is empty, then {@link #EUICC_UNSUPPORTED_COUNTRIES} is used. Used to hide eUICC UI from users who are currently in countries where no carriers support eUICC. | `@Readable` `@SystemApi` |
| `EUICC_UNSUPPORTED_COUNTRIES` | `"euicc_unsupported_countries"` | List of ISO country codes in which eUICC UI is not shown. Country codes should be separated by comma. Note: if {@link #EUICC_SUPPORTED_COUNTRIES} is empty, then {@link #EUICC_UNSUPPORTED_COUNTRIES} is used. Used to hide eUICC UI from users who are currently in countries where no carriers support eUICC. | `@Readable` `@SystemApi` |
| `EXTRA_SETTINGS_EMBEDDED_DEEP_LINK_HIGHLIGHT_MENU_KEY` | `"android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_HIGHLIGHT_MENU_KEY"` | Activity Extra: Specify a key that indicates the menu item which should be highlighted on settings home menu. This must be passed as an extra field to {@link #ACTION_SETTINGS_EMBED_DEEP_LINK_ACTIVITY}. | `Standard` |
| `EXTRA_SETTINGS_EMBEDDED_DEEP_LINK_INTENT_URI` | `"android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_INTENT_URI"` | Activity Extra: Specify the intent for the {@link Activity} which will be embedded in Settings app. It's an intent URI string from {@code intent.toUri(Intent.URI_INTENT_SCHEME)}. This must be passed as an extra field to {@link #ACTION_SETTINGS_EMBED_DEEP_LINK_ACTIVITY}. | `Standard` |
| `FOLDER` | `"folder"` | Arbitrary string (displayed to the user) that allows bookmarks to be organized into categories. There are some special names for standard folders, which all start with '@'. The label displayed for the folder changes with the locale (via {@link #getLabelForFolder}) but the folder name does not change so you can consistently query for the folder regardless of the current locale. Type: TEXT | `Standard` |
| `FORCE_ALLOW_ON_EXTERNAL` | `"force_allow_on_external"` | Whether any package can be on external storage. When this is true, any package, regardless of manifest values, is a candidate for installing or moving onto external storage. (0 = false, 1 = true) | `@Readable` |
| `FPS_DEVISOR` | `"fps_divisor"` | An integer to reduce the FPS by this factor. Only for experiments. Need to reboot the device for this setting to take full effect. | `@Readable` |
| `FSTRIM_MANDATORY_INTERVAL` | `"fstrim_mandatory_interval"` | Time since last fstrim (milliseconds) after which we force one to happen during device startup. If unset, the default is 3 days. | `@Readable` |
| `GPRS_REGISTER_CHECK_PERIOD_MS` | `"gprs_register_check_period_ms"` | The interval in milliseconds at which to check gprs registration after the first registration mismatch of gprs and voice service, to detect possible data network registration problems. | `@Readable` |
| `HAS_PAY_TOKENS` | `"has_pay_tokens"` | Whether the user has any pay tokens on their watch. | `Standard` |
| `HDR_CONVERSION_MODE` | `"hdr_conversion_mode"` | The HDR output mode chosen by the user. This is one of: {@link android.hardware.display.HdrConversionMode#HDR_CONVERSION_PASSTHROUGH}, {@link android.hardware.display.HdrConversionMode#HDR_CONVERSION_SYSTEM}, {@link android.hardware.display.HdrConversionMode#HDR_CONVERSION_FORCE}. | `@Readable` `@TestApi` |
| `HDR_FORCE_CONVERSION_TYPE` | `"hdr_force_conversion_type"` | The output HDR type chosen by the user in case when {@link #HDR_CONVERSION_MODE} is {@link #HDR_CONVERSION_FORCE}. This is one of: {@link android.view.Display.HdrCapabilities#HDR_TYPE_INVALID}, {@link android.view.Display.HdrCapabilities#HDR_TYPE_DOLBY_VISION}, {@link android.view.Display.HdrCapabilities#HDR_TYPE_HDR10}, {@link android.view.Display.HdrCapabilities#HDR_TYPE_HLG}, {@link android.view.Display.HdrCapabilities#HDR_TYPE_HDR10_PLUS} The value is {@link android.view.Display.HdrCapabilities#HDR_TYPE_INVALID} when user chooses SDR output type. | `@Readable` `@TestApi` |
| `HIDDEN_API_BLACKLIST_EXEMPTIONS` | `"hidden_api_blacklist_exemptions"` | Exemptions to the hidden API denylist. | `@Readable` `@TestApi` |
| `HIDDEN_API_POLICY` | `"hidden_api_policy"` | Hidden API enforcement policy for apps. Values correspond to @{@link android.content.pm.ApplicationInfo.HiddenApiEnforcementPolicy} | `@Readable` `@TestApi` |
| `ID` | `"_id"` | The row ID. Type: INTEGER | `Standard` |
| `INET_CONDITION_DEBOUNCE_DOWN_DELAY` | `"inet_condition_debounce_down_delay"` | ms during which to consume extra events related to Inet connection condtion after a transtion to partly-connected | `@Readable` |
| `INET_CONDITION_DEBOUNCE_UP_DELAY` | `"inet_condition_debounce_up_delay"` | ms during which to consume extra events related to Inet connection condition after a transtion to fully-connected | `@Readable` |
| `INTEGRITY_CHECK_INCLUDES_RULE_PROVIDER` | `"verify_integrity_for_rule_provider"` | Run integrity checks for integrity rule providers. 0 = bypass integrity verification on installs from rule providers (default) 1 = perform integrity verification on installs from rule providers | `@Readable` |
| `INTENT` | `"intent"` | The Intent URL of the bookmark, describing what it points to. This value is given to {@link android.content.Intent#getIntent} to create an Intent that can be launched. Type: TEXT | `Standard` |
| `INTENT_FIREWALL_UPDATE_CONTENT_URL` | `"intent_firewall_content_url"` | URL for intent firewall updates | `@Readable` |
| `KERNEL_CPU_THREAD_READER` | `"kernel_cpu_thread_reader"` | Settings for collecting statistics on CPU usage per thread The following strings are supported as keys: num_buckets (int) collected_uids (string) minimum_total_cpu_usage_millis (int) | `@Readable` |
| `LANG_ID_UPDATE_CONTENT_URL` | `"lang_id_content_url"` | URL for lang id model updates | `@Readable` |
| `LAST_CALL_FORWARD_ACTION` | `"last_call_forward_action"` | The last requested call forwarding action. | `@Readable` |
| `LID_BEHAVIOR` | `"lid_behavior"` | Specifies the behaviour the lid triggers when closed See WindowManagerPolicy.WindowManagerFuncs | `@Readable` |
| `LOOPER_STATS` | `"looper_stats"` | Looper stats settings. The following strings are supported as keys: enabled (boolean) sampling_interval (int) | `@Readable` |
| `LTE_SERVICE_FORCED` | `"lte_service_forced"` | Whether user can enable/disable LTE as a preferred network. A carrier might control this via gservices, OMA-DM, carrier app, etc. Type: int (0 for false, 1 for true) | `@Readable` |
| `MANAGED_PROVISIONING_DEFER_PROVISIONING_TO_ROLE_HOLDER` | `"managed_provisioning_defer_provisioning_to_role_holder"` | Whether to enable managed device provisioning via the role holder. | `Standard` |
| `MAX_ERROR_BYTES_PREFIX` | `"max_error_bytes_for_"` | Maximum number of bytes of a system crash/ANR/etc. report that ActivityManagerService should send to DropBox, as a prefix of the dropbox tag of the report type. For example, "max_error_bytes_for_system_server_anr" controls the maximum number of bytes captured with system server ANR reports. Type: int (max size in bytes) | `@Readable` |
| `MDC_INITIAL_MAX_RETRY` | `"mdc_initial_max_retry"` | The value passed to a Mobile DataConnection via bringUp which defines the number of retries to perform when setting up the initial connection. The default value defined in DataConnectionTrackerBase#DEFAULT_MDC_INITIAL_RETRY is currently 1. | `@Readable` |
| `MIN_DURATION_BETWEEN_RECOVERY_STEPS_IN_MS` | `"min_duration_between_recovery_steps"` | Minumim duration in millisecodns between cellular data recovery attempts | `@Readable` |
| `MOBILE_SIGNAL_DETECTOR` | `"mobile_signal_detector"` | The mobile signal detector setting. | `Standard` |
| `MULTI_SIM_SMS_PROMPT` | `"multi_sim_sms_prompt"` | Used to provide option to user to select subscription during send SMS. The value 1 - enable, 0 - disable | `@Readable` |
| `MULTI_SIM_SMS_SUBSCRIPTION` | `"multi_sim_sms"` | Subscription Id to be used for SMS on a multi sim device. | `@Readable` |
| `NETPOLICY_QUOTA_FRAC_JOBS` | `"netpolicy_quota_frac_jobs"` | {@hide} | `@Readable` |
| `NETPOLICY_QUOTA_FRAC_MULTIPATH` | `"netpolicy_quota_frac_multipath"` | {@hide} | `@Readable` |
| `NETPOLICY_QUOTA_LIMITED` | `"netpolicy_quota_limited"` | {@hide} | `@Readable` |
| `NETPOLICY_QUOTA_UNLIMITED` | `"netpolicy_quota_unlimited"` | {@hide} | `@Readable` |
| `NEW_CONTACT_AGGREGATOR` | `"new_contact_aggregator"` | Whether to enable new contacts aggregator or not. The value 1 - enable, 0 - disable | `@Readable` |
| `NITZ_UPDATE_DIFF` | `"nitz_update_diff"` | If Unix epoch time between two NITZ signals is greater than this value then the second signal cannot be ignored. This value is in milliseconds. It is used for telephony-based time and time zone detection. | `@Readable` |
| `NITZ_UPDATE_SPACING` | `"nitz_update_spacing"` | If the elapsed realtime between two NITZ signals is greater than this value then the second signal cannot be ignored. This value is in milliseconds. It is used for telephony-based time and time zone detection. | `@Readable` |
| `NTP_SERVER` | `"ntp_server"` | SNTP client config: The preferred NTP server. This setting overrides the static config.xml configuration when present and valid. The legacy form is the NTP server name as a string. Newer code should use the form: ntp://{server name}[:port] (the standard NTP port, 123, is used if not specified). The settings value can consist of a pipe ("\|") delimited list of server names or ntp:// URIs. When present, all server name / ntp:// URIs must be valid or the entire setting value will be ignored and Android's xml config will be used. For example, the following examples are valid: "time.android.com" "ntp://time.android.com" "ntp://time.android.com:123" "time.android.com\|time.other" "ntp://time.android.com:123\|ntp://time.other:123" "time.android.com\|ntp://time.other:123" | `@Readable` |
| `OEM_SETUP_COMPLETED_STATUS` | `"oem_setup_completed_status"` | The key to indicate to Setup Wizard if OEM setup is completed in Wear Services. | `Standard` |
| `OEM_SETUP_VERSION` | `"oem_setup_version"` | The version of oem setup present. | `@Readable` |
| `ORDERING` | `"ordering"` | The order in which the bookmark should be displayed Type: INTEGER | `Standard` |
| `OVERRIDE_SETTINGS_PROVIDER_RESTORE_ANY_VERSION` | `"override_settings_provider_restore_any_version"` | If set to 1, SettingsProvider's restoreAnyVersion="true" attribute will be ignored and restoring to lower version of platform API will be skipped. | `@Readable` |
| `PAC_CHANGE_DELAY` | `"pac_change_delay"` | The series of successively longer delays used in retrying to download PAC file. Last delay is used between successful PAC downloads. | `@Readable` |
| `PAIRED_DEVICE_OS_TYPE` | `"paired_device_os_type"` | What OS does paired device has. | `@Readable` |
| `PEOPLE_SPACE_CONVERSATION_TYPE` | `"people_space_conversation_type"` | Which types of conversation(s) to show in People Space. Values are: 0: Single user-selected conversation (default) 1: Priority conversations only 2: All conversations | `Standard` |
| `PHONE_PLAY_STORE_AVAILABILITY` | `"phone_play_store_availability"` | The play store availability on companion phone. | `@Readable` |
| `PHONE_SWITCHING_REQUEST_SOURCE` | `"phone_switching_request_source"` | Phone switching request source | `Standard` |
| `PHONE_SWITCHING_STATUS` | `"phone_switching_status"` | The status of the phone switching process. | `@Readable` |
| `POLICY_CONTROL` | `"policy_control"` | Defines global runtime overrides to window policy. See {@link com.android.server.wm.PolicyControl} for value format. | `@Readable` |
| `REDUCE_MOTION` | `"reduce_motion"` | Whether the device has enabled the feature to reduce motion and animation (0 = false, 1 = true) | `@Readable` |
| `REMOVE_GUEST_ON_EXIT` | `"remove_guest_on_exit"` | Whether guest user should be removed on exit from guest mode. Type: int | `Standard` |
| `REPAIR_MODE_ACTIVE` | `"repair_mode_active"` | Whether repair mode is active on the device. Set to 1 for true and 0 for false. | `Standard` |
| `REVERSE_CHARGING_AUTO_ON` | `"settings_key_reverse_charging_auto_turn_on"` | Whether to auto enable reverse charging once plugged-in. | `Standard` |
| `SAFE_BOOT_DISALLOWED` | `"safe_boot_disallowed"` | Whether the safe boot is disallowed. This setting should have the identical value as the corresponding user restriction. The purpose of the setting is to make the restriction available in early boot stages before the user restrictions are loaded. | `@Readable` |
| `SELINUX_STATUS` | `"selinux_status"` | SELinux enforcement status. If 0, permissive; if 1, enforcing. | `@Readable` |
| `SELINUX_UPDATE_CONTENT_URL` | `"selinux_content_url"` | URL for selinux (mandatory access control) updates | `@Readable` |
| `SETTINGS_USE_EXTERNAL_PROVIDER_API` | `"settings_use_external_provider_api"` | Whether or not Settings should enable external provider API. {@hide} | `@Readable` |
| `SETTINGS_USE_PSD_API` | `"settings_use_psd_api"` | Whether or not Settings should enable psd API. {@hide} | `@Readable` |
| `SETUP_LOCALE` | `"setup_locale"` | The Locale (as language tag) the user chose at startup. | `@Readable` |
| `SETUP_PREPAID_DETECTION_REDIR_HOST` | `"setup_prepaid_detection_redir_host"` | Host to check for a redirect to after an attempt to GET SETUP_PREPAID_DETECTION_TARGET_URL. (If we redirected there, this is a prepaid device with zero balance.) | `@Readable` |
| `SETUP_PREPAID_DETECTION_TARGET_URL` | `"setup_prepaid_detection_target_url"` | URL to attempt a GET on to see if this is a prepay device | `@Readable` |
| `SETUP_SKIPPED` | `"setup_skipped"` | Whether the setup was skipped. | `@Readable` |
| `SHORTCUT` | `"shortcut"` | Optional shortcut character associated with this bookmark. Type: INTEGER | `Standard` |
| `SHORTCUT_MANAGER_CONSTANTS` | `"shortcut_manager_constants"` | ShortcutManager specific settings. This is encoded as a key=value list, separated by commas. Ex: "reset_interval_sec=86400,max_updates_per_interval=1" The following keys are supported: reset_interval_sec (long) max_updates_per_interval (int) max_icon_dimension_dp (int, DP) max_icon_dimension_dp_lowram (int, DP) max_shortcuts (int) icon_quality (int, 0-100) icon_format (String) Type: string | `@Readable` |
| `SHOW_NEW_NOTIF_DISMISS` | `"show_new_notif_dismiss"` | Whether to show new notification dismissal. Values are: 0: Disabled 1: Enabled | `Standard` |
| `SHOW_PEOPLE_SPACE` | `"show_people_space"` | Whether to show People Space. Values are: 0: Disabled (default) 1: Enabled | `Standard` |
| `SHOW_PROCESSES` | `"show_processes"` | Control whether the process CPU usage meter should be shown. {@link android.os.Build.VERSION_CODES#N_MR1}. | `@Readable` `@Deprecated` |
| `SHOW_TEMPERATURE_WARNING` | `"show_temperature_warning"` | Whether to show the high temperature warning notification. | `@Readable` |
| `SIDE_BUTTON` | `"side_button"` | Whether there is a side button. | `Standard` |
| `SIGNED_CONFIG_VERSION` | `"signed_config_version"` | Current version of signed configuration applied. | `@Readable` |
| `SMART_SELECTION_UPDATE_CONTENT_URL` | `"smart_selection_content_url"` | URL for smart selection model updates | `@Readable` |
| `SMS_OUTGOING_CHECK_INTERVAL_MS` | `"sms_outgoing_check_interval_ms"` | The interval in milliseconds at which to check the number of SMS sent out without asking for use permit, to limit the un-authorized SMS usage. | `@Readable` |
| `SMS_OUTGOING_CHECK_MAX_COUNT` | `"sms_outgoing_check_max_count"` | The number of outgoing SMS sent without asking for user permit (of {@link #SMS_OUTGOING_CHECK_INTERVAL_MS} | `@Readable` |
| `SMS_SHORT_CODES_UPDATE_CONTENT_URL` | `"sms_short_codes_content_url"` | URL for sms short code updates | `@Readable` |
| `SMS_SHORT_CODE_CONFIRMATION` | `"sms_short_code_confirmation"` | Used to disable SMS short code confirmation - defaults to true. True indcates we will do the check, etc. Set to false to disable. | `@Readable` |
| `SMS_SHORT_CODE_RULE` | `"sms_short_code_rule"` | Used to select which country we use to determine premium sms codes. One of com.android.internal.telephony.SMSDispatcher.PREMIUM_RULE_USE_SIM, com.android.internal.telephony.SMSDispatcher.PREMIUM_RULE_USE_NETWORK, or com.android.internal.telephony.SMSDispatcher.PREMIUM_RULE_USE_BOTH. | `@Readable` |
| `SPEED_LABEL_CACHE_EVICTION_AGE_MILLIS` | `"speed_label_cache_eviction_age_millis"` | Value to specify how long in milliseconds to retain seen score cache curves to be used when generating SSID only bases score curves. Type: long | `@Readable` `@Deprecated` |
| `SQLITE_COMPATIBILITY_WAL_FLAGS` | `"sqlite_compatibility_wal_flags"` | Configuration flags for SQLite Compatibility WAL. Encoded as a key-value list, separated by commas. E.g.: compatibility_wal_supported=true, wal_syncmode=OFF Supported keys: {@code legacy_compatibility_wal_enabled} : A {code boolean} flag that determines whether or not "compatibility WAL" mode is enabled by default. This is a legacy flag and is honoured on Android Q and higher. This flag will be removed in a future release. {@code wal_syncmode} : A {@code String} representing the synchronization mode to use when WAL is enabled, either via {@code legacy_compatibility_wal_enabled} or using the obsolete {@code compatibility_wal_supported} flag. {@code truncate_size} : A {@code int} flag that specifies the truncate size of the WAL journal. {@code compatibility_wal_supported} : A {code boolean} flag that specifies whether the legacy "compatibility WAL" mode is enabled by default. This flag is obsolete and is only supported on Android Pie. | `@Readable` |
| `STAY_ON_WHILE_PLUGGED_IN` | `"stay_on_while_plugged_in"` | Whether we keep the device on while the device is plugged in. Supported values are: {@code 0} to never stay on while plugged in {@link BatteryManager#BATTERY_PLUGGED_AC} to stay on for AC charger {@link BatteryManager#BATTERY_PLUGGED_USB} to stay on for USB charger {@link BatteryManager#BATTERY_PLUGGED_WIRELESS} to stay on for wireless charger {@link BatteryManager#BATTERY_PLUGGED_DOCK} to stay on for dock charger These values can be OR-ed together. | `@Readable` |
| `STEM_1_TYPE` | `"STEM_1_TYPE"` | No explicit description provided in source comments. | `@Readable` |
| `STEM_2_TYPE` | `"STEM_2_TYPE"` | No explicit description provided in source comments. | `@Readable` |
| `STEM_3_TYPE` | `"STEM_3_TYPE"` | No explicit description provided in source comments. | `@Readable` |
| `STEM_PRIMARY_BUTTON_LONG_PRESS` | `"stem_primary_button_long_press"` | Overrides internal R.integer.config_longPressOnStemPrimaryBehavior. Allowable values detailed in frameworks/base/core/res/res/values/config.xml. Used by PhoneWindowManager. | `Standard` |
| `STEM_PRIMARY_BUTTON_SHORT_PRESS` | `"stem_primary_button_short_press"` | Overrides internal R.integer.config_shortPressOnStemPrimaryBehavior. Allowable values detailed in frameworks/base/core/res/res/values/config.xml. Used by PhoneWindowManager. | `Standard` |
| `STEM_PRIMARY_BUTTON_TRIPLE_PRESS` | `"stem_primary_button_triple_press"` | Overrides internal R.integer.config_triplePressOnStemPrimaryBehavior. Allowable values detailed in frameworks/base/core/res/res/values/config.xml. Used by PhoneWindowManager. | `Standard` |
| `SYNC_MANAGER_CONSTANTS` | `"sync_manager_constants"` | SyncManager specific settings. Type: string | `@Readable` |
| `SYNC_MAX_RETRY_DELAY_IN_SECONDS` | `"sync_max_retry_delay_in_seconds"` | The maximum reconnect delay for short network outages or when the network is suspended due to phone use. | `@Readable` |
| `SYSTEM_CAPABILITIES` | `"system_capabilities"` | The wear system capabiltiies. | `@Readable` |
| `SYSTEM_EDITION` | `"android_wear_system_edition"` | The android wear system edition. | `@Readable` |
| `SYS_TRACED` | `"sys_traced"` | traced global setting. This controls weather the deamons: traced and traced_probes run. This links the sys.traced system property. The following values are supported: 0 -> traced and traced_probes are disabled 1 -> traced and traced_probes are enabled Any other value defaults to disabled. | `@Readable` |
| `TCP_DEFAULT_INIT_RWND` | `"tcp_default_init_rwnd"` | Used to select TCP's default initial receiver window size in segments - defaults to a build config value. | `@Readable` |
| `TEXT_CLASSIFIER_ACTION_MODEL_PARAMS` | `"text_classifier_action_model_params"` | A serialized string of params that will be loaded into a text classifier action model. | `@Readable` |
| `TEXT_CLASSIFIER_CONSTANTS` | `"text_classifier_constants"` | TextClassifier specific settings. This is encoded as a key=value list, separated by commas. String[] types like entity_list_default use ":" as delimiter for values. Ex: classify_text_max_range_length (int) detect_language_from_text_enabled (boolean) entity_list_default (String[]) entity_list_editable (String[]) entity_list_not_editable (String[]) generate_links_log_sample_rate (int) generate_links_max_text_length (int) in_app_conversation_action_types_default (String[]) lang_id_context_settings (float[]) lang_id_threshold_override (float) local_textclassifier_enabled (boolean) model_dark_launch_enabled (boolean) notification_conversation_action_types_default (String[]) smart_linkify_enabled (boolean) smart_select_animation_enabled (boolean) smart_selection_enabled (boolean) smart_text_share_enabled (boolean) suggest_selection_max_range_length (int) system_textclassifier_enabled (boolean) template_intent_factory_enabled (boolean) translate_in_classification_enabled (boolean) Type: string see also android.view.textclassifier.TextClassificationConstants | `@Readable` |
| `THEATER_MODE_ON` | `"theater_mode_on"` | Whether Theater Mode is on. {@hide} | `@Readable` `@SystemApi` |
| `TITLE` | `"title"` | Descriptive name of the bookmark that can be displayed to the user. If this is empty, the title should be resolved at display time (use {@link #getTitle(Context, Cursor)} any time you want to display the title of a bookmark.) Type: TEXT | `Standard` |
| `TZINFO_UPDATE_CONTENT_URL` | `"tzinfo_content_url"` | URL for tzinfo (time zone) updates | `@Readable` |
| `UNUSED_STATIC_SHARED_LIB_MIN_CACHE_PERIOD` | `"unused_static_shared_lib_min_cache_period"` | The min period for caching unused static shared libs in milliseconds. Type: long | `@Readable` |
| `USE_GOOGLE_MAIL` | `"use_google_mail"` | If this setting is set (to anything), then all references to Gmail on the device must change to Google Mail. | `@Readable` |
| `WARNING_TEMPERATURE` | `"warning_temperature"` | Temperature at which the high temperature warning notification should be shown. | `@Readable` |
| `WEBVIEW_MULTIPROCESS` | `"webview_multiprocess"` | Developer setting to enable WebView multiprocess rendering. | `@Readable` `@SystemApi` |
| `WEBVIEW_PROVIDER` | `"webview_provider"` | Name of the package used as WebView provider (if unset the provider is instead determined by the system). | `@Readable` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `WET_MODE_ON` | `"wet_mode_on"` | Whether the device has Wet Mode/ Touch Lock Mode enabled. | `@Readable` |
| `WRIST_ORIENTATION_MODE` | `"wear_wrist_orientation_mode"` | The wrist orientation mode of the device Valid values - LEFT_WRIST_ROTATION_0 = "0" (default), LEFT_WRIST_ROTATION_180 = "1", RIGHT_WRIST_ROTATION_0 = "2", RIGHT_WRIST_ROTATION_180 = "3" | `@Readable` |
| `WTF_IS_FATAL` | `"wtf_is_fatal"` | Nonzero causes Log.wtf() to crash. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="input-methods-voice-gestures">Input Methods, Voice & Gestures (30 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ADB_ALLOWED_CONNECTION_TIME` | `"adb_allowed_connection_time"` | When the user first connects their device to a system a prompt is displayed to allow the adb connection with an option to 'Always allow' connections from this system. If the user selects this always allow option then the connection time is stored for the system. This setting is the time in ms within which a subsequent connection from an always allowed system is allowed to reconnect without user interaction. Type: long | `@Readable` |
| `AMBIENT_PLUGGED_TIMEOUT_MIN` | `"ambient_plugged_timeout_min"` | The timeout duration in minutes of ambient mode when plugged in. | `@Readable` |
| `AMBIENT_TOUCH_TO_WAKE` | `"ambient_touch_to_wake"` | Whether ambient touch to wake is enabled. | `@Readable` |
| `APP_INTEGRITY_VERIFICATION_TIMEOUT` | `"app_integrity_verification_timeout"` | Timeout for app integrity verification. | `@Readable` |
| `APP_TIME_LIMIT_USAGE_SOURCE` | `"app_time_limit_usage_source"` | App time limit usage source setting. This controls which app in a task will be considered the source of usage when calculating app usage time limits. 1 -> task root app 2 -> current app Any other value defaults to task root app. Need to reboot the device for this setting to take effect. | `@Readable` |
| `AUTO_BEDTIME_MODE` | `"auto_bedtime_mode"` | Whether the Auto Bedtime Mode experience is enabled. | `Standard` |
| `AUTO_TIME` | `"auto_time"` | Value to specify if the device's UTC system clock should be set automatically, e.g. using telephony signals like NITZ, or other sources like GNSS or NTP. Prefer {@code android.app.time.TimeManager} API calls to determine the state of automatic time detection instead of directly observing this setting as it may be ignored by the time_detector service under various conditions. 1=yes, 0=no (manual) | `@Readable` |
| `AUTO_TIME_ZONE` | `"auto_time_zone"` | Value to specify if the device's time zone system property should be set automatically, e.g. using telephony signals like MCC and NITZ, or other mechanisms like the location. Prefer {@code android.app.time.TimeManager} API calls to determine the state of automatic time zone detection instead of directly observing this setting as it may be ignored by the time_zone_detector service under various conditions. 1=yes, 0=no (manual). | `@Readable` |
| `AUTO_TIME_ZONE_EXPLICIT` | `"auto_time_zone_explicit"` | Records whether an explicit preference for {@link #AUTO_TIME_ZONE} has been expressed instead of the current value being the default. This value is used to tell if the {@link #AUTO_TIME_ZONE} value can be influenced by experiment flags that alter the setting's value for internal testers: once the user indicates a preference they leave the experiment, only users that are still using the default will be affected by the flag. Since {@link #AUTO_TIME_ZONE} can be altered by components besides the system server, and not just via the time_zone_detector logic that sets this value, this isn't guaranteed to be set when the device diverges from the default in all cases. Important AOSP system components like SettingsUI do use the time_zone_detector APIs. 1="has been set explicitly" | `Standard` |
| `BACKUP_AGENT_TIMEOUT_PARAMETERS` | `"backup_agent_timeout_parameters"` | Backup and restore agent timeout parameters. These parameters are represented by a comma-delimited key-value list. The following strings are supported as keys: kv_backup_agent_timeout_millis (long) full_backup_agent_timeout_millis (long) shared_backup_agent_timeout_millis (long) restore_agent_timeout_millis (long) restore_agent_finished_timeout_millis (long) They map to milliseconds represented as longs. Ex: "kv_backup_agent_timeout_millis=30000,full_backup_agent_timeout_millis=300000" | `@Readable` |
| `BEDTIME_HARD_MODE` | `"bedtime_hard_mode"` | Whether hard bedtime mode is active thus limiting user interactions. | `Standard` |
| `BEDTIME_MODE` | `"bedtime_mode"` | Whether bedtime mode is enabled. | `@Readable` |
| `EMERGENCY_GESTURE_STICKY_UI_MAX_DURATION_MILLIS` | `"emergency_gesture_sticky_ui_max_duration_millis"` | The maximum duration in milliseconds for which the emergency gesture UI can stay "sticky", where the notification pull-down shade and navigation gestures/buttons are temporarily disabled. The feature is disabled completely if the value is set to zero. | `Standard` |
| `EMERGENCY_GESTURE_TAP_DETECTION_MIN_TIME_MS` | `"emergency_gesture_tap_detection_min_time_ms"` | The minimum time in milliseconds to perform the emergency gesture. | `Standard` |
| `EUICC_FACTORY_RESET_TIMEOUT_MILLIS` | `"euicc_factory_reset_timeout_millis"` | Flag to set the waiting time for euicc factory reset inside System > Settings Type: long | `@Readable` |
| `EUICC_SWITCH_SLOT_TIMEOUT_MILLIS` | `"euicc_switch_slot_timeout_millis"` | Flag to set the waiting time for euicc slot switch. Type: long | `Standard` |
| `GMS_CHECKIN_TIMEOUT_MIN` | `"gms_checkin_timeout_min"` | Gcm checkin timeout in minutes. | `Standard` |
| `MAXIMUM_OBSCURING_OPACITY_FOR_TOUCH` | `"maximum_obscuring_opacity_for_touch"` | The maximum allowed obscuring opacity by UID to propagate touches. For certain window types (eg. SAWs), the decision of honoring {@link LayoutParams #FLAG_NOT_TOUCHABLE} or not depends on the combined obscuring opacity of the windows above the touch-consuming window. For a certain UID: If it's the same as the UID of the touch-consuming window, allow it to propagate the touch. Otherwise take all its windows of eligible window types above the touch-consuming window, compute their combined obscuring opacity considering that {@code opacity(A, B) = 1 - (1 - opacity(A))*(1 - opacity(B))}. If the computed value is lesser than or equal to this setting and there are no other windows preventing the touch, allow the UID to propagate the touch. | `@Readable` |
| `MULTI_SIM_VOICE_CALL_SUBSCRIPTION` | `"multi_sim_voice_call"` | Subscription Id to be used for voice call on a multi sim device. | `@Readable` |
| `MULTI_SIM_VOICE_PROMPT` | `"multi_sim_voice_prompt"` | Used to provide option to user to select subscription during dial. The supported values are 0 = disable or 1 = enable prompt. | `@Readable` `@UnsupportedAppUsage` |
| `NTP_TIMEOUT` | `"ntp_timeout"` | SNTP client config: Timeout to wait for an NTP server response. This setting overrides the static config.xml configuration when present and valid. The value is the timeout in milliseconds. It must be > 0. | `@Readable` |
| `PACKAGE_STREAMING_VERIFIER_TIMEOUT` | `"streaming_verifier_timeout"` | Timeout for package verification during streaming installations. | `@Readable` |
| `PACKAGE_VERIFIER_TIMEOUT` | `"verifier_timeout"` | Timeout for package verification. | `@Readable` |
| `STYLUS_EVER_USED` | `"stylus_ever_used"` | Indicates whether a stylus has ever been used on the device. | `@Readable` |
| `TIME_ONLY_MODE_CONSTANTS` | `"time_only_mode_constants"` | Time Only Mode specific settings. This is encoded as a key=value list, separated by commas. Ex: "foo=1,bar=true" The following keys are supported: enabled (boolean) disable_home (boolean) disable_tilt_to_wake (boolean) disable_touch_to_wake (boolean) Type: string | `@Readable` |
| `TIME_REMAINING_ESTIMATE_BASED_ON_USAGE` | `"time_remaining_estimate_based_on_usage"` | A boolean indicating whether {@link #TIME_REMAINING_ESTIMATE_MILLIS} is customized to the device's usage or using global models. See {@link #BATTERY_ESTIMATES_LAST_UPDATE_TIME} for the last time this value was updated. | `@Readable` `@Deprecated` |
| `TIME_REMAINING_ESTIMATE_MILLIS` | `"time_remaining_estimate_millis"` | A long value indicating how much longer the system battery is estimated to last in millis. See {@link #BATTERY_ESTIMATES_LAST_UPDATE_TIME} for the last time this value was updated. | `@Readable` `@Deprecated` |
| `WATCHDOG_TIMEOUT_MILLIS` | `"system_server_watchdog_timeout_ms"` | Timeout for the system server watchdog. | `Standard` |
| `WEAR_ACTIVITY_AUTO_RESUME_TIMEOUT_MS` | `"wear_activity_auto_resume_timeout_ms"` | The maximum ambient mode duration when an activity is allowed to auto resume. | `@Readable(maxTargetSdk = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)` |
| `WEAR_ACTIVITY_AUTO_RESUME_TIMEOUT_SET_BY_USER` | `"wear_activity_auto_resume_timeout_set_by_user"` | If the current {@code WEAR_ACTIVITY_AUTO_RESUME_TIMEOUT_MS} value is set by user. 1 for true, 0 for false. | `Standard` |

</details>

<details open>
<summary><h3 style="display:inline;" id="location-positioning">Location & Positioning (8 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `DEFAULT_INSTALL_LOCATION` | `"default_install_location"` | Default install location value. 0 = auto, let system decide 1 = internal 2 = sdcard | `@Readable` |
| `GNSS_HAL_LOCATION_REQUEST_DURATION_MILLIS` | `"gnss_hal_location_request_duration_millis"` | Duration of updates in millisecond for GNSS location request from HAL to framework. If zero, the GNSS location request feature is disabled. The value is a non-negative long. | `@Readable` |
| `LOCATION_BACKGROUND_THROTTLE_INTERVAL_MS` | `"location_background_throttle_interval_ms"` | The interval in milliseconds at which location requests will be throttled when they are coming from the background. | `@Readable` |
| `LOCATION_BACKGROUND_THROTTLE_PACKAGE_WHITELIST` | `"location_background_throttle_package_whitelist"` | Packages that are allowlisted for background throttling (throttling will not be applied). | `@Readable` |
| `LOCATION_BACKGROUND_THROTTLE_PROXIMITY_ALERT_INTERVAL_MS` | `"location_background_throttle_proximity_alert_interval_ms"` | Most frequent location update interval in milliseconds that proximity alert is allowed to request. | `@Readable` |
| `LOCATION_IGNORE_SETTINGS_PACKAGE_WHITELIST` | `"location_ignore_settings_package_whitelist"` | Packages that are allowlisted for ignoring location settings (may retrieve location even when user location settings are off), for emergency purposes. | `@Readable` `@TestApi` `@Deprecated` |
| `OBTAIN_PAIRED_DEVICE_LOCATION` | `"obtain_paired_device_location"` | If FLP should obtain location data from the paired device. | `@Readable` |
| `SET_INSTALL_LOCATION` | `"set_install_location"` | Let user pick default install location. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="multi-user-accounts-profiles">Multi-User, Accounts & Profiles (8 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ALLOW_WORK_PROFILE_TELEPHONY_FOR_NON_DPM_ROLE_HOLDERS` | `"allow_work_profile_telephony_for_non_dpm_role_holders"` | Whether work profile telephony feature is enabled for non {@link android.app.role.RoleManager#ROLE_DEVICE_POLICY_MANAGEMENT} holders. ("0" = false, "1" = true). | `@Readable` |
| `CUSTOM_BUGREPORT_HANDLER_USER` | `"custom_bugreport_handler_user"` | The user id for the custom bugreport handler app. This is currently used only by Power Menu short press. instead | `@Readable` `@Deprecated` |
| `DEVICE_DEMO_MODE` | `"device_demo_mode"` | Indicates whether this device is currently in retail demo mode. If true, the device usage is severely limited. Type: int (0 for false, 1 for true) | `@Readable` `@SystemApi` |
| `KEEP_PROFILE_IN_BACKGROUND` | `"keep_profile_in_background"` | Flag to keep background restricted profiles running after exiting. If disabled, the restricted profile can be put into stopped state as soon as the user leaves it. Type: int (0 for false, 1 for true) Overridden by the system based on device information. If null, the value specified by {@code config_keepRestrictedProfilesInBackground} is used. | `@Readable` |
| `USER_HFP_CLIENT_SETTING` | `"user_hfp_client_setting"` | The user's last setting for hfp client. | `@Readable` |
| `USER_PREFERRED_REFRESH_RATE` | `"user_preferred_refresh_rate"` | The refresh rate chosen by the user. | `@Readable` `@TestApi` |
| `USER_PREFERRED_RESOLUTION_HEIGHT` | `"user_preferred_resolution_height"` | The resolution height chosen by the user. | `@Readable` `@TestApi` |
| `USER_PREFERRED_RESOLUTION_WIDTH` | `"user_preferred_resolution_width"` | The resolution width chosen by the user. | `@Readable` `@TestApi` |

</details>

<details open>
<summary><h3 style="display:inline;" id="notifications-do-not-disturb-(zen)">Notifications & Do Not Disturb (Zen) (9 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `INSTALL_CARRIER_APP_NOTIFICATION_PERSISTENT` | `"install_carrier_app_notification_persistent"` | Whether the notification should be ongoing (persistent) when a carrier app install is required. The value is a boolean (1 or 0). | `@Readable` `@SystemApi` |
| `MAX_NOTIFICATION_ENQUEUE_RATE` | `"max_notification_enqueue_rate"` | The maximum allowed notification enqueue rate in Hertz. Should be a float, and includes updates only. | `@Readable` |
| `NOTIFICATION_SNOOZE_OPTIONS` | `"notification_snooze_options"` | The list of snooze options for notifications This is encoded as a key=value list, separated by commas. Ex: "default=60,options_array=15:30:60:120" The following keys are supported: default (int) options_array (int[]) All delays in integer minutes. Array order is respected. Options will be used in order up to the maximum allowed by the UI. | `@Readable` |
| `SHOW_NOTIFICATION_CHANNEL_WARNINGS` | `"show_notification_channel_warnings"` | Displays toasts when an app posts a notification that does not specify a valid channel. The value 1 - enable, 0 - disable | `@Readable` |
| `SMART_REPLIES_IN_NOTIFICATIONS_FLAGS` | `"smart_replies_in_notifications_flags"` | Configuration flags for smart replies in notifications. This is encoded as a key=value list, separated by commas. Ex: "enabled=1,max_squeeze_remeasure_count=3" The following keys are supported: enabled (boolean) requires_targeting_p (boolean) max_squeeze_remeasure_attempts (int) edit_choices_before_sending (boolean) show_in_heads_up (boolean) min_num_system_generated_replies (int) max_num_actions (int) | `@Readable` |
| `SMART_SUGGESTIONS_IN_NOTIFICATIONS_FLAGS` | `"smart_suggestions_in_notifications_flags"` | Configuration flags for the automatic generation of smart replies and smart actions in notifications. This is encoded as a key=value list, separated by commas. Ex: "generate_replies=false,generate_actions=true". The following keys are supported: generate_replies (boolean) generate_actions (boolean) | `@Readable` |
| `ZEN_DURATION` | `"zen_duration"` | No explicit description provided in source comments. | `@Deprecated` |
| `ZEN_MODE` | `"zen_mode"` | Defines global zen mode. ZEN_MODE_OFF, ZEN_MODE_IMPORTANT_INTERRUPTIONS, or ZEN_MODE_NO_INTERRUPTIONS. | `@Readable` `@UnsupportedAppUsage` |
| `ZEN_MODE_CONFIG_ETAG` | `"zen_mode_config_etag"` | Opaque value, changes when persisted zen mode configuration changes. | `@Readable` `@UnsupportedAppUsage` |

</details>

<details open>
<summary><h3 style="display:inline;" id="power-battery-thermal">Power, Battery & Thermal (32 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ADVANCED_BATTERY_USAGE_AMOUNT` | `"advanced_battery_usage_amount"` | The usage amount of advanced battery. The value is 0~100. | `@Readable` |
| `AUTOMATIC_POWER_SAVE_MODE` | `"automatic_power_save_mode"` | Whether battery saver is currently set to trigger based on percentage, dynamic power savings trigger, or none. See {@link AutoPowerSaveModeTriggers} for accepted values. | `@Readable` `@TestApi` |
| `AVERAGE_TIME_TO_DISCHARGE` | `"average_time_to_discharge"` | A long value indicating how long the system battery takes to deplete from 100% to 0% on average based on historical drain rates. See {@link #BATTERY_ESTIMATES_LAST_UPDATE_TIME} for the last time this value was updated. | `@Readable` `@Deprecated` |
| `BATTERY_CHARGING_STATE_ENFORCE_LEVEL` | `"battery_charging_state_enforce_level"` | Threshold battery level to enforce battery state as charging. That means when battery level is equal to or higher than this threshold, it is always considered charging, even if battery level lowered. This is used as an override for constants defined in BatteryStatsImpl. Its purposes are: Ease of experimentation Customization of different device | `Standard` |
| `BATTERY_CHARGING_STATE_UPDATE_DELAY` | `"battery_charging_state_update_delay"` | Delay for sending ACTION_CHARGING after device is plugged in. This is used as an override for constants defined in BatteryStatsImpl. Its purposes are: Ease of experimentation Customization of different device | `@Readable` |
| `BATTERY_DISCHARGE_DURATION_THRESHOLD` | `"battery_discharge_duration_threshold"` | Threshold values for the duration and level of a discharge cycle, under which we log discharge cycle info. | `@Readable` |
| `BATTERY_DISCHARGE_THRESHOLD` | `"battery_discharge_threshold"` | No explicit description provided in source comments. | `@Readable` |
| `BATTERY_ESTIMATES_LAST_UPDATE_TIME` | `"battery_estimates_last_update_time"` | A long indicating the epoch time in milliseconds when {@link #TIME_REMAINING_ESTIMATE_MILLIS}, {@link #TIME_REMAINING_ESTIMATE_BASED_ON_USAGE}, and {@link #AVERAGE_TIME_TO_DISCHARGE} were last updated. | `@Readable` `@Deprecated` |
| `BATTERY_SAVER_CONSTANTS` | `"battery_saver_constants"` | Battery Saver specific settings This is encoded as a key=value list, separated by commas. Ex: "vibration_disabled=true,adjust_brightness_factor=0.5" The following keys are supported: advertise_is_enabled (boolean) datasaver_disabled (boolean) enable_night_mode (boolean) launch_boost_disabled (boolean) vibration_disabled (boolean) animation_disabled (boolean) soundtrigger_disabled (boolean) fullbackup_deferred (boolean) keyvaluebackup_deferred (boolean) firewall_disabled (boolean) gps_mode (int) adjust_brightness_disabled (boolean) adjust_brightness_factor (float) force_all_apps_standby (boolean) force_background_check (boolean) optional_sensors_disabled (boolean) aod_disabled (boolean) quick_doze_enabled (boolean) | `@Readable` `@TestApi` `@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)` |
| `BATTERY_SAVER_DEVICE_SPECIFIC_CONSTANTS` | `"battery_saver_device_specific_constants"` | Battery Saver device specific settings This is encoded as a key=value list, separated by commas. The following keys are supported: cpufreq-i (list of "core-number:frequency" pairs concatenated with /) cpufreq-n (list of "core-number:frequency" pairs concatenated with /) See {@link com.android.server.power.batterysaver.BatterySaverPolicy} for the details. | `@Readable` |
| `BATTERY_SAVER_MODE` | `"battery_saver_mode"` | The device's battery saver mode, which can be one of the following: -{@link BATTERY_SAVER_MODE_NONE} -{@link BATTERY_SAVER_MODE_LIGHT} -{@link BATTERY_SAVER_MODE_TRADITIONAL_WATCH} -{@link BATTERY_SAVER_MODE_TIME_ONLY} -{@link BATTERY_SAVER_MODE_CUSTOM} | `@Readable` |
| `BATTERY_STATS_CONSTANTS` | `"battery_stats_constants"` | BatteryStats specific settings. This is encoded as a key=value list, separated by commas. Ex: "foo=1,bar=true" The following keys are supported: track_cpu_times_by_proc_state (boolean) track_cpu_active_cluster_time (boolean) read_binary_cpu_time (boolean) proc_state_cpu_times_read_delay_ms (long) external_stats_collection_rate_limit_ms (long) battery_level_collection_delay_ms (long) max_history_files (int) max_history_buffer_kb (int) battery_charged_delay_ms (int) battery_charging_enforce_level (int) Type: string see also com.android.internal.os.BatteryStatsImpl.Constants | `@Readable` |
| `BATTERY_TIP_CONSTANTS` | `"battery_tip_constants"` | Battery tip specific settings This is encoded as a key=value list, separated by commas. Ex: "battery_tip_enabled=true,summary_enabled=true,high_usage_enabled=true," "high_usage_app_count=3,reduced_battery_enabled=false,reduced_battery_percent=50," "high_usage_battery_draining=25,high_usage_period_ms=3000" The following keys are supported: battery_tip_enabled (boolean) summary_enabled (boolean) battery_saver_tip_enabled (boolean) high_usage_enabled (boolean) high_usage_app_count (int) high_usage_period_ms (long) high_usage_battery_draining (int) app_restriction_enabled (boolean) reduced_battery_enabled (boolean) reduced_battery_percent (int) low_battery_enabled (boolean) low_battery_hour (int) | `@Readable` |
| `BUGREPORT_IN_POWER_MENU` | `"bugreport_in_power_menu"` | When the user has enable the option to have a "bug report" command in the power menu. | `@Readable` `@Deprecated` |
| `DEVICE_IDLE_CONSTANTS` | `"device_idle_constants"` | Device Idle (Doze) specific settings. This is encoded as a key=value list, separated by commas. Ex: "inactive_to=60000,sensing_to=400000" The following keys are supported: inactive_to (long) sensing_to (long) motion_inactive_to (long) idle_after_inactive_to (long) idle_pending_to (long) max_idle_pending_to (long) idle_pending_factor (float) quick_doze_delay_to (long) idle_to (long) max_idle_to (long) idle_factor (float) min_time_to_alarm (long) max_temp_app_whitelist_duration (long) notification_whitelist_duration (long) Type: string | `Standard` |
| `EMERGENCY_GESTURE_POWER_BUTTON_COOLDOWN_PERIOD_MS` | `"emergency_gesture_power_button_cooldown_period_ms"` | The power button "cooldown" period in milliseconds after the Emergency gesture is triggered, during which single-key actions on the power button are suppressed. Cooldown period is disabled if set to zero. | `Standard` |
| `EXTRA_LOW_POWER_MODE` | `"extra_low_power"` | If 1 extra low power mode is enabled. | `Standard` |
| `INSTALL_CARRIER_APP_NOTIFICATION_SLEEP_MILLIS` | `"install_carrier_app_notification_sleep_millis"` | The amount of time (ms) to hide the install carrier app notification after the user has ignored it. After this time passes, the notification will be shown again The value is a long | `@Readable` `@SystemApi` |
| `LOW_POWER_MODE` | `"low_power"` | If 1 low power mode (aka battery saver) is enabled. | `@Readable` `@TestApi` |
| `LOW_POWER_MODE_STICKY` | `"low_power_sticky"` | If 1, battery saver ({@link #LOW_POWER_MODE}) will be re-activated after the device is unplugged from a charger or rebooted. | `@Readable` `@TestApi` |
| `LOW_POWER_MODE_SUGGESTION_PARAMS` | `"low_power_mode_suggestion_params"` | See com.android.settingslib.fuelgauge.BatterySaverUtils. | `@Readable` |
| `LOW_POWER_MODE_TRIGGER_LEVEL` | `"low_power_trigger_level"` | Battery level [1-100] at which low power mode automatically turns on. If 0, it will not automatically turn on. For Q and newer, it will only automatically turn on if the value is greater than 0 and the {@link #AUTOMATIC_POWER_SAVE_MODE} setting is also set to {@link android.os.PowerManager.AutoPowerSaveMode#POWER_SAVE_MODE_TRIGGER_PERCENTAGE}. | `@Readable` |
| `LOW_POWER_MODE_TRIGGER_LEVEL_MAX` | `"low_power_trigger_level_max"` | The max value for {@link #LOW_POWER_MODE_TRIGGER_LEVEL}. If this setting is not set or the value is 0, the default max will be used. | `@Readable` |
| `LOW_POWER_STANDBY_ACTIVE_DURING_MAINTENANCE` | `"low_power_standby_active_during_maintenance"` | Setting indicating whether Low Power Standby is allowed to be active during doze maintenance mode. Values are: 0: Low Power Standby will be disabled during doze maintenance mode 1: Low Power Standby can be active during doze maintenance mode | `Standard` |
| `POWER_BUTTON_LONG_PRESS` | `"power_button_long_press"` | Overrides internal R.integer.config_longPressOnPowerBehavior. Allowable values detailed in frameworks/base/core/res/res/values/config.xml. Used by PhoneWindowManager. | `@Readable` |
| `POWER_BUTTON_LONG_PRESS_DURATION_MS` | `"power_button_long_press_duration_ms"` | Override internal R.integer.config_longPressOnPowerDurationMs. It determines the length of power button press to be considered a long press in milliseconds. Used by PhoneWindowManager. | `@Readable` |
| `POWER_BUTTON_SHORT_PRESS` | `"power_button_short_press"` | Overrides internal R.integer.config_shortPressOnPowerBehavior. Allowable values detailed in frameworks/base/core/res/res/values/config.xml. Used by PhoneWindowManager. | `Standard` |
| `POWER_BUTTON_SUPPRESSION_DELAY_AFTER_GESTURE_WAKE` | `"power_button_suppression_delay_after_gesture_wake"` | The amount of time to suppress "power-off" from the power button after the device has woken due to a gesture (lifting the phone). Since users have learned to hit the power button immediately when lifting their device, it can cause the device to turn off if a gesture has just woken the device. This value tells us the milliseconds to wait after a gesture before "power-off" via power-button is functional again. A value of 0 is no delay, and reverts to the old behavior. | `@Readable` |
| `POWER_BUTTON_TRIPLE_PRESS` | `"power_button_triple_press"` | Overrides internal R.integer.config_triplePressOnPowerBehavior. Allowable values detailed in frameworks/base/core/res/res/values/config.xml. Used by PhoneWindowManager. | `Standard` |
| `POWER_BUTTON_VERY_LONG_PRESS` | `"power_button_very_long_press"` | Overrides internal R.integer.config_veryLongPressOnPowerBehavior. Allowable values detailed in frameworks/base/core/res/res/values/config.xml. Used by PhoneWindowManager. | `@Readable` |
| `POWER_MANAGER_CONSTANTS` | `"power_manager_constants"` | Power manager specific settings. This is encoded as a key=value list, separated by commas. Ex: "no_cached_wake_locks=1" The following keys are supported: no_cached_wake_locks (boolean) Type: string | `@Readable` |
| `SYS_UIDCPUPOWER` | `"sys_uidcpupower"` | UidCpuPower global setting. This links the sys.uidcpupower system property. The following values are supported: 0 -> /proc/uid_cpupower/* are disabled 1 -> /proc/uid_cpupower/* are enabled Any other value defaults to enabled. | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="security-privacy-lockscreen">Security, Privacy & Lockscreen (18 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION` | `"android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION"` | Activity Action: Show screen for controlling which apps have access to manage external storage. In some cases, a matching Activity may not exist, so ensure you safeguard against this. If you want to control a specific app's access to manage external storage, use {@link #ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION} instead. Output: Nothing. | `@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)` |
| `ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION` | `"android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION"` | Activity Action: Show screen for controlling if the app specified in the data URI of the intent can manage external storage. Launching the corresponding activity requires the permission {@link Manifest.permission#MANAGE_EXTERNAL_STORAGE}. In some cases, a matching Activity may not exist, so ensure you safeguard against this. Input: The Intent's data URI MUST specify the application package name whose ability of managing external storage you want to control. For example "package:com.my.app". Output: Nothing. | `@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)` |
| `ADD_USERS_WHEN_LOCKED` | `"add_users_when_locked"` | Whether users are allowed to add more users or guest from lockscreen. Type: int | `@Readable` |
| `ALLOW_USER_SWITCHING_WHEN_SYSTEM_USER_LOCKED` | `"allow_user_switching_when_system_user_locked"` | Allows switching users when system user is locked. Type: int | `@Readable` |
| `BLOCKED_SLICES` | `"blocked_slices"` | A colon separated list of keys for Settings Slices. | `@Readable` |
| `BLOCKING_HELPER_DISMISS_TO_VIEW_RATIO_LIMIT` | `"blocking_helper_dismiss_to_view_ratio"` | Settings key for the ratio of notification dismissals to notification views - one of the criteria for showing the notification blocking helper. The value is a float ranging from 0.0 to 1.0 (the closer to 0.0, the more intrusive the blocking helper will be). | `@Readable` |
| `BLOCKING_HELPER_STREAK_LIMIT` | `"blocking_helper_streak_limit"` | Settings key for the longest streak of dismissals - one of the criteria for showing the notification blocking helper. The value is an integer greater than 0. | `@Readable` |
| `CERT_PIN_UPDATE_CONTENT_URL` | `"cert_pin_content_url"` | URL for cert pinlist updates | `@Readable` |
| `CLOCKWORK_24HR_TIME` | `"clockwork_24hr_time"` | Whether 24 hour time format is enabled on the watch. | `@Readable` |
| `CLOCKWORK_AUTO_TIME` | `"clockwork_auto_time"` | Whether automatic time is enabled on the watch. | `@Readable` |
| `CLOCKWORK_AUTO_TIME_ZONE` | `"clockwork_auto_time_zone"` | Whether automatic time zone is enabled on the watch. | `@Readable` |
| `CLOCKWORK_HOME_READY` | `"clockwork_home_ready"` | Setting to determine if the Clockwork Home application is ready. Set to 1 when the Clockwork Home application has finished starting up. | `Standard` |
| `CLOCKWORK_SYSUI_MAIN_ACTIVITY` | `"clockwork_sysui_main_activity"` | Setting indicating the name of the main activity of the Wear OS sysui. | `Standard` |
| `CLOCKWORK_SYSUI_PACKAGE` | `"clockwork_sysui_package"` | Setting indicating the name of the Wear OS app package containing the device's sysui. | `@Readable` |
| `ONE_HANDED_KEYGUARD_SIDE` | `"one_handed_keyguard_side"` | In one handed mode, which side the keyguard should be on. Allowable values are one of the ONE_HANDED_KEYGUARD_SIDE_* constants. | `Standard` |
| `REQUIRE_PASSWORD_TO_DECRYPT` | `"require_password_to_decrypt"` | On devices that use full-disk encryption, indicates whether the primary user's lockscreen credential is required to decrypt the device on boot. This setting does not do anything on devices that use file-based encryption. With file-based encryption, the device boots without a credential being needed, but the lockscreen credential is required to unlock credential-encrypted storage. All devices that launched with Android 10 or higher use file-based encryption. Type: int (0 for false, 1 for true) | `@Readable` `@SystemApi` |
| `REVIEW_PERMISSIONS_NOTIFICATION_STATE` | `"review_permissions_notification_state"` | State of whether review notification permissions notification needs to be shown the user, and whether the user has interacted. Valid values: -1 = UNKNOWN 0 = SHOULD_SHOW 1 = USER_INTERACTED 2 = DISMISSED 3 = RESHOWN | `Standard` |
| `SECURE_FRP_MODE` | `"secure_frp_mode"` | Indicates whether the device is under restricted secure FRP mode. Secure FRP mode is enabled when the device is under FRP. On solving of FRP challenge, device is removed from this mode. Type: int (0 for false, 1 for true) | `@Readable` |

</details>

<details open>
<summary><h3 style="display:inline;" id="wearables-wear-os">Wearables & Wear OS (11 settings)</h3></summary>
<br/>

| Constant Name | Setting Key (`Settings.get*()`) | Description / Javadoc | Badges & Annotations |
| :--- | :--- | :--- | :--- |
| `ANDROID_WEAR_VERSION` | `"android_wear_version"` | The android wear system version. | `@Readable` |
| `PDP_WATCHDOG_ERROR_POLL_COUNT` | `"pdp_watchdog_error_poll_count"` | The number of polls to perform (at {@link #PDP_WATCHDOG_ERROR_POLL_INTERVAL_MS}) after hitting {@link #PDP_WATCHDOG_TRIGGER_PACKET_COUNT} before attempting data connection recovery. | `@Readable` |
| `PDP_WATCHDOG_ERROR_POLL_INTERVAL_MS` | `"pdp_watchdog_error_poll_interval_ms"` | The interval in milliseconds at which to check packet counts on the mobile data interface after {@link #PDP_WATCHDOG_TRIGGER_PACKET_COUNT} outgoing packets has been reached without incoming packets. | `@Readable` |
| `PDP_WATCHDOG_LONG_POLL_INTERVAL_MS` | `"pdp_watchdog_long_poll_interval_ms"` | The interval in milliseconds at which to check packet counts on the mobile data interface when screen is off, to detect possible data connection problems. | `@Readable` |
| `PDP_WATCHDOG_MAX_PDP_RESET_FAIL_COUNT` | `"pdp_watchdog_max_pdp_reset_fail_count"` | The number of failed PDP reset attempts before moving to something more drastic: re-registering to the network. | `@Readable` |
| `PDP_WATCHDOG_POLL_INTERVAL_MS` | `"pdp_watchdog_poll_interval_ms"` | The interval in milliseconds at which to check packet counts on the mobile data interface when screen is on, to detect possible data connection problems. | `@Readable` |
| `PDP_WATCHDOG_TRIGGER_PACKET_COUNT` | `"pdp_watchdog_trigger_packet_count"` | The number of outgoing packets sent without seeing an incoming packet that triggers a countdown (of {@link #PDP_WATCHDOG_ERROR_POLL_COUNT} device is logged to the event log | `@Readable` |
| `WEAR_LAUNCHER_UI_MODE` | `"wear_launcher_ui_mode"` | No explicit description provided in source comments. | `@Readable` |
| `WEAR_OS_VERSION_STRING` | `"wear_os_version_string"` | Wear OS version string. | `@Readable` |
| `WEAR_PLATFORM_MR_NUMBER` | `"wear_platform_mr_number"` | The Wear platform MR number. | `@Readable` |
| `WEAR_SYSTEM_STATUS_TRAY_CONFIGURATION` | `"wear_system_status_tray_configuration"` | Configuration of system status tray in wear. | `Standard` |

</details>

---


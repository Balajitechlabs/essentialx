package com.sameerasw.essentials.appfunctions.dto

import androidx.annotation.Keep
import androidx.appfunctions.AppFunctionSerializable

/**
 * Current overall status of device and key Essentials features.
 *
 * @param batteryLevel Battery percentage 0-100.
 * @param isCharging True if plugged in and charging.
 * @param soundMode Current sound mode: SOUND, VIBRATE, or SILENT.
 * @param isCaffeinateActive True if caffeinate display awake mode is active.
 * @param isFlashlightOn True if camera torch is on.
 * @param isAodEnabled True if Always On Display is enabled.
 * @param isNotificationLightingEnabled True if edge lighting for notifications is enabled.
 */
@Keep
@AppFunctionSerializable(isDescribedByKDoc = true)
data class DeviceStatusResponse(
    val batteryLevel: Int,
    val isCharging: Boolean,
    val soundMode: String,
    val isCaffeinateActive: Boolean,
    val isFlashlightOn: Boolean,
    val isAodEnabled: Boolean,
    val isNotificationLightingEnabled: Boolean
)

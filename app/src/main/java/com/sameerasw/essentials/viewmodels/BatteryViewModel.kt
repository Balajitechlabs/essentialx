/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Battery & Power Management
 * File: BatteryViewModel.kt
 * Description: ViewModel managing state for battery statistics, charging history,
 * ring drawer overlays, and power saving notification toggles.
 */

package com.sameerasw.essentials.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.utils.battery.BatteryInfoUtil

class BatteryViewModel : ViewModel() {
    val isBatteryNotificationEnabled = mutableStateOf(false)
    val isMapsPowerSavingEnabled = mutableStateOf(false)
    val isChargeQuickTileActive = mutableStateOf(false)
    val batteryPercentage = mutableStateOf(0)
    val isCharging = mutableStateOf(false)

    /**
     * Initializes battery settings and updates state.
     *
     * @param context [Context] Application context used for checking battery info and settings.
     */
    fun loadBatteryState(context: Context) {
        val repo = SettingsRepository(context)
        isBatteryNotificationEnabled.value = repo.isBatteryNotificationEnabled()
        isMapsPowerSavingEnabled.value = repo.getBoolean("maps_power_saving_enabled", false)
        
        val details = BatteryInfoUtil.getBasicDetails(context)
        batteryPercentage.value = details.level
        isCharging.value = details.plugged != 0 || details.status == android.os.BatteryManager.BATTERY_STATUS_CHARGING
    }

    /**
     * Updates power saving toggle for Maps navigation.
     *
     * @param context [Context] Application context for persistence.
     * @param enabled [Boolean] True if maps power saving mode should be active.
     */
    fun setMapsPowerSavingEnabled(context: Context, enabled: Boolean) {
        isMapsPowerSavingEnabled.value = enabled
        SettingsRepository(context).putBoolean("maps_power_saving_enabled", enabled)
    }
}

/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Quick Settings Tiles
 * File: QuickSettingsTilesViewModel.kt
 * Description: Manages QS tile status synchronization, tile action dispatching,
 * and user tile customization preferences.
 */

package com.sameerasw.essentials.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sameerasw.essentials.data.repository.SettingsRepository

class QuickSettingsTilesViewModel : ViewModel() {
    val isCaffeinateTileActive = mutableStateOf(false)
    val isFlashlightTileActive = mutableStateOf(false)
    val isAlwaysOnDisplayTileActive = mutableStateOf(false)
    val isAdaptiveBrightnessTileActive = mutableStateOf(false)
    val isDeveloperOptionsTileActive = mutableStateOf(false)

    /**
     * Synchronizes Quick Settings tile state values with system setting values.
     *
     * @param context [Context] Application context used for checking system tile states.
     */
    fun syncTileStates(context: Context) {
        val repo = SettingsRepository(context)
        isCaffeinateTileActive.value = repo.getBoolean("caffeinate_active", false)
        isAlwaysOnDisplayTileActive.value = repo.getBoolean("aod_tile_active", false)
    }
}

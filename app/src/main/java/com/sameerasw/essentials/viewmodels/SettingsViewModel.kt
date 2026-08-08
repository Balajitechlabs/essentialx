/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: App Settings & Configuration
 * File: SettingsViewModel.kt
 * Description: Manages user preferences persistence, default tabs, pinned features,
 * and app-wide UI customization settings.
 */

package com.sameerasw.essentials.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.domain.DIYTabs
import com.sameerasw.essentials.domain.HapticFeedbackType

class SettingsViewModel : ViewModel() {
    val pinnedFeatureKeys = mutableStateOf<List<String>>(emptyList())
    val pinnedQsTileKeys = mutableStateOf<List<String>>(emptyList())
    val hapticFeedbackType = mutableStateOf(HapticFeedbackType.SUBTLE)
    val defaultTab = mutableStateOf(DIYTabs.ESSENTIALS)

    /**
     * Loads saved pinned features and user settings from repository.
     *
     * @param context [Context] Application context for shared preferences access.
     */
    fun loadSettings(context: Context) {
        val repo = SettingsRepository(context)
        pinnedFeatureKeys.value = repo.getPinnedFeatures()
        pinnedQsTileKeys.value = repo.getPinnedQsTiles()
    }

    /**
     * Toggles a pinned feature key.
     *
     * @param context [Context] Application context for persistence.
     * @param key [String] Unique key of the feature card.
     */
    fun togglePinFeature(context: Context, key: String) {
        val repo = SettingsRepository(context)
        val current = pinnedFeatureKeys.value.toMutableList()
        if (current.contains(key)) {
            current.remove(key)
        } else {
            current.add(key)
        }
        repo.savePinnedFeatures(current)
        pinnedFeatureKeys.value = current
    }
}

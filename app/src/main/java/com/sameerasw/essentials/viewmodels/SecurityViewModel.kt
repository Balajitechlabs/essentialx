/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Security & Device Admin
 * File: SecurityViewModel.kt
 * Description: Handles state management for AppLock, RemoteLock, Biometric security,
 * and lock screen security features.
 */

package com.sameerasw.essentials.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sameerasw.essentials.data.repository.SettingsRepository

class SecurityViewModel : ViewModel() {
    val isAppLockEnabled = mutableStateOf(false)
    val isRemoteLockEnabled = mutableStateOf(false)
    val isScreenLockedSecurityEnabled = mutableStateOf(false)
    val isBiometricsRequired = mutableStateOf(false)

    /**
     * Initializes security toggles and states from persistent repository.
     *
     * @param context [Context] Application context used to retrieve settings.
     */
    fun loadSecurityState(context: Context) {
        val repo = SettingsRepository(context)
        isAppLockEnabled.value = repo.getBoolean(SettingsRepository.KEY_APP_LOCK_ENABLED, false)
        isRemoteLockEnabled.value = repo.getBoolean("remote_lock_enabled", false)
        isScreenLockedSecurityEnabled.value = repo.getBoolean("screen_locked_security_enabled", false)
    }

    /**
     * Sets AppLock active state.
     *
     * @param context [Context] Application context for persistence.
     * @param enabled [Boolean] True to enable AppLock protection.
     */
    fun setAppLockEnabled(context: Context, enabled: Boolean) {
        isAppLockEnabled.value = enabled
        SettingsRepository(context).putBoolean(SettingsRepository.KEY_APP_LOCK_ENABLED, enabled)
    }
}

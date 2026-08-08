/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Appfunctions
 * File: AppFunctionAvailabilityManager.kt
 * Description: Component file for AppFunctionAvailabilityManager.kt.
 */

package com.sameerasw.essentials.appfunctions

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appfunctions.AppFunctionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AppFunctionAvailabilityManager {
    private const val TAG = "AppFunctionAvailabilityManager"

    fun updateAvailability(context: Context) {
        if (Build.VERSION.SDK_INT < 36) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val appFunctionManager = AppFunctionManager.getInstance(context) ?: return@launch
                Log.d(TAG, "AppFunctionManager initialized successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating AppFunction availability", e)
            }
        }
    }
}

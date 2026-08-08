/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Appfunctions
 * File: CreateAutomationParams.kt
 * Description: Component file for CreateAutomationParams.kt.
 */

package com.sameerasw.essentials.appfunctions.dto

import androidx.annotation.Keep
import androidx.appfunctions.AppFunctionSerializable

/**
 * Parameters to create a new DIY automation.
 *
 * @param title Descriptive name for the automation.
 * @param type Rule type: TRIGGER, STATE, or APP.
 * @param triggerType Trigger name (ScreenOff, ScreenOn, DeviceUnlock, ChargerConnected, ChargerDisconnected, Schedule).
 * @param stateType State name (Charging, ScreenOn, TimePeriod).
 * @param actionType Action to perform (TurnOnFlashlight, TurnOffFlashlight, ToggleFlashlight, DimWallpaper, SoundMode, TurnOnLowPower, TurnOffLowPower, ScreenOff, TakeScreenshot, FreezeTag).
 * @param hour Hour 0-23 for schedule or time period.
 * @param minute Minute 0-59 for schedule or time period.
 * @param endHour End hour 0-23 for time period state.
 * @param endMinute End minute 0-59 for time period state.
 * @param soundMode Sound mode name: SOUND, VIBRATE, SILENT.
 * @param dimWallpaperAmount Wallpaper dim level fraction 0.0 to 1.0.
 * @param freezeTagId ID of freeze tag for FreezeTag action.
 * @param freezeMode Mode for FreezeTag action: Freeze or Unfreeze.
 */
@Keep
@AppFunctionSerializable(isDescribedByKDoc = true)
data class CreateAutomationParams(
    val title: String,
    val type: String,
    val triggerType: String,
    val stateType: String,
    val actionType: String,
    val hour: Int,
    val minute: Int,
    val endHour: Int,
    val endMinute: Int,
    val soundMode: String,
    val dimWallpaperAmount: Float,
    val freezeTagId: String,
    val freezeMode: String
)

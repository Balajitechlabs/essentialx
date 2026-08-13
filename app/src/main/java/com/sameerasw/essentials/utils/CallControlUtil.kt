/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Utilities
 * File: CallControlUtil.kt
 * Description: Utility functions to programmatically answer, end, and mute phone calls.
 */

package com.sameerasw.essentials.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.telecom.TelecomManager
import android.util.Log
import android.view.KeyEvent
import androidx.core.content.ContextCompat

object CallControlUtil {
    private const val TAG = "CallControlUtil"

    fun acceptCall(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ANSWER_PHONE_CALLS
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                try {
                    val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as? TelecomManager
                    if (telecomManager != null) {
                        Log.d(TAG, "Accepting call via TelecomManager")
                        telecomManager.acceptRingingCall()
                        return
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to accept call via TelecomManager", e)
                }
            }
        }
        emulateHeadsetHookClick(context)
    }

    fun endCall(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ANSWER_PHONE_CALLS
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                try {
                    val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as? TelecomManager
                    if (telecomManager != null) {
                        Log.d(TAG, "Ending call via TelecomManager")
                        val success = telecomManager.endCall()
                        if (success) return
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to end call via TelecomManager", e)
                }
            }
        }
        emulateHeadsetHookClick(context)
    }

    fun toggleMute(context: Context): Boolean {
        return try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            if (audioManager != null) {
                val currentMute = audioManager.isMicrophoneMute
                val newMute = !currentMute
                audioManager.isMicrophoneMute = newMute
                Log.d(TAG, "Toggled microphone mute: $newMute")
                newMute
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error toggling microphone mute", e)
            false
        }
    }

    private fun emulateHeadsetHookClick(context: Context) {
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            if (audioManager != null) {
                val downEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK)
                val upEvent = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK)
                audioManager.dispatchMediaKeyEvent(downEvent)
                audioManager.dispatchMediaKeyEvent(upEvent)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error emulating headset hook click", e)
        }
    }
}

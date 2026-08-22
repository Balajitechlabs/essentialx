/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Background Services & Receivers
 * File: UsbDebuggingTileService.kt
 * Description: Quick Settings Tile Service for managing USB and Wireless (WiFi) debugging state.
 */

package com.sameerasw.essentials.services.tiles

import android.content.Intent
import android.graphics.drawable.Icon
import android.provider.Settings
import android.service.quicksettings.Tile
import android.util.Log
import android.widget.Toast
import com.sameerasw.essentials.FeatureSettingsActivity
import com.sameerasw.essentials.R
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.utils.PermissionUtils

class UsbDebuggingTileService : BaseTileService() {
    override fun onClick() {
        if (!hasFeaturePermission()) {
            val intent =
                Intent(this, FeatureSettingsActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("feature", "Quick settings tiles")
                }
            startActivityAndCollapse(intent)
            return
        }
        super.onClick()
    }

    override fun getTileLabel(): String {
        val strRef = when (getDefaultAction()) {
            "usb" -> R.string.usb_debugging_title_short
            "wireless" -> R.string.wireless_debugging_title_short
            else -> R.string.tile_usb_debugging
        }
        return getString(strRef)
    }

    override fun getTileSubtitle(): String {
        val usbOn = isUsbDebuggingEnabled()
        val wifiOn = isWifiDebuggingEnabled()
        val strRes = when (getDefaultAction()) {
            "usb" -> if (usbOn) R.string.on else R.string.off
            "wireless" -> if (wifiOn) R.string.on else R.string.off
            else -> when {
                usbOn && wifiOn -> R.string.usb_and_wifi
                usbOn -> R.string.usb
                wifiOn -> R.string.wifi
                else -> R.string.off
            }
        }
        return getString(strRes)
    }

    override fun hasFeaturePermission(): Boolean = PermissionUtils.canWriteSecureSettings(this)

    override fun getTileIcon(): Icon {
        val icon = when (getDefaultAction()) {
            "usb" -> R.drawable.usb_debugging_24
            "wireless" -> R.drawable.wireless_debugging_24
            else -> R.drawable.rounded_adb_24
        }
        return Icon.createWithResource(this, icon)
    }

    override fun getTileState(): Int {
        val usbOn = isUsbDebuggingEnabled()
        val wifiOn = isWifiDebuggingEnabled()

        return when (getDefaultAction()) {
            "usb" -> if (usbOn) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            "wireless" -> if (wifiOn) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            else -> if (usbOn && wifiOn) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        }
    }

    override fun onTileClick() {
        val usbOn = isUsbDebuggingEnabled()
        val wifiOn = isWifiDebuggingEnabled()

        when (getDefaultAction()) {
            "usb" -> {
                setUsbDebuggingEnabled(!usbOn)
            }

            "wireless" -> {
                setWifiDebuggingEnabled(!wifiOn)
            }

            else -> {
                val newState = if (usbOn && wifiOn) 0 else 1
                setUsbDebuggingEnabled(newState == 1)
                setWifiDebuggingEnabled(newState == 1)
            }
        }
    }

    private fun getDefaultAction(): String {
        val prefs = getSharedPreferences("essentials_prefs", MODE_PRIVATE)
        return prefs.getString("debugging_tile_tap_action", "both") ?: "both"
    }

    private fun isUsbDebuggingEnabled(): Boolean =
        try {
            Settings.Global.getInt(contentResolver, Settings.Global.ADB_ENABLED, 0) == 1
        } catch (_: Exception) {
            false
        }

    private fun setUsbDebuggingEnabled(enabled: Boolean) {
        try {
            if(!enabled) toggleShizuku(false)
            Settings.Global.putInt(
                contentResolver,
                Settings.Global.ADB_ENABLED,
                if (enabled) 1 else 0
            )
            if(enabled) toggleShizuku(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isWifiDebuggingEnabled(): Boolean =
        try {
            Settings.Global.getInt(contentResolver, "adb_wifi_enabled", 0) == 1
        } catch (_: Exception) {
            false
        }

    private fun setWifiDebuggingEnabled(enabled: Boolean) {
        try {
            Settings.Global.putInt(contentResolver, "adb_wifi_enabled", if (enabled) 1 else 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun toggleShizuku(enabled: Boolean) {
        val token = SettingsRepository(this).getShizukuAuthToken()
        val action =
            if (enabled) "moe.shizuku.privileged.api.START" else "moe.shizuku.privileged.api.STOP"

        if (token.isEmpty()) {
            Toast.makeText(
                this,
                this.getString(R.string.toast_enter_shizuku_token),
                Toast.LENGTH_LONG
            ).show()
        } else {
            try {
                val shizukuIntent = Intent(action).apply {
                    `package` = "moe.shizuku.privileged.api"
                    putExtra("auth", token)
                    addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                }
                this.sendBroadcast(shizukuIntent)
            } catch (e: Exception) {
                Log.e("ShizukuActionReceiver", "Failed to toggle Shizuku", e)
            }
        }
    }
}

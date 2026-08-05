package com.sameerasw.essentials.services.tiles

import android.accessibilityservice.AccessibilityService
import android.app.admin.DevicePolicyManager
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.widget.Toast
import com.sameerasw.essentials.R
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.domain.ScreenOffMethod
import com.sameerasw.essentials.services.receivers.SecurityDeviceAdminReceiver
import com.sameerasw.essentials.utils.PermissionUtils

class LockdownTileService : BaseTileService() {
    override fun onTileClick() {
        val action =
            if (lockdownModeStatus()) ScreenOffMethod.DEVICE_ADMIN else ScreenOffMethod.ACCESSIBILITY
        lockdownAction(action)
    }

    override fun getTileLabel(): String = getString(R.string.tile_lockdown_mode)

    override fun getTileSubtitle(): String {
        return if (getTileState() == Tile.STATE_ACTIVE) getString(R.string.tile_active) else getString(
            R.string.tile_inactive
        )
    }

    override fun hasFeaturePermission(): Boolean {
//        val status = isAccessibilityGranted() || isDeviceAdmin()
        return true // i don't want to disable tile.
    }

    override fun getTileIcon(): Icon = Icon.createWithResource(this, R.drawable.rounded_lock_24)

    override fun getTileState(): Int {
        return if (lockdownModeStatus() && isDeviceAdmin()) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
    }

    private fun lockdownModeStatus(): Boolean {
        return getSharedPreferences("essentials_prefs", MODE_PRIVATE)
            .getBoolean(SettingsRepository.KEY_LOCKDOWN_MODE, false)
    }

    private fun getDevicePolicyManager(): DevicePolicyManager {
        return getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    private fun isDeviceAdmin(): Boolean {
        val adminComponent =
            android.content.ComponentName(this, SecurityDeviceAdminReceiver::class.java)
        return getDevicePolicyManager().isAdminActive(adminComponent)
    }

    private fun isAccessibilityGranted(): Boolean {
        return PermissionUtils.isAccessibilityServiceEnabled(this)
    }

    private fun lockdownAction(action: ScreenOffMethod) {
        when (action) {
            ScreenOffMethod.DEVICE_ADMIN -> {
                if (!isDeviceAdmin()) {
                    Toast.makeText(
                        this,
                        "Please make device admin to lockdown this device.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                getDevicePolicyManager().lockNow()
            }

            ScreenOffMethod.ACCESSIBILITY -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    Toast.makeText(
                        this,
                        "Feature not implemented for your device!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (!isAccessibilityGranted()) {
                    Toast.makeText(this, "Missing Accessibility Permission!", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                ScreenOffAccessibilityService.instance?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN)
            }

            else -> {}
        }
    }
}
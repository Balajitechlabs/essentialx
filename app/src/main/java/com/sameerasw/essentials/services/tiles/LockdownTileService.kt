package com.sameerasw.essentials.services.tiles

import android.accessibilityservice.AccessibilityService
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.view.KeyEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.sameerasw.essentials.R
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.utils.PermissionUtils
import com.sameerasw.essentials.utils.ShellUtils

class LockdownTileService : BaseTileService() {
    @RequiresApi(Build.VERSION_CODES.P)
    @Suppress("StartActivityAndCollapseDeprecated")
    override fun onTileClick() {
        if (ShellUtils.hasPermission(this)) {
            if (lockdownModeStatus()) {
                ShellUtils.runCommand(
                    this,
                    "cmd lock_settings require-strong-auth STRONG_AUTH_REQUIRED_AFTER_USER_LOCKDOWN --user 0"
                )
            } else {
                ShellUtils.runCommand(this, "input keyevent ${KeyEvent.KEYCODE_POWER}")
            }
            return
        }

        if (PermissionUtils.isAccessibilityServiceEnabled(this)) {
            ScreenOffAccessibilityService.instance?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN)
            return
        }
        
        Toast.makeText(
            this,
            "Missing Accessibility/Shizuku permission",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun getTileLabel(): String = getString(R.string.tile_lockdown_mode)

    override fun getTileSubtitle(): String {
        return if (getTileState() == Tile.STATE_ACTIVE) getString(R.string.tile_active) else getString(
            R.string.tile_inactive
        )
    }

    override fun hasFeaturePermission(): Boolean {
        val status =
            PermissionUtils.isAccessibilityServiceEnabled(this) || ShellUtils.hasPermission(this)
        return status
    }

    override fun getTileIcon(): Icon = Icon.createWithResource(this, R.drawable.rounded_lock_24)

    override fun getTileState(): Int {
        return if (lockdownModeStatus() && ShellUtils.hasPermission(this)) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
    }

    private fun lockdownModeStatus(): Boolean {
        return getSharedPreferences("essentials_prefs", MODE_PRIVATE)
            .getBoolean(SettingsRepository.KEY_LOCKDOWN_MODE, false)
    }
}
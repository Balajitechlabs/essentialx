package com.sameerasw.essentials.services.tiles

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import com.sameerasw.essentials.R
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.domain.ScreenOffMethod
import com.sameerasw.essentials.utils.DeviceLockUtils
import com.sameerasw.essentials.utils.PermissionUtils

class LockdownTileService : BaseTileService() {
    override fun onTileClick() {
        val action =
            if (lockdownModeStatus()) ScreenOffMethod.DEVICE_ADMIN else ScreenOffMethod.ACCESSIBILITY
        DeviceLockUtils.lockDevice(this, action)
    }

    override fun getTileLabel(): String = getString(R.string.tile_lockdown_mode)

    override fun getTileSubtitle(): String {
        return if (getTileState() == Tile.STATE_ACTIVE) getString(R.string.tile_active) else getString(
            R.string.tile_inactive
        )
    }

    override fun hasFeaturePermission(): Boolean {
        return true
    }

    override fun getTileIcon(): Icon = Icon.createWithResource(this, R.drawable.rounded_lock_24)

    override fun getTileState(): Int {
        return if (lockdownModeStatus() && PermissionUtils.isDeviceAdminActive(this)) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
    }

    private fun lockdownModeStatus(): Boolean {
        return SettingsRepository(this).getBoolean(SettingsRepository.KEY_LOCKDOWN_MODE, false)
    }
}
package com.sameerasw.essentials.services.tiles

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import com.sameerasw.essentials.R
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.utils.DeviceLockUtils
import com.sameerasw.essentials.utils.HapticUtil

class LockdownTileService : BaseTileService() {

    override fun onClick() {
        if (!isDeviceSupported() && !areUnsupportedFeaturesEnabled()) return
        HapticUtil.performHapticForService(this)
        DeviceLockUtils.performLockdownTileAction(this, isLongPress = false)
    }

    override fun onTileClick() {
        DeviceLockUtils.performLockdownTileAction(this, isLongPress = false)
    }

    override fun getTileLabel(): String {
        return if (lockdownModeStatus()) getString(R.string.tile_lockdown_mode) else getString(R.string.tile_lock)
    }

    override fun getTileSubtitle(): String = getString(R.string.tile_device)

    override fun hasFeaturePermission(): Boolean = true

    override fun getTileIcon(): Icon {
        val iconRes =
            if (lockdownModeStatus()) R.drawable.rounded_shield_lock_24 else R.drawable.rounded_lock_24
        return Icon.createWithResource(this, iconRes)
    }

    override fun getTileState(): Int = Tile.STATE_INACTIVE

    private fun lockdownModeStatus(): Boolean {
        return SettingsRepository(this).getBoolean(SettingsRepository.KEY_LOCKDOWN_MODE, false)
    }
}
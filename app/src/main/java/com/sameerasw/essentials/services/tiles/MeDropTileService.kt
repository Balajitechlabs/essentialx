/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Background Services & Receivers
 * File: MeDropTileService.kt
 */

package com.sameerasw.essentials.services.tiles

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import com.sameerasw.essentials.R
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.domain.model.MeDropSettings
import com.sameerasw.essentials.ui.activities.MeDropActivity

class MeDropTileService : BaseTileService() {

    override fun getTileLabel(): String = getString(R.string.feat_medrop_title)

    override fun getTileSubtitle(): String {
        val settingsJson = SettingsRepository(this).getMeDropSettingsJson()
        return if (settingsJson == null) {
            getString(R.string.feat_medrop_set_up)
        } else {
            try {
                val settings = com.google.gson.Gson().fromJson(settingsJson, MeDropSettings::class.java)
                settings.contact?.displayName ?: getString(R.string.feat_medrop_set_up)
            } catch (_: Exception) {
                ""
            }
        }
    }

    override fun getTileState(): Int {
        val settingsJson = SettingsRepository(this).getMeDropSettingsJson()
        if (settingsJson != null) {
            try {
                val settings = com.google.gson.Gson().fromJson(settingsJson, MeDropSettings::class.java)
                if (settings.contact != null) return Tile.STATE_ACTIVE
            } catch (_: Exception) {}
        }
        return Tile.STATE_INACTIVE
    }

    override fun hasFeaturePermission(): Boolean = true

    override fun onTileClick() {
        val repo = SettingsRepository(this)
        val allowWhenLocked = repo.isMeDropAllowWhenLocked()
        
        val launchAction = {
            val intent = Intent(this, MeDropActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val pendingIntent = PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                startActivityAndCollapse(pendingIntent)
            } else {
                @Suppress("DEPRECATION")
                startActivityAndCollapse(intent)
            }
        }

        if (isLocked && !allowWhenLocked) {
            unlockAndRun {
                launchAction()
            }
        } else {
            launchAction()
        }
    }
}

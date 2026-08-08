/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Background Services & Receivers
 * File: ColorPickerTileService.kt
 * Description: Background service component for ColorPickerTileService.kt.
 */

package com.sameerasw.essentials.services.tiles

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.service.quicksettings.Tile
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.sameerasw.essentials.R

@RequiresApi(Build.VERSION_CODES.N)
class ColorPickerTileService : BaseTileService() {

    override fun getTileLabel(): String = getString(R.string.tile_color_picker)

    override fun getTileSubtitle(): String = getString(R.string.subtitle_eye_dropper)

    override fun hasFeaturePermission(): Boolean = true

    override fun getTileIcon(): Icon {
        return Icon.createWithResource(this, R.drawable.rounded_colorize_24)
    }

    override fun getTileState(): Int = Tile.STATE_INACTIVE

    override fun onTileClick() {
        val intent = Intent(
            this,
            com.sameerasw.essentials.ui.activities.ColorPickerActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                try {
                    startActivityAndCollapse(pendingIntent)
                } catch (e: Exception) {
                    startActivity(intent)
                }
            } else {
                try {
                    @Suppress("DEPRECATION")
                    startActivityAndCollapse(intent)
                } catch (e: Exception) {
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            try {
                startActivity(intent)
            } catch (ex: Exception) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.toast_eyedropper_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

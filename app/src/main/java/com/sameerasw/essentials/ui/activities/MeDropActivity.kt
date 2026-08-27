 /*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Application Activities
 * File: MeDropActivity.kt
 */

package com.sameerasw.essentials.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sameerasw.essentials.ui.features.system.sheets.MeDropBottomSheet
import com.sameerasw.essentials.ui.theme.EssentialsTheme
import com.sameerasw.essentials.viewmodels.MainViewModel

class MeDropActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val isPitchBlackThemeEnabled by mainViewModel.isPitchBlackThemeEnabled
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                mainViewModel.check(context)
            }

            EssentialsTheme(pitchBlackTheme = isPitchBlackThemeEnabled) {
                MeDropBottomSheet(
                    viewModel = mainViewModel,
                    onDismissRequest = { finish() }
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isChangingConfigurations) {
            finish()
        }
    }
}

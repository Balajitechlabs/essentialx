/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: UI Core Components
 * File: MeDropButtonCard.kt
 */

package com.sameerasw.essentials.ui.core.cards

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.activities.MeDropSettingsActivity
import com.sameerasw.essentials.ui.features.system.sheets.MeDropBottomSheet
import com.sameerasw.essentials.ui.theme.Shapes
import com.sameerasw.essentials.utils.HapticUtil
import com.sameerasw.essentials.utils.MeDropNfcManager
import com.sameerasw.essentials.viewmodels.MainViewModel

@Composable
fun MeDropButtonCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val view = LocalView.current
    val mainViewModel: MainViewModel = viewModel()
    
    val showMeDropSheet by mainViewModel.showMeDropSheet
    val settings by mainViewModel.meDropSettings

    LaunchedEffect(showMeDropSheet, settings) {
        val activity = context as? Activity
        if (showMeDropSheet && settings?.contact != null && activity != null) {
            MeDropNfcManager.startBroadcast(activity, settings!!)
        } else if (activity != null) {
            MeDropNfcManager.stopBroadcast(activity)
        }
    }

    if (showMeDropSheet) {
        MeDropBottomSheet(
            viewModel = mainViewModel,
            onDismissRequest = { mainViewModel.showMeDropSheet.value = false }
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledTonalButton(
            onClick = {
                HapticUtil.performVirtualKeyHaptic(view)
                mainViewModel.showMeDropSheet.value = true
            },
            modifier = Modifier.weight(1f),
            shape = Shapes.medium
        ) {
            Icon(
                painter = painterResource(id = R.drawable.rounded_contacts_product_24),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.feat_medrop_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        FilledTonalIconButton(
            onClick = {
                HapticUtil.performVirtualKeyHaptic(view)
                val intent = Intent(context, MeDropSettingsActivity::class.java)
                context.startActivity(intent)
            },
            shape = Shapes.medium
        ) {
            Icon(
                painter = painterResource(id = R.drawable.rounded_settings_24),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

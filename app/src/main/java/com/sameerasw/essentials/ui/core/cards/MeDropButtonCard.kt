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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.activities.MeDropSettingsActivity
import com.sameerasw.essentials.ui.features.system.sheets.MeDropBottomSheet
import com.sameerasw.essentials.utils.HapticUtil
import com.sameerasw.essentials.utils.MeDropNfcManager
import com.sameerasw.essentials.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MeDropButtonCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val view = LocalView.current
    val mainViewModel: MainViewModel = viewModel()
    
    val showMeDropSheet by mainViewModel.showMeDropSheet
    val settings by mainViewModel.meDropSettings

    if (showMeDropSheet) {
        MeDropBottomSheet(
            viewModel = mainViewModel,
            onDismissRequest = { mainViewModel.showMeDropSheet.value = false }
        )
    }

    val safeSettings = settings
    val activeProfile = safeSettings?.activeProfileType ?: com.sameerasw.essentials.domain.model.MeDropProfileType.CONTACT
    val profilePhoto = safeSettings?.getEffectivePhotoUri(activeProfile)
    val contact = safeSettings?.contact

    SplitButtonLayout(
        leadingButton = {
            SplitButtonDefaults.LeadingButton(
                onClick = {
                    HapticUtil.performVirtualKeyHaptic(view)
                    mainViewModel.showMeDropSheet.value = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (!profilePhoto.isNullOrBlank()) {
                        AsyncImage(
                            model = profilePhoto,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    } else if (contact != null) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = contact.displayName.take(1).uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.rounded_contacts_product_24),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = if (contact != null) contact.displayName else stringResource(R.string.feat_medrop_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
        },
        trailingButton = {
            SplitButtonDefaults.TrailingButton(
                onClick = {
                    HapticUtil.performVirtualKeyHaptic(view)
                    val intent = Intent(context, MeDropSettingsActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.height(56.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.rounded_settings_24),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: WearOS Companion
 * File: WatchNotificationSettingsUI.kt
 * Description: UI component for managing watch notification sync settings.
 */

package com.sameerasw.essentials.ui.features.watch

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.sameerasw.essentials.R
import com.sameerasw.essentials.domain.model.AppSelection
import com.sameerasw.essentials.ui.core.cards.FeatureCard
import com.sameerasw.essentials.ui.core.cards.IconToggleItem
import com.sameerasw.essentials.ui.core.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.core.sheets.AppSelectionSheet
import com.sameerasw.essentials.utils.HapticUtil

@Composable
fun WatchNotificationSettingsUI(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val view = LocalView.current
    val prefs = remember {
        context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
    }

    var isSyncEnabled by remember {
        mutableStateOf(prefs.getBoolean("watch_notif_sync_enabled", false))
    }
    var isSilentEnabled by remember {
        mutableStateOf(prefs.getBoolean("watch_notif_silent_enabled", false))
    }
    var showAppPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        
       RoundedCardContainer{
            FeatureCard(
                title = stringResource(R.string.watch_notif_sync_choose_apps),
                description = stringResource(R.string.watch_notif_sync_choose_apps_desc),
                iconRes = R.drawable.rounded_apps_24,
                isEnabled = isSyncEnabled,
                showToggle = false,
                hasMoreSettings = true,
                onToggle = {},
                onClick = { showAppPicker = true }
            )
       }

        AnimatedVisibility(visible = isSyncEnabled) {
            Column {
                RoundedCardContainer(
                    modifier = Modifier
                        .padding(top = 18.dp)
                        .fillMaxWidth()
                ) {
                    IconToggleItem(
                        title = stringResource(R.string.watch_notif_sync_silent_title),
                        description = stringResource(R.string.watch_notif_sync_silent_desc),
                        iconRes = R.drawable.rounded_notifications_off_24,
                        checked = isSilentEnabled,
                        onCheckedChange = { checked ->
                            HapticUtil.performUIHaptic(view)
                            isSilentEnabled = checked
                            prefs.edit().putBoolean("watch_notif_silent_enabled", checked).apply()
                        }
                    )

                FeatureCard(
                    title = stringResource(R.string.watch_notif_sync_now),
                    description = stringResource(R.string.watch_notif_sync_now_desc),
                    iconRes = R.drawable.rounded_sync_24,
                    isEnabled = isSyncEnabled,
                    showToggle = false,
                    hasMoreSettings = false,
                    onToggle = {},
                    onClick = {
                        HapticUtil.performUIHaptic(view)
                        val listener = com.sameerasw.essentials.services.NotificationListener.instance
                        if (listener != null) {
                            val activeNotifs = listener.activeNotifications
                            activeNotifs?.forEach { sbn ->
                                com.sameerasw.essentials.services.WatchNotificationSyncManager.onNotificationPosted(context, sbn, isSilent = false)
                            }
                            android.widget.Toast.makeText(context, "Synced ${activeNotifs?.size ?: 0} notifications to watch", android.widget.Toast.LENGTH_SHORT).show()
                        } else {
                            android.widget.Toast.makeText(context, "Notification Listener Service not running", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                FeatureCard(
                    title = stringResource(R.string.watch_notif_sync_icons),
                    description = stringResource(R.string.watch_notif_sync_icons_desc),
                    iconRes = R.drawable.rounded_apps_24,
                    isEnabled = isSyncEnabled,
                    showToggle = false,
                    hasMoreSettings = false,
                    onToggle = {},
                    onClick = {
                        HapticUtil.performUIHaptic(view)
                        val allowedApps = com.sameerasw.essentials.services.WatchNotificationSyncManager.getAllowedApps(context)
                        val pkgsToSync = if (allowedApps.isNotEmpty()) {
                            allowedApps
                        } else {
                            val listener = com.sameerasw.essentials.services.NotificationListener.instance
                            listener?.activeNotifications?.map { it.packageName }?.toSet() ?: emptySet()
                        }
                        val count = com.sameerasw.essentials.services.WatchNotificationSyncManager.syncAppIcons(context, pkgsToSync)
                        android.widget.Toast.makeText(context, "Synced $count app icons to watch", android.widget.Toast.LENGTH_SHORT).show()
                    }
                )
                }
            }
        }
    }

    if (showAppPicker) {
        AppSelectionSheet(
            onDismissRequest = { showAppPicker = false },
            onLoadApps = { ctx ->
                val json = ctx.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
                    .getString("watch_notif_allowed_apps", null)
                if (json != null) {
                    try {
                        val pkgs = Gson().fromJson(json, Array<String>::class.java).toSet()
                        pkgs.map { AppSelection(it, true) }
                    } catch (e: Exception) {
                        emptyList()
                    }
                } else {
                    emptyList()
                }
            },
            onSaveApps = { ctx, selections ->
                val enabledPkgs = selections.filter { it.isEnabled }.map { it.packageName }
                val json = Gson().toJson(enabledPkgs)
                ctx.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putString("watch_notif_allowed_apps", json)
                    .apply()
            }
        )
    }
}

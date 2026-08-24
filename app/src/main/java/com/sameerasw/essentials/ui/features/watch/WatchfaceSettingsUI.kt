/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: WearOS Companion
 * File: WatchfaceSettingsUI.kt
 * Description: Composable settings screen for configuring Essentials Watchface preferences.
 */

package com.sameerasw.essentials.ui.features.watch

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.services.DeviceInfoSyncManager
import com.sameerasw.essentials.ui.components.menus.SegmentedDropdownMenuItem
import com.sameerasw.essentials.ui.core.cards.ConfigPickerItem
import com.sameerasw.essentials.ui.core.cards.IconToggleItem
import com.sameerasw.essentials.ui.core.containers.RoundedCardContainer

@Composable
fun WatchfaceSettingsUI(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
    }

    var hideBattery by remember {
        mutableStateOf(prefs.getBoolean("watchface_hide_battery", false))
    }
    var hideDeviceIcons by remember {
        mutableStateOf(prefs.getBoolean("watchface_hide_device_icons", false))
    }
    var showComplications by remember {
        mutableStateOf(prefs.getBoolean("watchface_show_complications", true))
    }
    var complicationOutline by remember {
        mutableStateOf(prefs.getBoolean("watchface_complication_outline", true))
    }
    var leftComplication by remember {
        mutableStateOf(prefs.getString("watchface_left_complication", "HEART_RATE") ?: "HEART_RATE")
    }
    var rightComplication by remember {
        mutableStateOf(prefs.getString("watchface_right_complication", "STEPS") ?: "STEPS")
    }
    var showUpcomingEvents by remember {
        mutableStateOf(prefs.getBoolean("watchface_show_upcoming_events", true))
    }
    var showGlow by remember {
        mutableStateOf(prefs.getBoolean("watchface_show_glow", true))
    }

    val complicationOptions = listOf("HEART_RATE", "STEPS", "DISTANCE", "CALORIES", "WATCH_BATTERY", "PHONE_BATTERY", "NONE")
    val complicationLabels = mapOf(
        "HEART_RATE" to stringResource(R.string.watchface_comp_heart_rate),
        "STEPS" to stringResource(R.string.watchface_comp_steps),
        "DISTANCE" to stringResource(R.string.watchface_comp_distance),
        "CALORIES" to stringResource(R.string.watchface_comp_calories),
        "WATCH_BATTERY" to stringResource(R.string.watchface_comp_watch_battery),
        "PHONE_BATTERY" to stringResource(R.string.watchface_comp_phone_battery),
        "NONE" to stringResource(R.string.watchface_comp_none),
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Section 1: Battery & Icons
        Text(
            text = stringResource(R.string.watchface_category_battery),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp),
        )
        RoundedCardContainer(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
        ) {
            IconToggleItem(
                iconRes = R.drawable.rounded_battery_android_frame_6_24,
                title = stringResource(R.string.watchface_hide_battery_title),
                description = stringResource(R.string.watchface_hide_battery_desc),
                isChecked = hideBattery,
                onCheckedChange = {
                    hideBattery = it
                    prefs.edit().putBoolean("watchface_hide_battery", it).apply()
                    DeviceInfoSyncManager.forceSync(context)
                },
            )
            IconToggleItem(
                iconRes = R.drawable.rounded_devices_24,
                title = stringResource(R.string.watchface_hide_device_icons_title),
                description = stringResource(R.string.watchface_hide_device_icons_desc),
                isChecked = hideDeviceIcons,
                onCheckedChange = {
                    hideDeviceIcons = it
                    prefs.edit().putBoolean("watchface_hide_device_icons", it).apply()
                    DeviceInfoSyncManager.forceSync(context)
                },
            )
        }

        // Section 2: Complications
        Text(
            text = stringResource(R.string.watchface_category_complications),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp),
        )
        RoundedCardContainer(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
        ) {
            IconToggleItem(
                iconRes = R.drawable.rounded_widgets_24,
                title = stringResource(R.string.watchface_show_complications_title),
                description = stringResource(R.string.watchface_show_complications_desc),
                isChecked = showComplications,
                onCheckedChange = {
                    showComplications = it
                    prefs.edit().putBoolean("watchface_show_complications", it).apply()
                    DeviceInfoSyncManager.forceSync(context)
                },
            )
            if (showComplications) {
                IconToggleItem(
                    iconRes = R.drawable.rounded_circles_24,
                    title = stringResource(R.string.watchface_complication_outline_title),
                    description = stringResource(R.string.watchface_complication_outline_desc),
                    isChecked = complicationOutline,
                    onCheckedChange = {
                        complicationOutline = it
                        prefs.edit().putBoolean("watchface_complication_outline", it).apply()
                        DeviceInfoSyncManager.forceSync(context)
                    },
                )
                ConfigPickerItem(
                    title = stringResource(R.string.watchface_left_complication_title),
                    selectedValue = complicationLabels[leftComplication] ?: leftComplication,
                    iconRes = when (leftComplication) {
                        "HEART_RATE" -> R.drawable.rounded_favorite_24
                        "STEPS" -> R.drawable.rounded_steps_24
                        "DISTANCE" -> R.drawable.rounded_distance_24
                        "CALORIES" -> R.drawable.rounded_local_fire_department_24
                        "WATCH_BATTERY" -> R.drawable.rounded_watch_24
                        "PHONE_BATTERY" -> R.drawable.rounded_mobile_24
                        else -> R.drawable.rounded_widgets_24
                    },
                ) {
                    complicationOptions.forEach { option ->
                        val label = complicationLabels[option] ?: option
                        SegmentedDropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                leftComplication = option
                                prefs.edit().putString("watchface_left_complication", option).apply()
                                DeviceInfoSyncManager.forceSync(context)
                            },
                        )
                    }
                }
                ConfigPickerItem(
                    title = stringResource(R.string.watchface_right_complication_title),
                    selectedValue = complicationLabels[rightComplication] ?: rightComplication,
                    iconRes = when (rightComplication) {
                        "HEART_RATE" -> R.drawable.rounded_favorite_24
                        "STEPS" -> R.drawable.rounded_steps_24
                        "DISTANCE" -> R.drawable.rounded_distance_24
                        "CALORIES" -> R.drawable.rounded_local_fire_department_24
                        "WATCH_BATTERY" -> R.drawable.rounded_watch_24
                        "PHONE_BATTERY" -> R.drawable.rounded_mobile_24
                        else -> R.drawable.rounded_widgets_24
                    },
                ) {
                    complicationOptions.forEach { option ->
                        val label = complicationLabels[option] ?: option
                        SegmentedDropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                rightComplication = option
                                prefs.edit().putString("watchface_right_complication", option).apply()
                                DeviceInfoSyncManager.forceSync(context)
                            },
                        )
                    }
                }
            }
        }

        // Section 3: At a Glance & Glow
        Text(
            text = stringResource(R.string.watchface_category_glance),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp),
        )
        RoundedCardContainer(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
        ) {
            IconToggleItem(
                iconRes = R.drawable.rounded_upcoming_24,
                title = stringResource(R.string.watchface_show_upcoming_events_title),
                description = stringResource(R.string.watchface_show_upcoming_events_desc),
                isChecked = showUpcomingEvents,
                onCheckedChange = {
                    showUpcomingEvents = it
                    prefs.edit().putBoolean("watchface_show_upcoming_events", it).apply()
                    DeviceInfoSyncManager.forceSync(context)
                },
            )
            if (showUpcomingEvents) {
                IconToggleItem(
                    iconRes = R.drawable.rounded_blur_on_24,
                    title = stringResource(R.string.watchface_show_glow_title),
                    description = stringResource(R.string.watchface_show_glow_desc),
                    isChecked = showGlow,
                    onCheckedChange = {
                        showGlow = it
                        prefs.edit().putBoolean("watchface_show_glow", it).apply()
                        DeviceInfoSyncManager.forceSync(context)
                    },
                )
            }
        }
    }
}

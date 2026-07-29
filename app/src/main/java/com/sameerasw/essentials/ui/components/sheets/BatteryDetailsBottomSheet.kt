package com.sameerasw.essentials.ui.components.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.theme.Shapes
import com.sameerasw.essentials.utils.BatteryDetails
import com.sameerasw.essentials.utils.BatteryInfoUtil
import com.sameerasw.essentials.utils.BatteryStatsUtil
import com.sameerasw.essentials.utils.BatteryUsageApp
import com.sameerasw.essentials.utils.CpuWakeupItem
import com.sameerasw.essentials.utils.HapticUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BatteryDetailsBottomSheet(
    initialDetails: BatteryDetails,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var batteryDetails by remember { mutableStateOf(initialDetails) }
    var selectedTab by remember { mutableIntStateOf(0) }

    var usageApps by remember { mutableStateOf<List<BatteryUsageApp>>(emptyList()) }
    var wakeupsList by remember { mutableStateOf<List<CpuWakeupItem>>(emptyList()) }

    DisposableEffect(context) {
        val receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(ctx: android.content.Context, intent: android.content.Intent) {
                if (intent.action == android.content.Intent.ACTION_BATTERY_CHANGED || intent.action == android.os.PowerManager.ACTION_POWER_SAVE_MODE_CHANGED) {
                    val freshBasic = BatteryInfoUtil.getBasicDetails(ctx)
                    batteryDetails = batteryDetails.copy(
                        level = freshBasic.level,
                        scale = freshBasic.scale,
                        status = freshBasic.status,
                        health = freshBasic.health,
                        plugged = freshBasic.plugged,
                        voltage = freshBasic.voltage,
                        temperature = freshBasic.temperature,
                        technology = freshBasic.technology,
                        isPresent = freshBasic.isPresent
                    )
                }
            }
        }
        val filter = android.content.IntentFilter().apply {
            addAction(android.content.Intent.ACTION_BATTERY_CHANGED)
            addAction(android.os.PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
        }
        context.registerReceiver(receiver, filter)
        onDispose {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: Exception) {
            }
        }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val updated = BatteryInfoUtil.fetchAdvancedDetails(context, initialDetails)
            val parsedApps = BatteryStatsUtil.parseUsageApps(context)
            val parsedWakeups = BatteryStatsUtil.parseWakeupHistory(context)
            withContext(Dispatchers.Main) {
                batteryDetails = updated
                usageApps = parsedApps
                wakeupsList = parsedWakeups
            }
        }
    }

    val isCharging = batteryDetails.status == android.os.BatteryManager.BATTERY_STATUS_CHARGING
    val isPowerSave = remember { com.sameerasw.essentials.utils.DeviceUtils.isPowerSaveMode(context) }
    val iconRes = BatteryInfoUtil.getBatteryIconRes(
        context = context,
        level = batteryDetails.level,
        isCharging = isCharging,
        status = batteryDetails.status,
        health = batteryDetails.health,
        isPresent = batteryDetails.isPresent,
        isPowerSave = isPowerSave
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${batteryDetails.level}%",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily(
                            androidx.compose.ui.text.font.Font(
                                R.font.google_sans_flex,
                                variationSettings = androidx.compose.ui.text.font.FontVariation.Settings(
                                    androidx.compose.ui.text.font.FontVariation.width(150f),
                                    androidx.compose.ui.text.font.FontVariation.weight(FontWeight.Normal.weight),
                                    androidx.compose.ui.text.font.FontVariation.Setting("ROND", 100f)
                                )
                            )
                        )
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

                val tabLabels = listOf(
                    stringResource(R.string.label_battery_tab_info),
                    stringResource(R.string.label_battery_tab_apps),
                    stringResource(R.string.label_battery_tab_system)
                )

            RoundedCardContainer {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceBright,
                            shape = Shapes.extraSmall
                        )
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                ) {
                    val modifiers = List(tabLabels.size) { Modifier.weight(1f) }
                    tabLabels.forEachIndexed { index, label ->
                        ToggleButton(
                            checked = selectedTab == index,
                            onCheckedChange = {
                                selectedTab = index
                                HapticUtil.performLightHaptic(view)
                            },
                            modifier = modifiers[index].semantics { role = Role.RadioButton },
                            shapes = when (index) {
                                0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                tabLabels.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                            },
                        ) {
                            Text(
                                text = label,
                                fontSize = dimensionResource(R.dimen.font_small).value.sp,
                                modifier = Modifier.basicMarquee(),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            when (selectedTab) {
                0 -> {
                    // Info
                    Text(
                        text = stringResource(R.string.label_battery_section_general),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    RoundedCardContainer(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InfoDetailRow(
                            title = stringResource(R.string.label_battery_health),
                            value = BatteryInfoUtil.formatHealth(batteryDetails.health),
                            iconRes = R.drawable.rounded_ecg_heart_24
                        )
                        InfoDetailRow(
                            title = stringResource(R.string.label_battery_plug_type),
                            value = BatteryInfoUtil.formatPlugged(batteryDetails.plugged),
                            iconRes = R.drawable.rounded_cable_24
                        )
                        InfoDetailRow(
                            title = stringResource(R.string.label_battery_temperature),
                            value = String.format(LocalLocale.current.platformLocale, "%.1f °C", batteryDetails.temperature / 10.0f),
                            iconRes = R.drawable.rounded_device_thermostat_24
                        )
                        InfoDetailRow(
                            title = stringResource(R.string.label_battery_voltage),
                            value = "${batteryDetails.voltage} mV",
                            iconRes = R.drawable.rounded_power_input_24
                        )
                        InfoDetailRow(
                            title = stringResource(R.string.label_battery_technology),
                            value = batteryDetails.technology,
                            iconRes = R.drawable.rounded_memory_alt_24
                        )
                    }

                    Text(
                        text = stringResource(R.string.label_battery_section_advanced),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    RoundedCardContainer(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val full = batteryDetails.chargeFull
                        val design = batteryDetails.chargeFullDesign

                        if (full != null && full > 0) {
                            val fullMah = if (full > 10000) full / 1000 else full
                            InfoDetailRow(
                                title = stringResource(R.string.label_battery_charge_full),
                                value = "$fullMah mAh",
                                iconRes = R.drawable.battery_android_frame_bolt_24px
                            )
                        }

                        if (design != null && design > 0) {
                            val designMah = if (design > 10000) design / 1000 else design
                            InfoDetailRow(
                                title = stringResource(R.string.label_battery_charge_full_design),
                                value = "$designMah mAh",
                                iconRes = R.drawable.battery_android_frame_shield_24px
                            )
                        }

                        if (full != null && design != null && design > 0) {
                            val healthPct = (full.toDouble() / design.toDouble()) * 100.0
                            InfoDetailRow(
                                title = stringResource(R.string.label_battery_capacity_health),
                                value = String.format(Locale.getDefault(), "%.1f %%", healthPct),
                                iconRes = R.drawable.rounded_ecg_heart_24
                            )
                        }

                        val counter = batteryDetails.chargeCounter
                        if (counter != null && counter > 0) {
                            val counterMah = if (counter > 10000) counter / 1000 else counter
                            InfoDetailRow(
                                title = stringResource(R.string.label_battery_charge_counter),
                                value = "$counterMah mAh",
                                iconRes = R.drawable.battery_android_frame_4_24px
                            )
                        }

                        batteryDetails.maxChargingCurrent?.let { maxCur ->
                            if (maxCur > 0) {
                                val curMa = if (maxCur > 10000) maxCur / 1000 else maxCur
                                InfoDetailRow(
                                    title = stringResource(R.string.label_battery_max_current),
                                    value = "$curMa mA",
                                    iconRes = R.drawable.rounded_power_input_24
                                )
                            }
                        }

                        batteryDetails.maxChargingVoltage?.let { maxVol ->
                            if (maxVol > 0) {
                                val volMv = if (maxVol > 100000) maxVol / 1000 else maxVol
                                InfoDetailRow(
                                    title = stringResource(R.string.label_battery_max_voltage),
                                    value = "$volMv mV",
                                    iconRes = R.drawable.rounded_power_input_24
                                )
                            }
                        }

                        batteryDetails.chargingState?.let { state ->
                            InfoDetailRow(
                                title = stringResource(R.string.label_battery_charging_state),
                                value = "$state",
                                iconRes = R.drawable.rounded_charger_24
                            )
                        }

                        batteryDetails.chargingPolicy?.let { policy ->
                            InfoDetailRow(
                                title = stringResource(R.string.label_battery_charging_policy),
                                value = BatteryInfoUtil.formatChargingPolicy(policy),
                                iconRes = R.drawable.rounded_info_24
                            )
                        }

                        batteryDetails.capacityLevel?.let { cap ->
                            InfoDetailRow(
                                title = stringResource(R.string.label_battery_capacity_level),
                                value = "$cap",
                                iconRes = R.drawable.rounded_battery_android_0_24
                            )
                        }
                    }
                }

                1 -> {
                    // Apps
                    Text(
                        text = stringResource(R.string.label_battery_usage_attribution),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    RoundedCardContainer(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (usageApps.isEmpty()) {
                            InfoDetailRow(
                                title = "Usage Data",
                                value = "No data",
                                iconRes = R.drawable.rounded_info_24
                            )
                        } else {
                            usageApps.forEach { app ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surfaceBright,
                                            shape = Shapes.extraSmall
                                        )
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (app.icon != null) {
                                        androidx.compose.foundation.Image(
                                            bitmap = app.icon.toBitmap(48, 48).asImageBitmap(),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(id = R.drawable.rounded_info_24),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = app.appName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = String.format(Locale.getDefault(), "%.2f mAh", app.powerMah),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // System
                    val profile = batteryDetails.powerProfile
                    if (!profile.isNullOrEmpty()) {
                        Text(
                            text = stringResource(R.string.label_battery_section_power_profile),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        RoundedCardContainer(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            profile["screen.on"]?.let {
                                InfoDetailRow(title = "Screen On Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                            }
                            profile["screen.full"]?.let {
                                InfoDetailRow(title = "Screen Max Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                            }
                            profile["ambient.on"]?.let {
                                InfoDetailRow(title = "Ambient/AOD Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                            }
                            profile["audio"]?.let {
                                InfoDetailRow(title = "Audio Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                            }
                            profile["video"]?.let {
                                InfoDetailRow(title = "Video Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                            }
                            profile["camera.avg"]?.let {
                                InfoDetailRow(title = "Camera Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                            }
                            profile["camera.flashlight"]?.let {
                                InfoDetailRow(title = "Flashlight Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                            }
                            profile["cpu.active"]?.let {
                                InfoDetailRow(title = "CPU Active Drain", value = "$it mA", iconRes = R.drawable.rounded_memory_alt_24)
                            }
                            profile["cpu.idle"]?.let {
                                InfoDetailRow(title = "CPU Idle Drain", value = "$it mA", iconRes = R.drawable.rounded_memory_alt_24)
                            }
                            profile["cpu.suspend"]?.let {
                                InfoDetailRow(title = "CPU Suspend Drain", value = "$it mA", iconRes = R.drawable.rounded_memory_alt_24)
                            }
                        }
                    }

                    if (wakeupsList.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.label_battery_wakeups_attribution),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        RoundedCardContainer(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            wakeupsList.take(20).forEach { item ->
                                InfoDetailRow(
                                    title = "${item.subsystem} (${item.timeAgo})",
                                    value = item.attribution,
                                    iconRes = item.iconRes
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoDetailRow(
    title: String,
    value: String,
    iconRes: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceBright,
                shape = Shapes.extraSmall
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

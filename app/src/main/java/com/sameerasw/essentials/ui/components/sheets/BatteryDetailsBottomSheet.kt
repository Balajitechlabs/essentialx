package com.sameerasw.essentials.ui.components.sheets

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.theme.Shapes
import com.sameerasw.essentials.utils.BatteryDetails
import com.sameerasw.essentials.utils.BatteryInfoUtil
import com.sameerasw.essentials.utils.ColorUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatteryDetailsBottomSheet(
    initialDetails: BatteryDetails,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var batteryDetails by remember { mutableStateOf(initialDetails) }

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
            withContext(Dispatchers.Main) {
                batteryDetails = updated
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

            // General
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
                    iconRes = R.drawable.rounded_battery_android_frame_shield_24
                )
                InfoDetailRow(
                    title = stringResource(R.string.label_battery_plug_type),
                    value = BatteryInfoUtil.formatPlugged(batteryDetails.plugged),
                    iconRes = R.drawable.rounded_power_settings_new_24
                )
                InfoDetailRow(
                    title = stringResource(R.string.label_battery_temperature),
                    value = String.format(Locale.getDefault(), "%.1f °C", batteryDetails.temperature / 10.0f),
                    iconRes = R.drawable.rounded_info_24
                )
                InfoDetailRow(
                    title = stringResource(R.string.label_battery_voltage),
                    value = "${batteryDetails.voltage} mV",
                    iconRes = R.drawable.rounded_info_24
                )
                InfoDetailRow(
                    title = stringResource(R.string.label_battery_technology),
                    value = batteryDetails.technology,
                    iconRes = R.drawable.rounded_memory_alt_24
                )
            }

            // Battery info
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
                        iconRes = R.drawable.rounded_battery_android_frame_plus_24
                    )
                }

                if (design != null && design > 0) {
                    val designMah = if (design > 10000) design / 1000 else design
                    InfoDetailRow(
                        title = stringResource(R.string.label_battery_charge_full_design),
                        value = "$designMah mAh",
                        iconRes = R.drawable.rounded_battery_android_frame_plus_24
                    )
                }

                if (full != null && design != null && design > 0) {
                    val healthPct = (full.toDouble() / design.toDouble()) * 100.0
                    InfoDetailRow(
                        title = stringResource(R.string.label_battery_capacity_health),
                        value = String.format(Locale.getDefault(), "%.1f %%", healthPct),
                        iconRes = R.drawable.rounded_battery_android_frame_shield_24
                    )
                }

                val counter = batteryDetails.chargeCounter
                if (counter != null && counter > 0) {
                    val counterMah = if (counter > 10000) counter / 1000 else counter
                    InfoDetailRow(
                        title = stringResource(R.string.label_battery_charge_counter),
                        value = "$counterMah mAh",
                        iconRes = R.drawable.rounded_battery_android_frame_6_24
                    )
                }

                batteryDetails.maxChargingCurrent?.let { maxCur ->
                    if (maxCur > 0) {
                        val curMa = if (maxCur > 10000) maxCur / 1000 else maxCur
                        InfoDetailRow(
                            title = stringResource(R.string.label_battery_max_current),
                            value = "$curMa mA",
                            iconRes = R.drawable.rounded_power_settings_new_24
                        )
                    }
                }

                batteryDetails.maxChargingVoltage?.let { maxVol ->
                    if (maxVol > 0) {
                        val volMv = if (maxVol > 100000) maxVol / 1000 else maxVol
                        InfoDetailRow(
                            title = stringResource(R.string.label_battery_max_voltage),
                            value = "$volMv mV",
                            iconRes = R.drawable.rounded_power_settings_new_24
                        )
                    }
                }

                batteryDetails.chargingState?.let { state ->
                    InfoDetailRow(
                        title = stringResource(R.string.label_battery_charging_state),
                        value = "$state",
                        iconRes = R.drawable.rounded_info_24
                    )
                }

                batteryDetails.chargingPolicy?.let { policy ->
                    InfoDetailRow(
                        title = stringResource(R.string.label_battery_charging_policy),
                        value = "$policy",
                        iconRes = R.drawable.rounded_info_24
                    )
                }

                batteryDetails.capacityLevel?.let { cap ->
                    InfoDetailRow(
                        title = stringResource(R.string.label_battery_capacity_level),
                        value = "$cap",
                        iconRes = R.drawable.rounded_info_24
                    )
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

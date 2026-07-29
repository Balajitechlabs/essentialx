package com.sameerasw.essentials.ui.components.battery

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.utils.BatteryDetails
import com.sameerasw.essentials.utils.BatteryInfoUtil
import java.util.Locale

@Composable
fun BatteryInfoTabContent(
    batteryDetails: BatteryDetails,
    isLoadingAdvanced: Boolean
) {
    // Health
    Text(
        text = stringResource(R.string.label_battery_section_health),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 8.dp)
    )

    RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
        InfoDetailRow(
            title = stringResource(R.string.label_battery_health),
            value = BatteryInfoUtil.formatHealth(batteryDetails.health),
            iconRes = R.drawable.rounded_ecg_heart_24
        )
        InfoDetailRow(
            title = stringResource(R.string.label_battery_temperature),
            value = String.format(LocalLocale.current.platformLocale, "%.1f °C", batteryDetails.temperature / 10.0f),
            iconRes = R.drawable.rounded_device_thermostat_24
        )

        if (isLoadingAdvanced) {
            BatteryLoadingIndicatorCard()
        } else {
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
        }
    }

    // Charging
    Text(
        text = stringResource(R.string.label_battery_section_charging),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 8.dp)
    )

    RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
        InfoDetailRow(
            title = stringResource(R.string.label_battery_plug_type),
            value = BatteryInfoUtil.formatPlugged(batteryDetails.plugged),
            iconRes = R.drawable.rounded_cable_24
        )

        if (isLoadingAdvanced) {
            BatteryLoadingIndicatorCard()
        } else {
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
        }
    }

    // Specs
    Text(
        text = stringResource(R.string.label_battery_section_specs),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 8.dp)
    )

    RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
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

        if (!isLoadingAdvanced) {
            val counter = batteryDetails.chargeCounter
            if (counter != null && counter > 0) {
                val counterMah = if (counter > 10000) counter / 1000 else counter
                InfoDetailRow(
                    title = stringResource(R.string.label_battery_charge_counter),
                    value = "$counterMah mAh",
                    iconRes = R.drawable.battery_android_frame_4_24px
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
}

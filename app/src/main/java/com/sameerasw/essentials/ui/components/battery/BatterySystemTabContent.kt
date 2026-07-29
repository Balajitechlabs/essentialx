package com.sameerasw.essentials.ui.components.battery

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.utils.CpuWakeupItem

import java.util.Locale

@Composable
fun BatterySystemTabContent(
    isLoadingAdvanced: Boolean,
    powerProfile: Map<String, String>?,
    wakeupsList: List<CpuWakeupItem>,
    showPercentage: Boolean,
    onToggleUnit: () -> Unit
) {
    if (isLoadingAdvanced) {
        RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
            BatteryLoadingIndicatorCard()
        }
    } else {
        if (!powerProfile.isNullOrEmpty()) {
            val totalMa = powerProfile.values.mapNotNull { it.toDoubleOrNull() }.sum().coerceAtLeast(0.0001)

            fun formatProfileValue(raw: String): String {
                val num = raw.toDoubleOrNull() ?: return "$raw mA"
                return if (showPercentage) {
                    val pct = (num / totalMa) * 100.0
                    String.format(Locale.getDefault(), "%.1f %%", pct)
                } else {
                    "$raw mA"
                }
            }

            RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
                powerProfile["screen.on"]?.let {
                    InfoDetailRow(title = "Screen On Drain", value = formatProfileValue(it), iconRes = R.drawable.rounded_info_24, onClick = onToggleUnit)
                }
                powerProfile["screen.full"]?.let {
                    InfoDetailRow(title = "Screen Max Drain", value = formatProfileValue(it), iconRes = R.drawable.rounded_info_24, onClick = onToggleUnit)
                }
                powerProfile["ambient.on"]?.let {
                    InfoDetailRow(title = "Ambient/AOD Drain", value = formatProfileValue(it), iconRes = R.drawable.rounded_info_24, onClick = onToggleUnit)
                }
                powerProfile["audio"]?.let {
                    InfoDetailRow(title = "Audio Drain", value = formatProfileValue(it), iconRes = R.drawable.rounded_info_24, onClick = onToggleUnit)
                }
                powerProfile["video"]?.let {
                    InfoDetailRow(title = "Video Drain", value = formatProfileValue(it), iconRes = R.drawable.rounded_info_24, onClick = onToggleUnit)
                }
                powerProfile["camera.avg"]?.let {
                    InfoDetailRow(title = "Camera Drain", value = formatProfileValue(it), iconRes = R.drawable.rounded_info_24, onClick = onToggleUnit)
                }
                powerProfile["camera.flashlight"]?.let {
                    InfoDetailRow(title = "Flashlight Drain", value = formatProfileValue(it), iconRes = R.drawable.rounded_info_24, onClick = onToggleUnit)
                }
                powerProfile["cpu.active"]?.let {
                    InfoDetailRow(title = "CPU Active Drain", value = formatProfileValue(it), iconRes = R.drawable.rounded_memory_alt_24, onClick = onToggleUnit)
                }
                powerProfile["cpu.idle"]?.let {
                    InfoDetailRow(title = "CPU Idle Drain", value = formatProfileValue(it), iconRes = R.drawable.rounded_memory_alt_24, onClick = onToggleUnit)
                }
                powerProfile["cpu.suspend"]?.let {
                    InfoDetailRow(title = "CPU Suspend Drain", value = formatProfileValue(it), iconRes = R.drawable.rounded_memory_alt_24, onClick = onToggleUnit)
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

            RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
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

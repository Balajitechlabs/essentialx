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

@Composable
fun BatterySystemTabContent(
    isLoadingAdvanced: Boolean,
    powerProfile: Map<String, String>?,
    wakeupsList: List<CpuWakeupItem>
) {
    if (isLoadingAdvanced) {
        RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
            BatteryLoadingIndicatorCard()
        }
    } else {
        if (!powerProfile.isNullOrEmpty()) {
            RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
                powerProfile["screen.on"]?.let {
                    InfoDetailRow(title = "Screen On Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                }
                powerProfile["screen.full"]?.let {
                    InfoDetailRow(title = "Screen Max Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                }
                powerProfile["ambient.on"]?.let {
                    InfoDetailRow(title = "Ambient/AOD Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                }
                powerProfile["audio"]?.let {
                    InfoDetailRow(title = "Audio Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                }
                powerProfile["video"]?.let {
                    InfoDetailRow(title = "Video Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                }
                powerProfile["camera.avg"]?.let {
                    InfoDetailRow(title = "Camera Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                }
                powerProfile["camera.flashlight"]?.let {
                    InfoDetailRow(title = "Flashlight Drain", value = "$it mA", iconRes = R.drawable.rounded_info_24)
                }
                powerProfile["cpu.active"]?.let {
                    InfoDetailRow(title = "CPU Active Drain", value = "$it mA", iconRes = R.drawable.rounded_memory_alt_24)
                }
                powerProfile["cpu.idle"]?.let {
                    InfoDetailRow(title = "CPU Idle Drain", value = "$it mA", iconRes = R.drawable.rounded_memory_alt_24)
                }
                powerProfile["cpu.suspend"]?.let {
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

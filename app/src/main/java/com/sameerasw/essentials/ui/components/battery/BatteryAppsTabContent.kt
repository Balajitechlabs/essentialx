package com.sameerasw.essentials.ui.components.battery

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import com.sameerasw.essentials.ui.theme.Shapes
import com.sameerasw.essentials.utils.BatteryUsageApp
import com.sameerasw.essentials.utils.HapticUtil
import java.util.Locale

import androidx.compose.foundation.clickable

@Composable
fun BatteryAppsTabContent(
    isLoadingAdvanced: Boolean,
    usageApps: List<BatteryUsageApp>,
    showAllApps: Boolean,
    onToggleShowAll: () -> Unit,
    showPercentage: Boolean,
    onToggleUnit: () -> Unit,
    view: View
) {
    if (isLoadingAdvanced) {
        RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
            BatteryLoadingIndicatorCard()
        }
    } else if (usageApps.isEmpty()) {
        RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
            InfoDetailRow(
                title = "Usage Data",
                value = "No data",
                iconRes = R.drawable.rounded_info_24
            )
        }
    } else {
        val displayedApps = if (showAllApps) usageApps else usageApps.take(20)
        val totalMah = usageApps.sumOf { it.powerMah }.coerceAtLeast(0.0001)

        RoundedCardContainer(modifier = Modifier.fillMaxWidth()) {
            displayedApps.forEach { app ->
                val displayValue = if (showPercentage) {
                    val pct = (app.powerMah / totalMah) * 100.0
                    String.format(Locale.getDefault(), "%.1f %%", pct)
                } else {
                    String.format(Locale.getDefault(), "%.2f mAh", app.powerMah)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceBright,
                            shape = Shapes.extraSmall
                        )
                        .clickable {
                            HapticUtil.performVirtualKeyHaptic(view)
                            onToggleUnit()
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (app.icon != null) {
                        Image(
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = displayValue,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        if (usageApps.size > 20) {
            com.sameerasw.essentials.ui.components.buttons.ListExpandToggleButton(
                isExpanded = showAllApps,
                onToggle = onToggleShowAll
            )
        }
    }
}

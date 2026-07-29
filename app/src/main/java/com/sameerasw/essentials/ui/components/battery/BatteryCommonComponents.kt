package com.sameerasw.essentials.ui.components.battery

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.theme.Shapes
import androidx.compose.foundation.clickable
import com.sameerasw.essentials.utils.HapticUtil
import java.util.Locale

@Composable
fun InfoDetailRow(
    title: String,
    value: String,
    iconRes: Int,
    onClick: (() -> Unit)? = null
) {
    val view = LocalView.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceBright,
                shape = Shapes.extraSmall
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable {
                        HapticUtil.performVirtualKeyHaptic(view)
                        onClick()
                    }
                } else Modifier
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
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BatteryLoadingIndicatorCard() {
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
        LinearWavyProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BatteryUsageBreakdownHeader(
    appsPct: Float,
    systemPct: Float,
    otherPct: Float,
    activeTab: Int // 1: Apps, 2: System
) {
    val safeApps = appsPct.coerceIn(0f, 100f)
    val safeSystem = systemPct.coerceIn(0f, 100f)
    val safeOther = otherPct.coerceIn(0f, 100f)

    val animatedAppsWeight by animateFloatAsState(targetValue = safeApps.coerceAtLeast(1f), label = "apps_weight")
    val animatedSystemWeight by animateFloatAsState(targetValue = safeSystem.coerceAtLeast(1f), label = "system_weight")
    val animatedOtherWeight by animateFloatAsState(targetValue = safeOther.coerceAtLeast(1f), label = "other_weight")

    // Colors: Selected tab gets Primary, other active tabs get Secondary/Tertiary, disabled/other gets surfaceVariant
    val appsColor = if (activeTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val systemColor = if (activeTab == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    val otherColor = MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Multi-segment progress bar (Fully rounded outer ends, connected extraSmall inner joints)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(CircleShape),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(animatedAppsWeight)
                        .fillMaxHeight()
                        .clip(androidx.compose.material3.ButtonGroupDefaults.connectedLeadingButtonShapes().shape)
                        .background(appsColor)
                )
                Box(
                    modifier = Modifier
                        .weight(animatedSystemWeight)
                        .fillMaxHeight()
                        .clip(androidx.compose.material3.ButtonGroupDefaults.connectedMiddleButtonShapes().shape)
                        .background(systemColor)
                )
                Box(
                    modifier = Modifier
                        .weight(animatedOtherWeight)
                        .fillMaxHeight()
                        .clip(androidx.compose.material3.ButtonGroupDefaults.connectedTrailingButtonShapes().shape)
                        .background(otherColor)
                )
            }

            // Legend row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BreakdownLegendItem(
                    label = stringResource(R.string.label_battery_tab_apps),
                    percentage = safeApps,
                    color = appsColor,
                    isSelected = activeTab == 1
                )
                BreakdownLegendItem(
                    label = stringResource(R.string.label_battery_tab_system),
                    percentage = safeSystem,
                    color = systemColor,
                    isSelected = activeTab == 2
                )
                BreakdownLegendItem(
                    label = stringResource(R.string.label_battery_other),
                    percentage = safeOther,
                    color = otherColor,
                    isSelected = false
                )
            }
        }
    }
}

@Composable
private fun BreakdownLegendItem(
    label: String,
    percentage: Float,
    color: Color,
    isSelected: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = "$label ${String.format(Locale.getDefault(), "%.0f%%", percentage)}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

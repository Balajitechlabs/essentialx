package com.sameerasw.essentials.ui.components.cards

import android.os.BatteryManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.components.sheets.BatteryDetailsBottomSheet
import com.sameerasw.essentials.ui.theme.Shapes
import com.sameerasw.essentials.utils.BatteryInfoUtil
import com.sameerasw.essentials.utils.HapticUtil
import com.sameerasw.essentials.utils.ShellUtils

@Composable
fun BatteryInfoCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val view = LocalView.current
    var showSheet by remember { mutableStateOf(false) }

    val hasPermission = remember { ShellUtils.hasPermission(context) }
    val basicDetails = remember { BatteryInfoUtil.getBasicDetails(context) }

    val isCharging = basicDetails.status == BatteryManager.BATTERY_STATUS_CHARGING
    val isPowerSave = remember { com.sameerasw.essentials.utils.DeviceUtils.isPowerSaveMode(context) }
    val iconRes = BatteryInfoUtil.getBatteryIconRes(
        level = basicDetails.level,
        isCharging = isCharging,
        status = basicDetails.status,
        health = basicDetails.health,
        isPresent = basicDetails.isPresent,
        isPowerSave = isPowerSave
    )

    if (showSheet) {
        BatteryDetailsBottomSheet(
            initialDetails = basicDetails,
            onDismiss = { showSheet = false }
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceBright,
                shape = Shapes.extraSmall
            )
            .then(
                if (hasPermission) {
                    Modifier.clickable {
                        HapticUtil.performVirtualKeyHaptic(view)
                        showSheet = true
                    }
                } else Modifier
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(R.string.label_device_battery),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${basicDetails.level}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            if (hasPermission) {
                Icon(
                    painter = painterResource(id = R.drawable.rounded_chevron_right_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

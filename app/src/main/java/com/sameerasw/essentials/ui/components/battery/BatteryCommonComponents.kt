package com.sameerasw.essentials.ui.components.battery

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.Canvas
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sameerasw.essentials.R
import com.sameerasw.essentials.ui.theme.Shapes
import com.sameerasw.essentials.utils.HapticUtil
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.Card
import com.sameerasw.essentials.ui.components.containers.RoundedCardContainer
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SectionHeaderTitle(
    title: Any,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val view = LocalView.current
    val isTranslationModeActive by com.sameerasw.essentials.translation.TranslationManager.isTranslationModeEnabled
    var showMenu by remember { mutableStateOf(false) }
    var translationSheetKey by remember { mutableStateOf<String?>(null) }

    val displayTitle = when (title) {
        is Int -> stringResource(title)
        is String -> title
        else -> title.toString()
    }

    Box(modifier = modifier) {
        Text(
            text = displayTitle,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .combinedClickable(
                    onClick = {},
                    onLongClick = if (isTranslationModeActive) {
                        {
                            HapticUtil.performVirtualKeyHaptic(view)
                            showMenu = true
                        }
                    } else null
                )
                .padding(start = 8.dp)
        )

        com.sameerasw.essentials.ui.components.menus.SegmentedDropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            com.sameerasw.essentials.translation.ui.TranslationMenuItems(
                title = title,
                onSelectKey = { key ->
                    showMenu = false
                    translationSheetKey = key
                }
            )
        }
    }

    val keyForSheet1 = translationSheetKey
    if (keyForSheet1 != null) {
        val resolvedKey = remember(keyForSheet1) {
            com.sameerasw.essentials.translation.TranslationManager.resolveKey(context, keyForSheet1) ?: keyForSheet1
        }
        com.sameerasw.essentials.translation.ui.TranslationBottomSheet(
            stringKey = resolvedKey,
            onDismissRequest = { translationSheetKey = null }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoDetailRow(
    title: Any,
    value: String,
    iconRes: Int,
    onClick: (() -> Unit)? = null
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val view = LocalView.current
    val isTranslationModeActive by com.sameerasw.essentials.translation.TranslationManager.isTranslationModeEnabled
    var showMenu by remember { mutableStateOf(false) }
    var translationSheetKey by remember { mutableStateOf<String?>(null) }

    val displayTitle = when (title) {
        is Int -> stringResource(title)
        is String -> title
        else -> title.toString()
    }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceBright,
                    shape = Shapes.extraSmall
                )
                .combinedClickable(
                    onClick = {
                        if (onClick != null) {
                            HapticUtil.performVirtualKeyHaptic(view)
                            onClick()
                        }
                    },
                    onLongClick = if (isTranslationModeActive) {
                        {
                            HapticUtil.performVirtualKeyHaptic(view)
                            showMenu = true
                        }
                    } else null
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
                text = displayTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            AnimatedContent(
                targetState = value,
                transitionSpec = {
                    fun parseNum(s: String): Double? {
                        val digits = s.replace("-", "").replace(Regex("[^0-9.]"), "")
                        return digits.toDoubleOrNull()
                    }
                    val oldVal = parseNum(initialState)
                    val newVal = parseNum(targetState)
                    val isIncreasing = if (oldVal != null && newVal != null) newVal > oldVal else true

                    if (isIncreasing) {
                        (slideInVertically { height -> -height } + fadeIn())
                            .togetherWith(slideOutVertically { height -> height } + fadeOut())
                    } else {
                        (slideInVertically { height -> height } + fadeIn())
                            .togetherWith(slideOutVertically { height -> -height } + fadeOut())
                    }
                },
                label = "info_row_value"
            ) { targetVal ->
                Text(
                    text = targetVal,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        com.sameerasw.essentials.ui.components.menus.SegmentedDropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            com.sameerasw.essentials.translation.ui.TranslationMenuItems(
                title = title,
                onSelectKey = { key ->
                    showMenu = false
                    translationSheetKey = key
                }
            )
        }
    }

    val keyForSheet2 = translationSheetKey
    if (keyForSheet2 != null) {
        val resolvedKey = remember(keyForSheet2) {
            com.sameerasw.essentials.translation.TranslationManager.resolveKey(context, keyForSheet2) ?: keyForSheet2
        }
        com.sameerasw.essentials.translation.ui.TranslationBottomSheet(
            stringKey = resolvedKey,
            onDismissRequest = { translationSheetKey = null }
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
fun TopAppsBreakdownHeader(
    usageApps: List<com.sameerasw.essentials.utils.BatteryUsageApp>
) {
    if (usageApps.isEmpty()) return

    val top7Apps = remember(usageApps) { usageApps.take(7) }
    val remainingApps = remember(usageApps) { usageApps.drop(7) }
    val otherMah = remember(remainingApps) { remainingApps.sumOf { it.powerMah } }
    val totalAllMah = remember(usageApps) { usageApps.sumOf { it.powerMah }.coerceAtLeast(0.0001) }

    val hasOther = otherMah > 0.0001
    val totalSegments = top7Apps.size + (if (hasOther) 1 else 0)
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            top7Apps.forEachIndexed { index, app ->
                val weight = ((app.powerMah / totalAllMah) * 100.0).toFloat().coerceAtLeast(1f)
                var brandColor by remember(app.packageName) {
                    mutableStateOf<Color?>(null)
                }

                androidx.compose.runtime.LaunchedEffect(app.packageName) {
                    if (app.packageName != null) {
                        com.sameerasw.essentials.utils.AppUtil.getAppBrandColor(context, app.packageName) { argb ->
                            if (argb != android.graphics.Color.TRANSPARENT && argb != android.graphics.Color.GRAY) {
                                brandColor = Color(argb)
                            }
                        }
                    }
                }

                val barColor = brandColor ?: MaterialTheme.colorScheme.primaryContainer

                val shape = when {
                    totalSegments == 1 -> CircleShape
                    index == 0 -> ButtonGroupDefaults.connectedLeadingButtonShapes().shape
                    index == totalSegments - 1 -> ButtonGroupDefaults.connectedTrailingButtonShapes().shape
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                }

                Box(
                    modifier = Modifier
                        .weight(weight)
                        .fillMaxHeight()
                        .clip(shape)
                        .background(barColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (app.icon != null) {
                        val bitmap = remember(app.icon) { app.icon.toBitmap(48, 48).asImageBitmap() }
                        Image(
                            bitmap = bitmap,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.rounded_info_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (hasOther) {
                val otherWeight = ((otherMah / totalAllMah) * 100.0).toFloat().coerceAtLeast(1f)
                val otherShape = if (top7Apps.isEmpty()) CircleShape else ButtonGroupDefaults.connectedTrailingButtonShapes().shape
                Box(
                    modifier = Modifier
                        .weight(otherWeight)
                        .fillMaxHeight()
                        .clip(otherShape)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
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

    // Colors: Only selected tab gets Primary accent color, all unselected sections use outlineVariant
    val appsColor = if (activeTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val systemColor = if (activeTab == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val otherColor = MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
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
                        .clip(ButtonGroupDefaults.connectedLeadingButtonShapes().shape)
                        .background(appsColor)
                )
                Box(
                    modifier = Modifier
                        .weight(animatedSystemWeight)
                        .fillMaxHeight()
                        .clip(ButtonGroupDefaults.connectedMiddleButtonShapes().shape)
                        .background(systemColor)
                )
                Box(
                    modifier = Modifier
                        .weight(animatedOtherWeight)
                        .fillMaxHeight()
                        .clip(ButtonGroupDefaults.connectedTrailingButtonShapes().shape)
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
                    isSelected = activeTab == 1
                )
                BreakdownLegendItem(
                    label = stringResource(R.string.label_battery_tab_system),
                    percentage = safeSystem,
                    isSelected = activeTab == 2
                )
                BreakdownLegendItem(
                    label = stringResource(R.string.label_battery_other),
                    percentage = safeOther,
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
    isSelected: Boolean
) {
    Text(
        text = "$label ${String.format(Locale.getDefault(), "%.0f%%", percentage)}",
        style = MaterialTheme.typography.labelLarge,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    )
}
@Composable
fun BatteryDrainGraphCard(
    currentLevel: Int,
    chargeTimeRemainingMs: Long? = null,
    avgCurrentMa: Int? = null,
    isPlugged: Boolean = false,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val outlineVariant = MaterialTheme.colorScheme.outlineVariant
    val surfaceColor = MaterialTheme.colorScheme.surfaceBright
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val dashedColor = primaryColor.copy(alpha = 0.5f)

    // format hours
    fun formatHoursRounded(hours: Double): String {
        val rounded = (Math.round(hours * 4.0) / 4.0)
        return if (rounded % 1.0 == 0.0) {
            "${rounded.toInt()}h"
        } else {
            "$rounded" + "h"
        }
    }

    //  prediction duration
    val predictedDurationMs = remember(chargeTimeRemainingMs) {
        if (chargeTimeRemainingMs != null && chargeTimeRemainingMs > 0) {
            chargeTimeRemainingMs
        } else {
            null
        }
    }

    val formattedToGo = remember(predictedDurationMs) {
        if (predictedDurationMs != null) {
            val hours = predictedDurationMs.toDouble() / (1000.0 * 3600.0)
            "${formatHoursRounded(hours)} to go"
        } else {
            "¯\\_(ツ)_/¯"
        }
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    val formattedAgo = remember(currentLevel) {
        val repo = com.sameerasw.essentials.data.repository.SettingsRepository(context)
        val lastResetTime = repo.getLong("last_battery_stats_reset_time", -1L)
        val elapsedMs = if (lastResetTime > 0L) {
            (System.currentTimeMillis() - lastResetTime).coerceAtLeast(0L)
        } else {
            android.os.SystemClock.elapsedRealtime()
        }
        val hours = elapsedMs.toDouble() / (1000.0 * 3600.0)
        "${formatHoursRounded(hours)} ago"
    }

    RoundedCardContainer{
Card(
    shape = Shapes.extraSmall,
    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = surfaceColor)
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 20.dp.toPx()

        val graphWidth = width - padding * 2
        val graphHeight = height - padding * 2

        // guides
        val lineY100 = padding
        val lineY50 = padding + graphHeight / 2
        val lineY0 = padding + graphHeight

        val strokeWidthPx = 2.dp.toPx()
        val gridColor = outlineVariant.copy(alpha = 0.4f)

        drawLine(
            color = gridColor,
            start = androidx.compose.ui.geometry.Offset(padding, lineY100),
            end = androidx.compose.ui.geometry.Offset(width - padding, lineY100),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = gridColor,
            start = androidx.compose.ui.geometry.Offset(padding, lineY50),
            end = androidx.compose.ui.geometry.Offset(width - padding, lineY50),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = gridColor,
            start = androidx.compose.ui.geometry.Offset(padding, lineY0),
            end = androidx.compose.ui.geometry.Offset(width - padding, lineY0),
            strokeWidth = 1.dp.toPx()
        )

        // Define points
        val midX = padding + graphWidth * 0.55f
        val actualStartY = if (isPlugged) lineY0 else lineY100
        val currentY = padding + graphHeight * (1f - currentLevel / 100f)

        // drain / charge path
        val actualPath = Path().apply {
            moveTo(padding, actualStartY)
            cubicTo(
                padding + (midX - padding) * 0.5f, actualStartY,
                padding + (midX - padding) * 0.5f, currentY,
                midX, currentY
            )
        }

        // Fill area
        val fillPath = Path().apply {
            addPath(actualPath)
            lineTo(midX, lineY0)
            lineTo(padding, lineY0)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(primaryColor.copy(alpha = 0.25f), Color.Transparent),
                startY = padding,
                endY = lineY0
            )
        )

        drawPath(
            path = actualPath,
            color = primaryColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // predicted path
        if (predictedDurationMs != null) {
            val endX = width - padding
            val predictedY = if (isPlugged) lineY100 else lineY0

            val predictedPath = Path().apply {
                moveTo(midX, currentY)
                cubicTo(
                    midX + (endX - midX) * 0.5f, currentY,
                    midX + (endX - midX) * 0.5f, predictedY,
                    endX, predictedY
                )
            }

            drawPath(
                path = predictedPath,
                color = dashedColor,
                style = Stroke(
                    width = 2.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                )
            )
        }

        //  current level
        drawCircle(
            color = surfaceColor,
            radius = 6.dp.toPx(),
            center = androidx.compose.ui.geometry.Offset(midX, currentY)
        )
        drawCircle(
            color = primaryColor,
            radius = 4.dp.toPx(),
            center = androidx.compose.ui.geometry.Offset(midX, currentY)
        )
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formattedAgo,
            style = MaterialTheme.typography.bodyMedium,
            color = onSurfaceVariant
        )
        Text(
            text = formattedToGo,
            style = MaterialTheme.typography.bodyMedium,
            color = onSurfaceVariant
        )
    }
}
    }
}

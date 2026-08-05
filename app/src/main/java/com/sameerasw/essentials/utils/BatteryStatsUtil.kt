package com.sameerasw.essentials.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.sameerasw.essentials.R
import com.sameerasw.essentials.data.repository.SettingsRepository

data class BatteryUsageApp(
    val uid: Int,
    val packageName: String?,
    val appName: String,
    val powerMah: Double,
    val fgTimeMs: Long,
    val bgTimeMs: Long,
    val icon: Drawable?
)

data class CpuWakeupItem(
    val timeAgo: String,
    val subsystem: String,
    val attribution: String,
    val iconRes: Int
)

object BatteryStatsUtil {

    fun resetStats(context: Context): Boolean {
        val res1 = ShellUtils.runCommandWithOutput(context, "dumpsys batterystats --reset")
        ShellUtils.runCommand(context, "cmd battery reset")
        if (res1 != null) {
            val repo = SettingsRepository(context)
            repo.putLong("last_battery_stats_reset_time", System.currentTimeMillis())
            BatteryHistoryManager.clearHistory(context)
        }
        return res1 != null
    }

    fun parseUsageApps(context: Context): List<BatteryUsageApp> {
        val output = ShellUtils.runCommandWithOutput(context, "dumpsys batterystats --usage")
            ?: return emptyList()

        val pm = context.packageManager
        val list = mutableListOf<BatteryUsageApp>()

        var currentUid: Int? = null
        var currentMah = 0.0
        var currentFg = 0L
        var currentBg = 0L

        fun getSystemUidLabel(uid: Int, pkg: String?): String {
            return when (uid) {
                -5 -> "Tethering & Hotspot"
                0 -> "Root / Kernel"
                1001 -> "Telephony"
                1003 -> "Graphics / GPU"
                1010 -> "Wi-Fi"
                1013 -> "Media Server"
                1017 -> "Keystore"
                1019 -> "DRM"
                1020 -> "Multicast DNS"
                1021 -> "GPS"
                1036 -> "Log Daemon"
                1040 -> "Media Extractor"
                1041 -> "Audio Server"
                1046 -> "Media Codec"
                1047 -> "Camera Server"
                1053 -> "WebView Zygote"
                1058 -> "Crash Dumps"
                1064 -> "Hardware Security"
                1066 -> "Stats Daemon"
                1067 -> "Incident Daemon"
                1069 -> "Low Memory Killer"
                1072 -> "GPU Service"
                1080 -> "Context Hub"
                1082 -> "ART Service"
                1083 -> "UWB Subsystem"
                1092 -> "PRNG Seeder"
                else -> pkg?.let { getAppName(pm, it) } ?: "System ($uid)"
            }
        }

        output.lines().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.startsWith("UID ")) {
                val uid = currentUid
                if (uid != null && currentMah > 0.0001) {
                    val pkg = pm.getPackagesForUid(uid)?.firstOrNull()
                    val label = getSystemUidLabel(uid, pkg)
                    val drawable = pkg?.let { getAppIcon(pm, it) }
                    list.add(BatteryUsageApp(uid, pkg, label, currentMah, currentFg, currentBg, drawable))
                }
                val parts = trimmed.split(":")
                currentUid = parts[0].removePrefix("UID ").trim().let { uStr ->
                    if (uStr.startsWith("u0a")) {
                        10000 + (uStr.removePrefix("u0a").toIntOrNull() ?: 0)
                    } else {
                        uStr.toIntOrNull()
                    }
                }
                currentMah = parts.getOrNull(1)?.trim()?.split(" ")?.firstOrNull()?.toDoubleOrNull() ?: 0.0
                currentFg = 0L
                currentBg = 0L
            } else if (currentUid != null && trimmed.startsWith("cpu=")) {
                // parse times if present
            }
        }

        val lastUid = currentUid
        if (lastUid != null && currentMah > 0.0001) {
            val pkg = pm.getPackagesForUid(lastUid)?.firstOrNull()
            val label = getSystemUidLabel(lastUid, pkg)
            val drawable = pkg?.let { getAppIcon(pm, it) }
            list.add(BatteryUsageApp(lastUid, pkg, label, currentMah, currentFg, currentBg, drawable))
        }

        return list.sortedByDescending { it.powerMah }
    }

    fun parseWakeupHistory(context: Context): List<CpuWakeupItem> {
        val output = ShellUtils.runCommandWithOutput(context, "dumpsys batterystats --wakeups")
            ?: return emptyList()

        val list = mutableListOf<CpuWakeupItem>()
        var currentTimeAgo = ""

        output.lines().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.startsWith("-") && trimmed.endsWith(":")) {
                val rawTime = trimmed.removePrefix("-").removeSuffix(":")
                currentTimeAgo = formatReadableDuration(rawTime)
            } else if (trimmed.startsWith("Attribution:")) {
                val attr = trimmed.removePrefix("Attribution:").trim()
                val subsystem = when {
                    attr.contains("Alarm", ignoreCase = true) -> "Alarm"
                    attr.contains("Wifi", ignoreCase = true) -> "Wi-Fi"
                    attr.contains("Sensor", ignoreCase = true) -> "Sensor"
                    attr.contains("Cellular", ignoreCase = true) -> "Cellular Data"
                    else -> "Subsystem"
                }
                val icon = when (subsystem) {
                    "Alarm" -> R.drawable.rounded_info_24
                    "Wi-Fi" -> R.drawable.rounded_info_24
                    "Sensor" -> R.drawable.rounded_device_thermostat_24
                    "Cellular Data" -> R.drawable.rounded_cable_24
                    else -> R.drawable.rounded_info_24
                }
                list.add(CpuWakeupItem(currentTimeAgo, subsystem, attr, icon))
            }
        }

        return list.take(50)
    }

    private fun formatReadableDuration(raw: String): String {
        // Raw example: "1m9s227ms" or "2h12m56s324ms"
        var str = raw.substringBefore("ms")
        if (str.isEmpty()) str = raw
        return "$str ago"
    }

    private fun getAppName(pm: PackageManager, packageName: String): String {
        return try {
            val appInfo = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName
        }
    }

    private fun getAppIcon(pm: PackageManager, packageName: String): Drawable? {
        return try {
            pm.getApplicationIcon(packageName)
        } catch (e: Exception) {
            null
        }
    }
}

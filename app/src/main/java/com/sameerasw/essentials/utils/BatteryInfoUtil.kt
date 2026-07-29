package com.sameerasw.essentials.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.sameerasw.essentials.R

data class BatteryDetails(
    val level: Int,
    val scale: Int,
    val status: Int,
    val health: Int,
    val plugged: Int,
    val voltage: Int,
    val temperature: Int,
    val technology: String,
    val isPresent: Boolean,

    // Shell / sysfs attributes
    val chargeFull: Long? = null,
    val chargeFullDesign: Long? = null,
    val chargeCounter: Long? = null,
    val maxChargingCurrent: Int? = null,
    val maxChargingVoltage: Int? = null,
    val chargingState: Int? = null,
    val chargingPolicy: Int? = null,
    val capacityLevel: Int? = null,
    val currentNow: Long? = null,
    val voltageNow: Long? = null
)

object BatteryInfoUtil {

    fun getBatteryIntent(context: Context): Intent? {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        return context.registerReceiver(null, filter)
    }

    fun getBasicDetails(context: Context): BatteryDetails {
        val intent = getBatteryIntent(context)
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN) ?: BatteryManager.BATTERY_STATUS_UNKNOWN
        val health = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN) ?: BatteryManager.BATTERY_HEALTH_UNKNOWN
        val plugged = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        val voltage = intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) ?: 0
        val temp = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        val tech = intent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Li-ion"
        val present = intent?.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true) ?: true

        return BatteryDetails(
            level = level,
            scale = scale,
            status = status,
            health = health,
            plugged = plugged,
            voltage = voltage,
            temperature = temp,
            technology = tech,
            isPresent = present
        )
    }

    fun fetchAdvancedDetails(context: Context, basic: BatteryDetails): BatteryDetails {
        if (!ShellUtils.hasPermission(context)) return basic

        var chargeFull: Long? = readSysfsLong(context, "/sys/class/power_supply/battery/charge_full")
        var chargeFullDesign: Long? = readSysfsLong(context, "/sys/class/power_supply/battery/charge_full_design")
        var currentNow: Long? = readSysfsLong(context, "/sys/class/power_supply/battery/current_now")
        var voltageNow: Long? = readSysfsLong(context, "/sys/class/power_supply/battery/voltage_now")

        val dumpsysOutput = ShellUtils.runCommandWithOutput(context, "dumpsys battery")
        val dumpsysMap = parseDumpsysBattery(dumpsysOutput)

        if (chargeFull == null) {
            dumpsysMap["Charge counter"]?.toLongOrNull()?.let {
                // dumpsys sometimes gives charge counter or max capacity
            }
        }

        val chargeCounter = dumpsysMap["Charge counter"]?.toLongOrNull() ?: readSysfsLong(context, "/sys/class/power_supply/battery/charge_counter")
        val maxChargingCurrent = dumpsysMap["Max charging current"]?.toIntOrNull()
        val maxChargingVoltage = dumpsysMap["Max charging voltage"]?.toIntOrNull()
        val chargingState = dumpsysMap["Charging state"]?.toIntOrNull()
        val chargingPolicy = dumpsysMap["Charging policy"]?.toIntOrNull()
        val capacityLevel = dumpsysMap["Capacity level"]?.toIntOrNull()

        return basic.copy(
            chargeFull = chargeFull,
            chargeFullDesign = chargeFullDesign,
            chargeCounter = chargeCounter,
            maxChargingCurrent = maxChargingCurrent,
            maxChargingVoltage = maxChargingVoltage,
            chargingState = chargingState,
            chargingPolicy = chargingPolicy,
            capacityLevel = capacityLevel,
            currentNow = currentNow,
            voltageNow = voltageNow
        )
    }

    private fun readSysfsLong(context: Context, path: String): Long? {
        val out = ShellUtils.runCommandWithOutput(context, "cat $path") ?: return null
        return out.trim().toLongOrNull()
    }

    private fun parseDumpsysBattery(output: String?): Map<String, String> {
        if (output.isNullOrBlank()) return emptyMap()
        val map = mutableMapOf<String, String>()
        output.lines().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.contains(":")) {
                val parts = trimmed.split(":", limit = 2)
                if (parts.size == 2) {
                    map[parts[0].trim()] = parts[1].trim()
                }
            }
        }
        return map
    }

    fun getBatteryIconRes(
        context: Context,
        level: Int,
        isCharging: Boolean,
        status: Int = BatteryManager.BATTERY_STATUS_UNKNOWN,
        health: Int = BatteryManager.BATTERY_HEALTH_UNKNOWN,
        isPresent: Boolean = true,
        isPowerSave: Boolean = false
    ): Int {
        if (!isPresent || health == BatteryManager.BATTERY_HEALTH_OVERHEAT || health == BatteryManager.BATTERY_HEALTH_DEAD || health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE || health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
            return R.drawable.battery_android_frame_alert_24px
        }
        val isChargeLimitEnabled = try {
            android.provider.Settings.Secure.getInt(context.contentResolver, "charge_optimization_mode", 0) == 1
        } catch (e: Exception) {
            false
        }
        if (isCharging && level >= 80 && isChargeLimitEnabled) {
            return R.drawable.battery_android_frame_shield_24px
        }
        if (level >= 100) {
            return R.drawable.battery_android_frame_full_24px
        }
        if (isCharging) {
            return R.drawable.battery_android_frame_bolt_24px
        }
        if (isPowerSave) {
            return R.drawable.battery_android_frame_plus_24px
        }
        return when {
            level <= 0 -> R.drawable.battery_android_0_24px
            level <= 15 -> R.drawable.battery_android_frame_1_24px
            level <= 30 -> R.drawable.battery_android_frame_2_24px
            level <= 45 -> R.drawable.battery_android_frame_3_24px
            level <= 60 -> R.drawable.battery_android_frame_4_24px
            level <= 80 -> R.drawable.battery_android_frame_5_24px
            else -> R.drawable.battery_android_frame_6_24px
        }
    }

    fun formatStatus(status: Int): String {
        return when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not Charging"
            else -> "Unknown"
        }
    }

    fun formatHealth(health: Int): String {
        return when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified Failure"
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            else -> "Unknown"
        }
    }

    fun formatPlugged(plugged: Int): String {
        return when (plugged) {
            BatteryManager.BATTERY_PLUGGED_AC -> "AC Charger"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB Port"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
            BatteryManager.BATTERY_PLUGGED_DOCK -> "Dock"
            0 -> "Unplugged"
            else -> "Plugged"
        }
    }
}

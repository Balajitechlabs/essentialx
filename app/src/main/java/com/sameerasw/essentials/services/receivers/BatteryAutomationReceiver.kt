package com.sameerasw.essentials.services.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.utils.BatteryStatsUtil

class BatteryAutomationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == Intent.ACTION_POWER_DISCONNECTED) {
            checkAutoResetBatteryStats(context)
        }
    }

    private fun checkAutoResetBatteryStats(context: Context) {
        val repo = SettingsRepository(context)
        val autoResetEnabled = repo.getBoolean("auto_reset_battery_stats", false)
        if (autoResetEnabled) {
            val bm = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
            val level = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: -1
            if (level >= 90) {
                BatteryStatsUtil.resetStats(context)
            }
        }
    }
}

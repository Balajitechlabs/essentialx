package com.sameerasw.essentials.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sameerasw.essentials.data.repository.SettingsRepository

data class BatteryHistoryPoint(
    val timestamp: Long,
    val level: Int,
    val isPlugged: Boolean
)

object BatteryHistoryManager {
    private const val KEY_HISTORY_POINTS = "battery_history_points"
    private const val HOURLY_INTERVAL_MS = 45 * 60 * 1000L // 45 minutes

    fun getHistory(context: Context): List<BatteryHistoryPoint> {
        val repo = SettingsRepository(context)
        val json = repo.getString(KEY_HISTORY_POINTS, "[]")
        val lastResetTime = repo.getLong("last_battery_stats_reset_time", -1L)

        val list = try {
            val type = object : TypeToken<List<BatteryHistoryPoint>>() {}.type
            Gson().fromJson<List<BatteryHistoryPoint>>(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }

        val filtered = if (lastResetTime > 0L) {
            list.filter { it.timestamp >= lastResetTime }
        } else {
            list
        }

        return filtered.sortedBy { it.timestamp }
    }

    fun recordPoint(context: Context, level: Int, isPlugged: Boolean, forceRecord: Boolean = false) {
        val repo = SettingsRepository(context)
        val now = System.currentTimeMillis()
        val currentHistory = getHistory(context).toMutableList()

        if (currentHistory.isNotEmpty()) {
            val lastPoint = currentHistory.last()
            val stateChanged = lastPoint.isPlugged != isPlugged
            val timeElapsed = now - lastPoint.timestamp >= HOURLY_INTERVAL_MS

            if (!forceRecord && !stateChanged && !timeElapsed) {
                return
            }
        }

        currentHistory.add(BatteryHistoryPoint(now, level, isPlugged))

        // Keep last 100 
        val trimmedHistory = if (currentHistory.size > 100) {
            currentHistory.takeLast(100)
        } else {
            currentHistory
        }

        repo.putString(KEY_HISTORY_POINTS, Gson().toJson(trimmedHistory))
    }

    fun clearHistory(context: Context) {
        val repo = SettingsRepository(context)
        repo.putString(KEY_HISTORY_POINTS, "[]")
    }
}

/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Utilities
 * File: SettingsRecorder.kt
 * Description: Utility to snapshot and diff Android Settings tables (System, Secure, Global).
 */

package com.sameerasw.essentials.utils

import android.content.Context
import android.net.Uri
import android.provider.Settings
import com.sameerasw.essentials.domain.diy.Action

object SettingsRecorder {

    // Sensitive keys that might cause unexpected system disruptions
    val SENSITIVE_KEYS: Set<String> = setOf(
        "adb_enabled",
        "adb_wifi_enabled",
        "development_settings_enabled",
        "device_provisioned",
        "user_setup_complete",
    )

    fun isSensitiveKey(key: String): Boolean {
        return SENSITIVE_KEYS.any { it.equals(key, ignoreCase = true) }
    }

    fun snapshot(context: Context): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val resolver = context.contentResolver

        fun readTable(uri: Uri, table: Action.SettingsTable) {
            try {
                resolver.query(uri, arrayOf("name", "value"), null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex("name")
                    val valueIndex = cursor.getColumnIndex("value")
                    if (nameIndex != -1 && valueIndex != -1) {
                        while (cursor.moveToNext()) {
                            val name = cursor.getString(nameIndex)
                            val value = cursor.getString(valueIndex) ?: ""
                            if (name != null) {
                                result["${table.name}/$name"] = value
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore errors reading protected tables if permissions are restricted
            }
        }

        readTable(Settings.System.CONTENT_URI, Action.SettingsTable.SYSTEM)
        readTable(Settings.Secure.CONTENT_URI, Action.SettingsTable.SECURE)
        readTable(Settings.Global.CONTENT_URI, Action.SettingsTable.GLOBAL)

        return result
    }

    fun diff(
        before: Map<String, String>,
        after: Map<String, String>
    ): List<Action.SettingsEntry> {
        val changes = mutableListOf<Action.SettingsEntry>()

        for ((keyWithTable, afterValue) in after) {
            val beforeValue = before[keyWithTable]
            if (beforeValue != afterValue) {
                val parts = keyWithTable.split("/", limit = 2)
                if (parts.size == 2) {
                    val tableName = parts[0]
                    val key = parts[1]
                    val table = try {
                        Action.SettingsTable.valueOf(tableName)
                    } catch (e: Exception) {
                        Action.SettingsTable.SYSTEM
                    }
                    changes.add(
                        Action.SettingsEntry(
                            table = table,
                            key = key,
                            value = afterValue
                        )
                    )
                }
            }
        }

        return changes
    }
}

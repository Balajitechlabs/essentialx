/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Background Services & Receivers
 * File: WatchNotificationSyncManager.kt
 * Description: Manages syncing phone notifications to WearOS launcher shade via Wearable MessageClient.
 */

package com.sameerasw.essentials.services

import android.content.Context
import android.content.pm.PackageManager
import android.service.notification.StatusBarNotification
import android.util.Log
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import org.json.JSONObject

object WatchNotificationSyncManager {
    private const val TAG = "WatchNotifSyncManager"
    const val PATH_WATCH_NOTIFICATION = "/watch_notification"
    const val PATH_WATCH_NOTIFICATION_REMOVED = "/watch_notification_removed"

    fun isSyncEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("watch_notif_sync_enabled", false)
    }

    fun isSilentSyncEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("watch_notif_silent_enabled", false)
    }

    fun getAllowedApps(context: Context): Set<String> {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        val json = prefs.getString("watch_notif_allowed_apps", null) ?: return emptySet()
        return try {
            Gson().fromJson(json, Array<String>::class.java).toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    fun onNotificationPosted(context: Context, sbn: StatusBarNotification, isSilent: Boolean) {
        val enabled = isSyncEnabled(context)
        Log.d(TAG, "onNotificationPosted: pkg=${sbn.packageName}, isSyncEnabled=$enabled, isOngoing=${sbn.isOngoing}, isSilent=$isSilent")
        if (!enabled) return

        // Skip ongoing notifications (e.g. active downloads, media players, ongoing calls)
        if (sbn.isOngoing) {
            Log.d(TAG, "Skipping ongoing notification from ${sbn.packageName}")
            return
        }

        // Skip silent notifications if not enabled
        if (isSilent && !isSilentSyncEnabled(context)) {
            Log.d(TAG, "Skipping silent notification from ${sbn.packageName}")
            return
        }

        // Check app filter (empty allowed set = sync all apps)
        val allowedApps = getAllowedApps(context)
        if (allowedApps.isNotEmpty() && !allowedApps.contains(sbn.packageName)) {
            Log.d(TAG, "Skipping notification from ${sbn.packageName} - not in allowed apps ($allowedApps)")
            return
        }

        val extras = sbn.notification.extras ?: return
        var title = extras.getCharSequence("android.title")?.toString() ?: ""
        var text = extras.getCharSequence("android.text")?.toString()
            ?: extras.getCharSequence("android.bigText")?.toString()
            ?: sbn.notification.tickerText?.toString()
            ?: ""

        if (title.isBlank() && text.isBlank()) {
            val lines = extras.getCharSequenceArray("android.textLines")
            if (!lines.isNullOrEmpty()) {
                text = lines.joinToString("\n")
            }
        }

        if (title.isBlank() && text.isBlank()) {
            Log.d(TAG, "Skipping notification from ${sbn.packageName} - title and text are blank")
            return
        }

        val appName = try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(sbn.packageName, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            sbn.packageName
        }

        val jsonObj = JSONObject().apply {
            put("key", sbn.key)
            put("packageName", sbn.packageName)
            put("appName", appName)
            put("title", title)
            put("text", text)
            put("postTime", sbn.postTime)
        }

        Log.d(TAG, "Sending notification to watch: $jsonObj")
        sendMessageToWatch(context, PATH_WATCH_NOTIFICATION, jsonObj.toString().toByteArray())
    }

    fun onNotificationRemoved(context: Context, key: String) {
        if (!isSyncEnabled(context)) return
        Log.d(TAG, "Sending notification removed to watch: key=$key")
        sendMessageToWatch(context, PATH_WATCH_NOTIFICATION_REMOVED, key.toByteArray())
    }

    private fun sendMessageToWatch(context: Context, path: String, data: ByteArray) {
        val nodeClient = Wearable.getNodeClient(context)
        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            Log.d(TAG, "sendMessageToWatch: connected nodes count=${nodes.size}")
            if (nodes.isEmpty()) return@addOnSuccessListener
            val messageClient = Wearable.getMessageClient(context)
            for (node in nodes) {
                messageClient.sendMessage(node.id, path, data).addOnSuccessListener {
                    Log.d(TAG, "Message sent successfully to node ${node.displayName} path $path")
                }.addOnFailureListener { e ->
                    Log.e(TAG, "Failed to send message to node ${node.displayName} path $path", e)
                }
            }
        }.addOnFailureListener { e ->
            Log.e(TAG, "Failed to get connected nodes for watch notification sync", e)
        }
    }
}

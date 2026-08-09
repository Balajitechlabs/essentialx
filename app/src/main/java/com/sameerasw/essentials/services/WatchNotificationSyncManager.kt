/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Background Services & Receivers
 * File: WatchNotificationSyncManager.kt
 * Description: Manages syncing phone notifications to WearOS launcher shade via Wearable MessageClient.
 */

package com.sameerasw.essentials.services

import android.app.Notification
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

    private fun isMediaNotification(sbn: StatusBarNotification): Boolean {
        val category = sbn.notification.category
        if (category == Notification.CATEGORY_TRANSPORT) return true

        val extras = sbn.notification.extras ?: return false
        val template = extras.getString(Notification.EXTRA_TEMPLATE)
        if (template != null && (template.contains("MediaStyle") || template.contains("DecoratedMediaCustomViewStyle"))) {
            return true
        }
        return extras.containsKey(Notification.EXTRA_MEDIA_SESSION)
    }

    fun onNotificationPosted(context: Context, sbn: StatusBarNotification, isSilent: Boolean) {
        val enabled = isSyncEnabled(context)
        Log.d(TAG, "onNotificationPosted: pkg=${sbn.packageName}, isSyncEnabled=$enabled, isOngoing=${sbn.isOngoing}, isSilent=$isSilent")
        if (!enabled) return

        val isMedia = isMediaNotification(sbn)

        // Skip silent notifications if not enabled, unless it's a media playback notification
        if (isSilent && !isMedia && !isSilentSyncEnabled(context)) {
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
        var title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
            ?: extras.getCharSequence("android.media.title")?.toString()
            ?: ""

        var text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
            ?: extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString()
            ?: extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString()
            ?: extras.getCharSequence("android.artist")?.toString()
            ?: extras.getCharSequence("android.album")?.toString()
            ?: sbn.notification.tickerText?.toString()
            ?: ""

        if (title.isBlank() && text.isBlank()) {
            val lines = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
            if (!lines.isNullOrEmpty()) {
                text = lines.joinToString("\n")
            }
        }

        val appName = try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(sbn.packageName, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            sbn.packageName
        }

        if (title.isBlank() && text.isNotBlank()) {
            title = appName
        } else if (title.isNotBlank() && text.isBlank()) {
            text = appName
        }

        if (title.isBlank() && text.isBlank()) {
            Log.d(TAG, "Skipping notification from ${sbn.packageName} - title and text are blank")
            return
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

        // Ensure app icon is synced to watch for this package
        syncAppIcons(context, setOf(sbn.packageName))
    }

    fun onNotificationRemoved(context: Context, key: String) {
        if (!isSyncEnabled(context)) return
        Log.d(TAG, "Sending notification removed to watch: key=$key")
        sendMessageToWatch(context, PATH_WATCH_NOTIFICATION_REMOVED, key.toByteArray())
    }

    const val PATH_WATCH_APP_ICONS = "/watch_app_icons"

    fun syncAppIcons(context: Context, packageNames: Set<String>): Int {
        if (!isSyncEnabled(context)) return 0
        val pm = context.packageManager
        val iconsObj = JSONObject()
        var count = 0

        for (pkg in packageNames) {
            try {
                val appInfo = pm.getApplicationInfo(pkg, 0)
                val drawable = pm.getApplicationIcon(appInfo)
                val bitmap = if (drawable is android.graphics.drawable.BitmapDrawable) {
                    drawable.bitmap
                } else {
                    val bmp = android.graphics.Bitmap.createBitmap(
                        drawable.intrinsicWidth.coerceAtLeast(1),
                        drawable.intrinsicHeight.coerceAtLeast(1),
                        android.graphics.Bitmap.Config.ARGB_8888
                    )
                    val canvas = android.graphics.Canvas(bmp)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    bmp
                }
                val stream = java.io.ByteArrayOutputStream()
                val scaledBmp = android.graphics.Bitmap.createScaledBitmap(bitmap, 48, 48, true)
                scaledBmp.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
                val iconBase64 = android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.NO_WRAP)
                iconsObj.put(pkg, iconBase64)
                count++
            } catch (e: Exception) {
                Log.e(TAG, "Failed to extract icon for $pkg", e)
            }
        }

        if (count > 0) {
            Log.d(TAG, "Syncing $count app icons to watch")
            sendMessageToWatch(context, PATH_WATCH_APP_ICONS, iconsObj.toString().toByteArray())
        }
        return count
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

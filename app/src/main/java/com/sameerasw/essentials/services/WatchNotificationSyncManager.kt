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
import android.app.RemoteInput
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

object WatchNotificationSyncManager {
    private const val TAG = "WatchNotifSyncManager"
    const val PATH_WATCH_NOTIFICATION = "/watch_notification"
    const val PATH_WATCH_NOTIFICATION_REMOVED = "/watch_notification_removed"
    const val PATH_WATCH_ACTIVE_NOTIFICATIONS_SYNC = "/watch_active_notifications_sync"
    const val PATH_WATCH_SET_NOTIFICATION_SOUND = "/set_notification_sound"

    fun isSyncEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("watch_notif_sync_enabled", false)
    }

    fun ensureListenerServiceRunning(context: Context) {
        if (NotificationListener.instance == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                NotificationListenerService.requestRebind(
                    ComponentName(context, NotificationListener::class.java)
                )
                Log.d(TAG, "Requested rebind for NotificationListenerService")
            } catch (e: Exception) {
                Log.e(TAG, "Error requesting rebind for NotificationListenerService", e)
            }
        }
    }

    fun isSilentSyncEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("watch_notif_silent_enabled", false)
    }

    fun isMediaSyncEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("watch_notif_media_enabled", true)
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

    private fun isCallNotification(sbn: StatusBarNotification): Boolean {
        val category = sbn.notification.category
        if (category == Notification.CATEGORY_CALL) return true

        val extras = sbn.notification.extras
        if (extras != null) {
            val template = extras.getString(Notification.EXTRA_TEMPLATE)
            if (template != null && (template.contains("CallStyle") || template.contains("IncomingCallStyle"))) {
                return true
            }
        }

        val pkg = sbn.packageName.lowercase()
        return pkg.contains("dialer") || pkg.contains("incallui") || pkg.contains("telecom") || pkg.contains("telephony")
    }

    private fun canReplyToNotification(sbn: StatusBarNotification): Boolean {
        val actions = sbn.notification.actions ?: return false
        for (action in actions) {
            if (!action.remoteInputs.isNullOrEmpty()) return true
        }
        return false
    }

    fun onNotificationPosted(context: Context, sbn: StatusBarNotification, isSilent: Boolean) {
        ensureListenerServiceRunning(context)
        val enabled = isSyncEnabled(context)
        Log.d(TAG, "onNotificationPosted: pkg=${sbn.packageName}, isSyncEnabled=$enabled, isOngoing=${sbn.isOngoing}, isSilent=$isSilent")
        if (!enabled) return

        val isMedia = isMediaNotification(sbn)

        if (isCallNotification(sbn) && WatchCallSyncManager.isCallSyncEnabled(context)) {
            Log.d(TAG, "Skipping call notification from ${sbn.packageName} - handled via call sync")
            return
        }

        if (isMedia && !isMediaSyncEnabled(context)) {
            Log.d(TAG, "Skipping media notification from ${sbn.packageName} - media sync disabled")
            return
        }

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

        val postTime = if (sbn.postTime > 0) sbn.postTime else System.currentTimeMillis()
        val jsonObj = JSONObject().apply {
            put("key", sbn.key)
            put("packageName", sbn.packageName)
            put("appName", appName)
            put("title", title)
            put("text", text)
            put("postTime", postTime)
            put("isMedia", isMedia)
            put("canReply", canReplyToNotification(sbn))
        }

        Log.d(TAG, "Sending notification to watch: $jsonObj")
        sendMessageToWatch(context, PATH_WATCH_NOTIFICATION, jsonObj.toString().toByteArray())

        // Ensure app icon is synced to watch for this package
        syncAppIcons(context, setOf(sbn.packageName))
    }

    fun syncActiveNotifications(context: Context, activeNotifs: Array<StatusBarNotification>?): Int {
        if (!isSyncEnabled(context) || activeNotifs == null) return 0
        val allowedApps = getAllowedApps(context)
        val silentSyncEnabled = isSilentSyncEnabled(context)
        val mediaSyncEnabled = isMediaSyncEnabled(context)
        val listener = NotificationListener.instance
        val jsonArray = JSONArray()
        val pkgsToSync = mutableSetOf<String>()

        for (sbn in activeNotifs) {
            if (sbn.packageName == context.packageName) continue
            if (allowedApps.isNotEmpty() && !allowedApps.contains(sbn.packageName)) continue

            val isMedia = isMediaNotification(sbn)

            if (isCallNotification(sbn) && WatchCallSyncManager.isCallSyncEnabled(context)) {
                Log.d(TAG, "Skipping call notification from ${sbn.packageName} during manual sync - handled via call sync")
                continue
            }

            if (isMedia && !mediaSyncEnabled) {
                Log.d(TAG, "Skipping media notification from ${sbn.packageName} during manual sync")
                continue
            }

            val isSilent = listener?.isSilentNotification(sbn) ?: false

            // Skip silent notifications if not enabled, unless it's a media playback notification
            if (isSilent && !isMedia && !silentSyncEnabled) {
                Log.d(TAG, "Skipping silent notification from ${sbn.packageName} during manual sync")
                continue
            }

            val extras = sbn.notification.extras ?: continue
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

            if (title.isBlank() && text.isNotBlank()) title = appName
            else if (title.isNotBlank() && text.isBlank()) text = appName

            if (title.isBlank() && text.isBlank()) continue

            val postTime = if (sbn.postTime > 0) sbn.postTime else System.currentTimeMillis()
            val jsonObj = JSONObject().apply {
                put("key", sbn.key)
                put("packageName", sbn.packageName)
                put("appName", appName)
                put("title", title)
                put("text", text)
                put("postTime", postTime)
                put("isMedia", isMedia)
                put("canReply", canReplyToNotification(sbn))
            }
            jsonArray.put(jsonObj)
            pkgsToSync.add(sbn.packageName)
        }

        Log.d(TAG, "Syncing ${jsonArray.length()} active notifications to watch")
        sendMessageToWatch(context, PATH_WATCH_ACTIVE_NOTIFICATIONS_SYNC, jsonArray.toString().toByteArray())
        if (pkgsToSync.isNotEmpty()) {
            syncAppIcons(context, pkgsToSync)
        }
        return jsonArray.length()
    }

    fun handleReplyFromWatch(context: Context, jsonStr: String) {
        try {
            val jsonObj = JSONObject(jsonStr)
            val key = jsonObj.optString("key")
            val replyText = jsonObj.optString("replyText")
            if (key.isBlank() || replyText.isBlank()) return

            val listener = NotificationListener.instance ?: return
            val sbn = listener.activeNotifications?.find { it.key == key } ?: return
            val actions = sbn.notification.actions ?: return

            for (action in actions) {
                val remoteInputs = action.remoteInputs ?: continue
                if (remoteInputs.isNotEmpty()) {
                    val intent = Intent()
                    val bundle = android.os.Bundle()
                    for (remoteInput in remoteInputs) {
                        bundle.putCharSequence(remoteInput.resultKey, replyText)
                    }
                    RemoteInput.addResultsToIntent(remoteInputs, intent, bundle)
                    action.actionIntent.send(context, 0, intent)
                    Log.d(TAG, "Successfully replied to notification $key: $replyText")
                    break
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling notification reply from watch", e)
        }
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

    fun setWatchNotificationSound(context: Context, soundName: String) {
        Log.d(TAG, "setWatchNotificationSound: $soundName")
        sendMessageToWatch(context, PATH_WATCH_SET_NOTIFICATION_SOUND, soundName.toByteArray())
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

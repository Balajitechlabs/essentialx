/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Background Services & Receivers
 * File: DownloadUpdateReceiver.kt
 * Description: Background service component for DownloadUpdateReceiver.kt.
 */

package com.sameerasw.essentials.services.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sameerasw.essentials.utils.AutoUpdateManagerHelper
import com.sameerasw.essentials.utils.UpdateNotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        if (intent.action == "com.sameerasw.essentials.ACTION_DOWNLOAD_UPDATE") {
            val downloadUrl = intent.getStringExtra("download_url") ?: return
            val version = intent.getStringExtra("version") ?: ""

            val apkNameExtra = intent.getStringExtra("apk_name")
            val notifId = intent.getIntExtra("notification_id", 7001)

            val pendingResult = goAsync()
            val helper = AutoUpdateManagerHelper(context)
            val cleanVersion = version.replace(Regex("[^a-zA-Z0-9]"), "_")
            val targetApkName = apkNameExtra ?: "Essentials_$cleanVersion"

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    helper.downloadAndInstallApk(
                        apkUrl = downloadUrl,
                        apkName = targetApkName,
                        onProgressUpdate = { progress ->
                            UpdateNotificationHelper.showDownloadProgressNotification(
                                context = context,
                                version = version,
                                progress = progress,
                                notificationId = notifId,
                            )
                        },
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    UpdateNotificationHelper.cancelNotification(context, notifId)
                    pendingResult.finish()
                }
            }
        }
    }
}

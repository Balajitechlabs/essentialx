/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Utilities - General
 * File: UpdateNotificationHelper.kt
 * Description: Utility helper for UpdateNotificationHelper.kt.
 */

package com.sameerasw.essentials.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.sameerasw.essentials.MainActivity
import com.sameerasw.essentials.R
import com.sameerasw.essentials.services.receivers.DownloadUpdateReceiver
import com.sameerasw.essentials.ui.activities.YourAndroidActivity

object UpdateNotificationHelper {
    private const val CHANNEL_ID = "app_updates"
    private const val NOTIFICATION_ID = 1001

    fun hasNotificationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS,
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.update_channel_name)
            val descriptionText = context.getString(R.string.update_channel_desc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showUpdateNotification(
        context: Context,
        version: String,
        downloadUrl: String,
    ) {
        if (!hasNotificationPermission(context)) return
        createNotificationChannel(context)

        val mainIntent =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("show_update_sheet", true)
            }
        val contentPendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val downloadIntent =
            Intent(context, DownloadUpdateReceiver::class.java).apply {
                action = "com.sameerasw.essentials.ACTION_DOWNLOAD_UPDATE"
                putExtra("download_url", downloadUrl)
                putExtra("version", version)
            }
        val downloadPendingIntent =
            PendingIntent.getBroadcast(
                context,
                1,
                downloadIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val builder =
            NotificationCompat
                .Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(context.getString(R.string.notification_update_available))
                .setContentText(context.getString(R.string.notification_update_subtext, version))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .addAction(
                    R.drawable.rounded_mobile_arrow_down_24,
                    context.getString(R.string.action_download),
                    downloadPendingIntent,
                )

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private const val GROUP_REPO_UPDATES = "com.sameerasw.essentials.TRACKED_REPO_UPDATES"

    fun showTrackedRepoUpdateNotification(
        context: Context,
        repoName: String,
        repoFullName: String,
        version: String,
        downloadUrl: String,
        releaseNotes: String? = null,
    ) {
        if (!hasNotificationPermission(context)) return
        createNotificationChannel(context)

        val notifId = (repoFullName.hashCode() and 0x7FFFFFFF)
        val mainIntent =
            Intent(context, YourAndroidActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("repo_full_name", repoFullName)
            }
        val contentPendingIntent =
            PendingIntent.getActivity(
                context,
                notifId,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val subtext = context.getString(R.string.notification_tracked_repo_update_subtext, version)
        val builder =
            NotificationCompat
                .Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(context.getString(R.string.notification_tracked_repo_update_title, repoName))
                .setContentText(subtext)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .setGroup(GROUP_REPO_UPDATES)

        if (!releaseNotes.isNullOrBlank()) {
            val preview = releaseNotes.take(300).trim()
            builder.setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(context.getString(R.string.notification_tracked_repo_update_title, repoName))
                    .setSummaryText(version)
                    .bigText(preview),
            )
        }

        if (downloadUrl.isNotEmpty()) {
            val downloadIntent =
                Intent(context, DownloadUpdateReceiver::class.java).apply {
                    action = "com.sameerasw.essentials.ACTION_DOWNLOAD_UPDATE"
                    putExtra("download_url", downloadUrl)
                    putExtra("version", version)
                    putExtra("apk_name", "${repoName}_$version")
                    putExtra("notification_id", notifId)
                }
            val downloadPendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    notifId + 1,
                    downloadIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            builder.addAction(
                R.drawable.rounded_mobile_arrow_down_24,
                context.getString(R.string.action_download),
                downloadPendingIntent,
            )
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notifId, builder.build())
    }

    fun showDownloadProgressNotification(
        context: Context,
        version: String,
        progress: Int,
        notificationId: Int = NOTIFICATION_ID,
        title: String = context.getString(R.string.notification_update_available),
    ) {
        if (progress >= 100) {
            cancelNotification(context, notificationId)
            return
        }
        createNotificationChannel(context)

        val mainIntent =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        val contentPendingIntent =
            PendingIntent.getActivity(
                context,
                notificationId,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val builder =
            NotificationCompat
                .Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(context.getString(R.string.downloading_update_progress, progress))
                .setProgress(100, progress, false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(contentPendingIntent)

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    fun cancelNotification(
        context: Context,
        notificationId: Int = NOTIFICATION_ID,
    ) {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}

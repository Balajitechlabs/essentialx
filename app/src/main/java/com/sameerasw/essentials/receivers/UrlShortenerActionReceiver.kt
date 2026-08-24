/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Utilities - URL Shortener
 * File: UrlShortenerActionReceiver.kt
 * Description: BroadcastReceiver for instant Copy & Share actions from the URL Shortener Android notification.
 */

package com.sameerasw.essentials.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.sameerasw.essentials.R
import com.sameerasw.essentials.utils.UrlShortener

class UrlShortenerActionReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_COPY = "com.sameerasw.essentials.action.COPY_SHORT_URL"
        const val ACTION_SHARE = "com.sameerasw.essentials.action.SHARE_SHORT_URL"
        const val EXTRA_URL = "extra_short_url"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val shortUrl = intent.getStringExtra(EXTRA_URL) ?: return
        val notifId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, UrlShortener.NOTIFICATION_ID)

        try {
            when (intent.action) {
                ACTION_COPY -> {
                    try {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Shortened Link", shortUrl)
                        clipboard.setPrimaryClip(clip)

                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, context.getString(R.string.action_copy_clipboard), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.w("UrlShortenerReceiver", "Failed to copy to clipboard", e)
                    }

                    // Dismiss notification on copy
                    try {
                        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        nm.cancel(notifId)
                    } catch (e: Exception) {
                        Log.w("UrlShortenerReceiver", "Failed to cancel notification", e)
                    }
                }

                ACTION_SHARE -> {
                    try {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shortUrl)
                            type = "text/plain"
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        val chooser = Intent.createChooser(sendIntent, null).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(chooser)
                    } catch (e: Exception) {
                        Log.w("UrlShortenerReceiver", "Failed to launch share chooser", e)
                    }

                    // Dismiss notification on share
                    try {
                        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        nm.cancel(notifId)
                    } catch (e: Exception) {
                        Log.w("UrlShortenerReceiver", "Failed to cancel notification", e)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("UrlShortenerReceiver", "Unexpected receiver exception", e)
        }
    }
}

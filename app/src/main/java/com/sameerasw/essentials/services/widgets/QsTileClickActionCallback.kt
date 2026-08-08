/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Background Services & Receivers
 * File: QsTileClickActionCallback.kt
 * Description: Background service component for QsTileClickActionCallback.kt.
 */

package com.sameerasw.essentials.services.widgets

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.sameerasw.essentials.services.receivers.QsTileActionRouter

class QsTileClickActionCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val serviceClassName = parameters[SERVICE_CLASS_KEY] ?: return
        // action
        val intent = Intent(context, QsTileActionRouter::class.java).apply {
            action = QsTileActionRouter.ACTION_TRIGGER_TILE
            putExtra(QsTileActionRouter.EXTRA_SERVICE_CLASS_NAME, serviceClassName)
        }
        context.sendBroadcast(intent)
    }

    companion object {
        val SERVICE_CLASS_KEY = ActionParameters.Key<String>("service_class_name")
    }
}

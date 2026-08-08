/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Background Services & Receivers
 * File: QsTileActionRouter.kt
 * Description: Background service component for QsTileActionRouter.kt.
 */

package com.sameerasw.essentials.services.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sameerasw.essentials.MainActivity
import com.sameerasw.essentials.domain.controller.CaffeinateController
import com.sameerasw.essentials.services.tiles.BaseTileService
import com.sameerasw.essentials.services.tiles.CaffeinateTileService
import com.sameerasw.essentials.services.tiles.FlashlightTileService
import com.sameerasw.essentials.utils.HapticUtil

class QsTileActionRouter : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_TRIGGER_TILE) return

        val serviceClassName = intent.getStringExtra(EXTRA_SERVICE_CLASS_NAME) ?: return
        Log.d("QsTileActionRouter", "Triggering tile action for: $serviceClassName")

        // Perform haptics on click trigger
        HapticUtil.performHapticForService(context)

        try {
            when (serviceClassName) {
                CaffeinateTileService::class.java.name -> {
                    if (CaffeinateController.isStarting.value) {
                        CaffeinateController.cycleTimeout(context)
                    } else {
                        CaffeinateController.toggle(context)
                    }
                }

                FlashlightTileService::class.java.name -> {
                    val flashlightIntent =
                        Intent(context, FlashlightActionReceiver::class.java).apply {
                            action = FlashlightActionReceiver.ACTION_TOGGLE
                        }
                    context.sendBroadcast(flashlightIntent)
                }

                else -> {
                    val clazz = Class.forName(serviceClassName)
                    if (BaseTileService::class.java.isAssignableFrom(clazz)) {
                        val tileService =
                            clazz.getDeclaredConstructor().newInstance() as BaseTileService

                        val attachBaseContextMethod =
                            android.content.ContextWrapper::class.java.getDeclaredMethod(
                                "attachBaseContext",
                                Context::class.java
                            )
                        attachBaseContextMethod.isAccessible = true
                        attachBaseContextMethod.invoke(tileService, context)

                        tileService.onTileClick()
                    } else {
                        fallbackLaunch(context)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("QsTileActionRouter", "Failed to trigger tile $serviceClassName headlessly", e)
            fallbackLaunch(context)
        }

        // Notify widget to update state after action
        val updateIntent = Intent("com.sameerasw.essentials.action.QS_TILES_WIDGET_UPDATE").apply {
            setPackage(context.packageName)
        }
        context.sendBroadcast(updateIntent)
    }

    private fun fallbackLaunch(context: Context) {
        try {
            val launchIntent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(launchIntent)
        } catch (e: Exception) {
            Log.e("QsTileActionRouter", "Failed to launch activity", e)
        }
    }

    companion object {
        const val ACTION_TRIGGER_TILE = "com.sameerasw.essentials.action.TRIGGER_QS_TILE"
        const val EXTRA_SERVICE_CLASS_NAME = "extra_service_class_name"
    }
}

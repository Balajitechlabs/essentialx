/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: NFC / HCE
 * File: MeDropNfcManager.kt
 */

package com.sameerasw.essentials.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.cardemulation.CardEmulation
import com.sameerasw.essentials.domain.model.MeDropSettings
import com.sameerasw.essentials.services.MeDropHceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MeDropNfcManager {

    fun isNfcAvailable(context: Context): Boolean =
        NfcAdapter.getDefaultAdapter(context) != null

    fun isNfcEnabled(context: Context): Boolean =
        NfcAdapter.getDefaultAdapter(context)?.isEnabled == true

    suspend fun startBroadcast(activity: Activity, settings: MeDropSettings) {
        val contact = settings.contact ?: return
        val activeType = settings.activeProfileType
        val activeEntries = settings.getEffectiveEntryIds(activeType)
        val photoUri = settings.getEffectivePhotoUri(activeType)
        val context = activity.applicationContext

        val vCard = withContext(Dispatchers.IO) {
            contact.toVCard(
                context = context,
                activeEntryIds = activeEntries,
                customPhotoUri = photoUri
            )
        }

        withContext(Dispatchers.IO) {
            MeDropHceService.prepareVCard(vCard)
        }

        // setPreferredService must be called on Main thread with foreground Activity
        withContext(Dispatchers.Main) {
            val component = ComponentName(context, MeDropHceService::class.java)
            val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
            if (nfcAdapter != null && !activity.isFinishing && !activity.isDestroyed) {
                try {
                    val cardEmulation = CardEmulation.getInstance(nfcAdapter)
                    cardEmulation.setPreferredService(activity, component)
                } catch (_: Exception) {}
            }
        }
    }

    suspend fun stopBroadcast(activity: Activity) {
        val context = activity.applicationContext

        withContext(Dispatchers.Main) {
            val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
            if (nfcAdapter != null) {
                try {
                    val cardEmulation = CardEmulation.getInstance(nfcAdapter)
                    cardEmulation.unsetPreferredService(activity)
                } catch (_: Exception) {}
            }
        }

        withContext(Dispatchers.IO) {
            MeDropHceService.clearVCard()
        }
    }

    suspend fun startBroadcast(context: Context, settings: MeDropSettings) {
        if (context is Activity) {
            startBroadcast(context, settings)
        } else {
            val contact = settings.contact ?: return
            val activeType = settings.activeProfileType
            val activeEntries = settings.getEffectiveEntryIds(activeType)
            val photoUri = settings.getEffectivePhotoUri(activeType)

            val vCard = withContext(Dispatchers.IO) {
                contact.toVCard(
                    context = context,
                    activeEntryIds = activeEntries,
                    customPhotoUri = photoUri
                )
            }

            withContext(Dispatchers.IO) {
                MeDropHceService.prepareVCard(vCard)
            }
        }
    }

    suspend fun stopBroadcast(context: Context) {
        if (context is Activity) {
            stopBroadcast(context)
        } else {
            withContext(Dispatchers.IO) {
                MeDropHceService.clearVCard()
            }
        }
    }
}

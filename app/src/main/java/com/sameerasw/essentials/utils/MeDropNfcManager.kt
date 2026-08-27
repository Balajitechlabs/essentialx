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
import com.sameerasw.essentials.domain.model.MeDropContact
import com.sameerasw.essentials.domain.model.MeDropSettings
import com.sameerasw.essentials.services.MeDropHceService

object MeDropNfcManager {

    fun isNfcAvailable(context: Context): Boolean =
        NfcAdapter.getDefaultAdapter(context) != null

    fun isNfcEnabled(context: Context): Boolean =
        NfcAdapter.getDefaultAdapter(context)?.isEnabled == true

    fun startBroadcast(activity: Activity, settings: MeDropSettings) {
        val contact = settings.contact ?: return
        val activeType = settings.activeProfileType
        val activeEntries = settings.getEffectiveEntryIds(activeType)
        val photoUri = settings.getEffectivePhotoUri(activeType)

        val vCard = contact.toVCard(
            context = activity.applicationContext,
            activeEntryIds = activeEntries,
            customPhotoUri = photoUri
        )

        val context = activity.applicationContext
        MeDropHceService.prepareVCard(vCard)
        val pm = context.packageManager
        val component = ComponentName(context, MeDropHceService::class.java)
        pm.setComponentEnabledSetting(
            component,
            android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            android.content.pm.PackageManager.DONT_KILL_APP
        )

        // Set as preferred service to override other apps (like Twitter/X) using the same NDEF AID
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        if (nfcAdapter != null) {
            val cardEmulation = CardEmulation.getInstance(nfcAdapter)
            cardEmulation.setPreferredService(activity, component)
        }
    }

    fun stopBroadcast(activity: Activity) {
        val context = activity.applicationContext
        MeDropHceService.clearVCard()

        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        if (nfcAdapter != null) {
            val cardEmulation = CardEmulation.getInstance(nfcAdapter)
            cardEmulation.unsetPreferredService(activity)
        }

        val pm = context.packageManager
        val component = ComponentName(context, MeDropHceService::class.java)
        pm.setComponentEnabledSetting(
            component,
            android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            android.content.pm.PackageManager.DONT_KILL_APP
        )
    }

    fun startBroadcast(context: Context, settings: MeDropSettings) {
        if (context is Activity) {
            startBroadcast(context, settings)
        } else {
            val contact = settings.contact ?: return
            val activeType = settings.activeProfileType
            val activeEntries = settings.getEffectiveEntryIds(activeType)
            val photoUri = settings.getEffectivePhotoUri(activeType)

            val vCard = contact.toVCard(
                context = context,
                activeEntryIds = activeEntries,
                customPhotoUri = photoUri
            )

            MeDropHceService.prepareVCard(vCard)
            val pm = context.packageManager
            val component = ComponentName(context, MeDropHceService::class.java)
            pm.setComponentEnabledSetting(
                component,
                android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                android.content.pm.PackageManager.DONT_KILL_APP
            )
        }
    }

    fun stopBroadcast(context: Context) {
        if (context is Activity) {
            stopBroadcast(context)
        } else {
            MeDropHceService.clearVCard()
            val pm = context.packageManager
            val component = ComponentName(context, MeDropHceService::class.java)
            pm.setComponentEnabledSetting(
                component,
                android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                android.content.pm.PackageManager.DONT_KILL_APP
            )
        }
    }
}

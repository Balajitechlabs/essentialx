/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Shizuku / Telephony
 * File: CarrierConfigModifier.kt
 * Description: Privileged Instrumentation to override CarrierConfig (e.g. Carrier Name) via Shizuku shell delegation.
 */

package com.sameerasw.essentials.shizuku

import android.app.Activity
import android.app.IActivityManager
import android.app.Instrumentation
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.os.Process
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.util.Log
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper

class CarrierConfigModifier : Instrumentation() {
    override fun onCreate(arguments: Bundle?) {
        try {
            HiddenApiBypass.setHiddenApiExemptions("")
        } catch (_: Throwable) {
        }
        super.onCreate(arguments)
        val result = Bundle()
        if (arguments == null) {
            result.putBoolean(BUNDLE_RESULT, false)
            result.putString(BUNDLE_RESULT_MSG, "Arguments bundle is null")
            finish(Activity.RESULT_CANCELED, result)
            return
        }

        var index = 0
        val maxRetries = 50
        while (!Shizuku.pingBinder()) {
            index++
            try {
                Thread.sleep(100)
            } catch (_: InterruptedException) {
                break
            }
            if (index >= maxRetries) {
                break
            }
        }

        if (index >= maxRetries) {
            result.putBoolean(BUNDLE_RESULT, false)
            result.putString(BUNDLE_RESULT_MSG, "Shizuku binder not ready")
            finish(Activity.RESULT_OK, result)
            return
        }

        try {
            overrideConfig(arguments)
            result.putBoolean(BUNDLE_RESULT, true)
        } catch (t: Throwable) {
            Log.e(TAG, "Failed to override carrier config", t)
            result.putBoolean(BUNDLE_RESULT, false)
            result.putString(BUNDLE_RESULT_MSG, t.message ?: t.javaClass.simpleName)
        }
        finish(Activity.RESULT_OK, result)
    }

    @Throws(Exception::class)
    private fun overrideConfig(arguments: Bundle) {
        val binder =
            SystemServiceHelper.getSystemService(Context.ACTIVITY_SERVICE)
                ?: throw IllegalStateException("Activity service binder not found")
        val am = IActivityManager.Stub.asInterface(ShizukuBinderWrapper(binder))

        am.startDelegateShellPermissionIdentity(Process.myUid(), null)

        try {
            val cm = context.getSystemService(CarrierConfigManager::class.java)
            val sm = context.getSystemService(SubscriptionManager::class.java)

            val selectedSubId = arguments.getInt(BUNDLE_SELECT_SIM_ID, -1)
            arguments.remove(BUNDLE_SELECT_SIM_ID)

            val subIds: IntArray =
                if (selectedSubId == -1) {
                    sm.javaClass.getMethod("getActiveSubscriptionIdList").invoke(sm) as IntArray
                } else {
                    intArrayOf(selectedSubId)
                }

            val values = toPersistableBundle(arguments)

            for (subId in subIds) {
                try {
                    cm.javaClass
                        .getMethod(
                            "overrideConfig",
                            Int::class.javaPrimitiveType,
                            PersistableBundle::class.java,
                            Boolean::class.javaPrimitiveType,
                        ).invoke(cm, subId, values, false)
                } catch (_: NoSuchMethodException) {
                    cm.javaClass
                        .getMethod(
                            "overrideConfig",
                            Int::class.javaPrimitiveType,
                            PersistableBundle::class.java,
                        ).invoke(cm, subId, values)
                }
            }
        } finally {
            try {
                am.stopDelegateShellPermissionIdentity()
            } catch (_: Throwable) {
            }
        }
    }

    private fun toPersistableBundle(bundle: Bundle): PersistableBundle {
        val pb = PersistableBundle()
        for (key in bundle.keySet()) {
            @Suppress("DEPRECATION")
            when (val value = bundle[key]) {
                is Int -> pb.putInt(key, value)
                is Long -> pb.putLong(key, value)
                is Double -> pb.putDouble(key, value)
                is String -> pb.putString(key, value)
                is Boolean -> pb.putBoolean(key, value)
                is IntArray -> pb.putIntArray(key, value)
                is LongArray -> pb.putLongArray(key, value)
                is DoubleArray -> pb.putDoubleArray(key, value)
                is BooleanArray -> pb.putBooleanArray(key, value)
            }
        }
        return pb
    }

    companion object {
        private const val TAG = "CarrierConfigModifier"
        const val BUNDLE_SELECT_SIM_ID = "select_sim_id"
        const val BUNDLE_RESULT = "result"
        const val BUNDLE_RESULT_MSG = "result_msg"

        fun buildOverrideBundle(
            subId: Int,
            carrierName: String?,
        ): Bundle =
            Bundle().apply {
                putInt(BUNDLE_SELECT_SIM_ID, subId)
                if (!carrierName.isNullOrBlank()) {
                    putBoolean(CarrierConfigManager.KEY_CARRIER_NAME_OVERRIDE_BOOL, true)
                    putString(CarrierConfigManager.KEY_CARRIER_NAME_STRING, carrierName)
                    putString(CarrierConfigManager.KEY_CARRIER_CONFIG_VERSION_STRING, System.currentTimeMillis().toString())
                } else {
                    putBoolean(CarrierConfigManager.KEY_CARRIER_NAME_OVERRIDE_BOOL, false)
                    putString(CarrierConfigManager.KEY_CARRIER_NAME_STRING, "")
                    putString(CarrierConfigManager.KEY_CARRIER_CONFIG_VERSION_STRING, System.currentTimeMillis().toString())
                }
            }

        fun buildResetBundle(subId: Int): Bundle =
            Bundle().apply {
                putInt(BUNDLE_SELECT_SIM_ID, subId)
                putBoolean(CarrierConfigManager.KEY_CARRIER_NAME_OVERRIDE_BOOL, false)
                putString(CarrierConfigManager.KEY_CARRIER_NAME_STRING, "")
                putString(CarrierConfigManager.KEY_CARRIER_CONFIG_VERSION_STRING, System.currentTimeMillis().toString())
            }
    }
}

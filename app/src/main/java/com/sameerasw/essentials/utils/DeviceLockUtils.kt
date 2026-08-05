package com.sameerasw.essentials.utils

import android.accessibilityservice.AccessibilityService
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.KeyEvent
import android.widget.Toast
import com.sameerasw.essentials.R
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.domain.ScreenOffMethod
import com.sameerasw.essentials.services.tiles.ScreenOffAccessibilityService

object DeviceLockUtils {

    fun performLockdownTileAction(context: Context, isLongPress: Boolean): Boolean {
        val isLockdownEnabled = SettingsRepository(context).getBoolean(SettingsRepository.KEY_LOCKDOWN_MODE, false)
        val prefs = context.getSharedPreferences("essentials_prefs", Context.MODE_PRIVATE)

        val selectedScreenOffMethod = try {
            ScreenOffMethod.valueOf(
                prefs.getString("screen_off_method", ScreenOffMethod.ACCESSIBILITY.name)
                    ?: ScreenOffMethod.ACCESSIBILITY.name
            )
        } catch (e: Exception) {
            ScreenOffMethod.ACCESSIBILITY
        }

        val methodToExecute = if (!isLongPress) {
            if (isLockdownEnabled) ScreenOffMethod.DEVICE_ADMIN else selectedScreenOffMethod
        } else {
            if (isLockdownEnabled) selectedScreenOffMethod else ScreenOffMethod.DEVICE_ADMIN
        }

        return lockDevice(context, methodToExecute)
    }

    fun lockDevice(context: Context, method: ScreenOffMethod): Boolean {
        return when (method) {
            ScreenOffMethod.ACCESSIBILITY -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.msg_feature_not_supported),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (!PermissionUtils.isAccessibilityServiceEnabled(context)) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.msg_missing_accessibility_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (ScreenOffAccessibilityService.instance != null) {
                    performAccessibilityLock(ScreenOffAccessibilityService.instance!!)
                } else {
                    val serviceIntent = Intent(context, ScreenOffAccessibilityService::class.java).apply {
                        action = "LOCK_SCREEN"
                    }
                    context.startService(serviceIntent)
                }
                true
            }

            ScreenOffMethod.DEVICE_ADMIN -> {
                if (!PermissionUtils.isDeviceAdminActive(context)) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.msg_device_admin_required_lockdown),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                dpm.lockNow()
                true
            }

            ScreenOffMethod.INPUT -> {
                if (!ShellUtils.hasPermission(context)) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.msg_shizuku_root_required_lock),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                ShellUtils.runCommand(context, "input keyevent ${KeyEvent.KEYCODE_POWER}")
                true
            }
        }
    }

    fun performAccessibilityLock(service: AccessibilityService): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN)
        } else {
            false
        }
    }
}

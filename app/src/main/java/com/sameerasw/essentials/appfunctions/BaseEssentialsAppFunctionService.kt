package com.sameerasw.essentials.appfunctions

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.BatteryManager
import androidx.annotation.RequiresApi
import androidx.appfunctions.*
import com.sameerasw.essentials.appfunctions.dto.AppFunctionResult
import com.sameerasw.essentials.appfunctions.dto.AutomationSummary
import com.sameerasw.essentials.appfunctions.dto.CreateAutomationParams
import com.sameerasw.essentials.appfunctions.dto.DeviceStatusResponse
import com.sameerasw.essentials.appfunctions.dto.FreezeTagSummary
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.domain.ScreenOffMethod
import com.sameerasw.essentials.domain.controller.CaffeinateController
import com.sameerasw.essentials.domain.diy.Action
import com.sameerasw.essentials.domain.diy.Automation
import com.sameerasw.essentials.domain.diy.DIYRepository
import com.sameerasw.essentials.domain.diy.State
import com.sameerasw.essentials.domain.diy.Trigger
import com.sameerasw.essentials.services.automation.executors.CombinedActionExecutor
import com.sameerasw.essentials.services.tiles.ScreenOffAccessibilityService
import com.sameerasw.essentials.utils.DeviceLockUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

@RequiresApi(36)
@AppFunctionServiceEntryPoint(
    serviceName = "EssentialsAppFunctionService",
    appFunctionXmlFileName = "essentials_app_function_service"
)
abstract class BaseEssentialsAppFunctionService : AppFunctionService() {

    /**
     * Toggles the device flashlight on or off.
     *
     * @param enabled True to turn on the flashlight, false to turn it off.
     * @return Result indicating whether the operation succeeded.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun toggleFlashlight(enabled: Boolean): AppFunctionResult = withContext(Dispatchers.IO) {
        val action = if (enabled) Action.TurnOnFlashlight else Action.TurnOffFlashlight
        CombinedActionExecutor.execute(applicationContext, action)
        AppFunctionResult(true, "Flashlight set to ${if (enabled) "ON" else "OFF"}")
    }

    /**
     * Toggles Caffeinate mode to keep the device screen awake.
     *
     * @param enabled True to activate Caffeinate mode, false to stop it.
     * @return Result indicating whether Caffeinate state changed.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun toggleCaffeinate(enabled: Boolean): AppFunctionResult = withContext(Dispatchers.IO) {
        if (enabled) {
            CaffeinateController.toggle(applicationContext)
        } else {
            CaffeinateController.cancelAll(applicationContext)
        }
        AppFunctionResult(true, "Caffeinate ${if (enabled) "activated" else "deactivated"}")
    }

    /**
     * Sets the device ringer or sound mode.
     *
     * @param mode Target sound mode: "SOUND", "VIBRATE", or "SILENT".
     * @return Result of setting the sound mode.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun setSoundMode(mode: String): AppFunctionResult = withContext(Dispatchers.IO) {
        val soundModeType = when (mode.uppercase()) {
            "VIBRATE" -> Action.SoundModeType.VIBRATE
            "SILENT" -> Action.SoundModeType.SILENT
            else -> Action.SoundModeType.SOUND
        }
        CombinedActionExecutor.execute(applicationContext, Action.SoundMode(soundModeType))
        AppFunctionResult(true, "Sound mode set to ${soundModeType.name}")
    }

    /**
     * Returns the overall status of the device, including battery level, charging state, sound mode, flashlight, and caffeinate.
     *
     * @return Current status details of the device.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun getDeviceStatus(): DeviceStatusResponse = withContext(Dispatchers.IO) {
        val context = applicationContext
        val repo = SettingsRepository(context)

        val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = if (level >= 0 && scale > 0) (level * 100 / scale.toFloat()).toInt() else 0
        val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val soundModeStr = when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_VIBRATE -> "VIBRATE"
            AudioManager.RINGER_MODE_SILENT -> "SILENT"
            else -> "SOUND"
        }

        val isTorchOn = ScreenOffAccessibilityService.instance?.flashlightHandler?.isTorchOn ?: false

        DeviceStatusResponse(
            batteryLevel = batteryPct,
            isCharging = isCharging,
            soundMode = soundModeStr,
            isCaffeinateActive = CaffeinateController.isActive.value,
            isFlashlightOn = isTorchOn,
            isAodEnabled = repo.isAodEnabled(),
            isNotificationLightingEnabled = repo.getBoolean(SettingsRepository.KEY_EDGE_LIGHTING_ENABLED, false)
        )
    }

    /**
     * Dims or brightens the device background wallpaper.
     *
     * @param dimLevel Darkness percentage from 0 (normal brightness) to 100 (completely dimmed).
     * @return Result of applying wallpaper dimming.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun setWallpaperDim(dimLevel: Int): AppFunctionResult = withContext(Dispatchers.IO) {
        val clamped = (dimLevel.coerceIn(0, 100)).toFloat() / 100f
        CombinedActionExecutor.execute(applicationContext, Action.DimWallpaper(clamped))
        AppFunctionResult(true, "Wallpaper dim level set to $dimLevel%")
    }

    /**
     * Configures Always On Display (AOD) mode.
     *
     * @param mode Target mode: "Off", "Dynamic", or "On".
     * @return Result of applying AOD configuration.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun setAlwaysOnDisplay(mode: String): AppFunctionResult = withContext(Dispatchers.IO) {
        val repo = SettingsRepository(applicationContext)
        val enabled = !mode.equals("off", ignoreCase = true)
        repo.setAodEnabled(enabled)
        AppFunctionResult(true, "Always On Display set to $mode")
    }

    /**
     * Changes the Pixel lock screen clock style.
     *
     * @param style Clock style name: "DEFAULT", "METRO", "EXPRESSIVE", "PRIDE", "MONOSPACE", or "BUBBLE".
     * @return Result of applying clock style.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun setLockScreenClockStyle(style: String): AppFunctionResult = withContext(Dispatchers.IO) {
        val repo = SettingsRepository(applicationContext)
        repo.putString("lock_screen_clock_style", style.uppercase())
        AppFunctionResult(true, "Lock screen clock style set to ${style.uppercase()}")
    }

    /**
     * Toggles Notification Edge Lighting effect for incoming notifications.
     *
     * @param enabled True to enable notification lighting, false to disable.
     * @return Result of toggling notification lighting.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun toggleNotificationLighting(enabled: Boolean): AppFunctionResult = withContext(Dispatchers.IO) {
        val repo = SettingsRepository(applicationContext)
        repo.putBoolean(SettingsRepository.KEY_EDGE_LIGHTING_ENABLED, enabled)
        AppFunctionResult(true, "Notification lighting ${if (enabled) "enabled" else "disabled"}")
    }

    /**
     * Toggles Flashlight Pulse notification alerts.
     *
     * @param enabled True to enable flashlight pulse for notifications, false to disable.
     * @return Result of toggling flashlight pulse.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun toggleFlashlightPulse(enabled: Boolean): AppFunctionResult = withContext(Dispatchers.IO) {
        val repo = SettingsRepository(applicationContext)
        repo.putBoolean(SettingsRepository.KEY_FLASHLIGHT_PULSE_ENABLED, enabled)
        AppFunctionResult(true, "Flashlight pulse ${if (enabled) "enabled" else "disabled"}")
    }

    /**
     * Turns off the device screen immediately.
     *
     * @return Result of turning off the screen.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun turnScreenOff(): AppFunctionResult = withContext(Dispatchers.IO) {
        DeviceLockUtils.lockDevice(applicationContext, ScreenOffMethod.ACCESSIBILITY)
        AppFunctionResult(true, "Screen turned off")
    }

    /**
     * Toggles system Low Power (Battery Saver) mode.
     *
     * @param enabled True to enable battery saver mode, false to disable.
     * @return Result of toggling low power mode.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun setLowPowerMode(enabled: Boolean): AppFunctionResult = withContext(Dispatchers.IO) {
        val action = if (enabled) Action.TurnOnLowPower else Action.TurnOffLowPower
        CombinedActionExecutor.execute(applicationContext, action)
        AppFunctionResult(true, "Low power mode ${if (enabled) "enabled" else "disabled"}")
    }

    /**
     * Lists all app freeze tags defined in Essentials.
     *
     * @return List of freeze tag summaries.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun listFreezeTags(): List<FreezeTagSummary> = withContext(Dispatchers.IO) {
        val repo = SettingsRepository(applicationContext)
        val tags = repo.getFreezeTags()
        tags.map { tag ->
            FreezeTagSummary(
                id = tag.id,
                name = tag.name,
                appCount = 0,
                isFrozen = false
            )
        }
    }

    /**
     * Freezes all apps associated with a specific app freeze tag.
     *
     * @param tagId The unique ID or name of the freeze tag.
     * @return Result of freezing tag apps.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun freezeTagApps(tagId: String): AppFunctionResult = withContext(Dispatchers.IO) {
        val repo = SettingsRepository(applicationContext)
        val tags = repo.getFreezeTags()
        val targetTag = tags.find { it.id.equals(tagId, ignoreCase = true) || it.name.equals(tagId, ignoreCase = true) }
            ?: return@withContext AppFunctionResult(false, "Freeze tag '$tagId' not found")

        CombinedActionExecutor.execute(applicationContext, Action.FreezeTag("Freeze", listOf(targetTag.id)))
        AppFunctionResult(true, "Apps in tag '${targetTag.name}' frozen successfully")
    }

    /**
     * Unfreezes all apps associated with a specific app freeze tag.
     *
     * @param tagId The unique ID or name of the freeze tag.
     * @return Result of unfreezing tag apps.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun unfreezeTagApps(tagId: String): AppFunctionResult = withContext(Dispatchers.IO) {
        val repo = SettingsRepository(applicationContext)
        val tags = repo.getFreezeTags()
        val targetTag = tags.find { it.id.equals(tagId, ignoreCase = true) || it.name.equals(tagId, ignoreCase = true) }
            ?: return@withContext AppFunctionResult(false, "Freeze tag '$tagId' not found")

        CombinedActionExecutor.execute(applicationContext, Action.FreezeTag("Unfreeze", listOf(targetTag.id)))
        AppFunctionResult(true, "Apps in tag '${targetTag.name}' unfrozen successfully")
    }

    /**
     * Lists all custom DIY automations configured in Essentials.
     *
     * @return List of automation summaries.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun listAutomations(): List<AutomationSummary> = withContext(Dispatchers.IO) {
        val automations = DIYRepository.automations.value
        automations.map { auto ->
            val triggerDesc = when (auto.type) {
                Automation.Type.TRIGGER -> auto.trigger?.javaClass?.simpleName ?: "Trigger"
                Automation.Type.STATE -> auto.state?.javaClass?.simpleName ?: "State"
                Automation.Type.APP -> "App launch"
                else -> "Manual"
            }
            AutomationSummary(
                id = auto.id,
                title = auto.id,
                type = auto.type.name,
                isEnabled = auto.isEnabled,
                triggerOrStateDescription = triggerDesc,
                actionsCount = auto.actions.size
            )
        }
    }

    /**
     * Toggles an existing DIY automation on or off by its ID.
     *
     * @param automationId Unique ID of the automation.
     * @param enabled True to enable the automation, false to disable it.
     * @return Result of toggling the automation.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun toggleAutomation(automationId: String, enabled: Boolean): AppFunctionResult = withContext(Dispatchers.IO) {
        val automations = DIYRepository.automations.value
        val target = automations.find { it.id.equals(automationId, ignoreCase = true) }
            ?: return@withContext AppFunctionResult(false, "Automation '$automationId' not found")

        DIYRepository.updateAutomation(target.copy(isEnabled = enabled))
        AppFunctionResult(true, "Automation '${target.id}' ${if (enabled) "enabled" else "disabled"}")
    }

    /**
     * Deletes a DIY automation by its ID.
     *
     * @param automationId Unique ID of the automation to remove.
     * @return Result of deleting the automation.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun deleteAutomation(automationId: String): AppFunctionResult = withContext(Dispatchers.IO) {
        val automations = DIYRepository.automations.value
        val target = automations.find { it.id.equals(automationId, ignoreCase = true) }
            ?: return@withContext AppFunctionResult(false, "Automation '$automationId' not found")

        DIYRepository.removeAutomation(target.id)
        AppFunctionResult(true, "Automation '${target.id}' deleted successfully")
    }

    /**
     * Creates a new DIY automation in Essentials directly from parameters parsed by AI.
     *
     * @param params Parameters specifying the automation title, type, trigger/state, and target action.
     * @return Result of creating the automation.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun createAutomation(params: CreateAutomationParams): AppFunctionResult = withContext(Dispatchers.IO) {
        val autoType = when (params.type.uppercase()) {
            "STATE" -> Automation.Type.STATE
            "APP" -> Automation.Type.APP
            else -> Automation.Type.TRIGGER
        }

        val trigger: Trigger? = if (autoType == Automation.Type.TRIGGER) {
            when (params.triggerType) {
                "ScreenOff" -> Trigger.ScreenOff
                "ScreenOn" -> Trigger.ScreenOn
                "DeviceUnlock" -> Trigger.DeviceUnlock
                "ChargerConnected" -> Trigger.ChargerConnected
                "ChargerDisconnected" -> Trigger.ChargerDisconnected
                "Schedule" -> Trigger.Schedule(params.hour, params.minute)
                else -> Trigger.ChargerConnected
            }
        } else null

        val state: State? = if (autoType == Automation.Type.STATE) {
            when (params.stateType) {
                "Charging" -> State.Charging
                "ScreenOn" -> State.ScreenOn
                "TimePeriod" -> State.TimePeriod(params.hour, params.minute, params.endHour, params.endMinute)
                else -> State.Charging
            }
        } else null

        val action: Action = when (params.actionType) {
            "TurnOnFlashlight" -> Action.TurnOnFlashlight
            "TurnOffFlashlight" -> Action.TurnOffFlashlight
            "ToggleFlashlight" -> Action.ToggleFlashlight
            "DimWallpaper" -> Action.DimWallpaper(params.dimWallpaperAmount)
            "SoundMode" -> Action.SoundMode(
                when (params.soundMode.uppercase()) {
                    "VIBRATE" -> Action.SoundModeType.VIBRATE
                    "SILENT" -> Action.SoundModeType.SILENT
                    else -> Action.SoundModeType.SOUND
                }
            )
            "TurnOnLowPower" -> Action.TurnOnLowPower
            "TurnOffLowPower" -> Action.TurnOffLowPower
            "ScreenOff" -> Action.ScreenOff()
            "MediaPlayPause" -> Action.MediaPlayPause
            "TakeScreenshot" -> Action.TakeScreenshot
            "FreezeTag" -> Action.FreezeTag(params.freezeMode, listOf(params.freezeTagId))
            else -> Action.HapticVibration
        }

        val newAutomation = Automation(
            id = UUID.randomUUID().toString(),
            type = autoType,
            trigger = trigger,
            state = state,
            actions = listOf(action),
            isEnabled = true
        )

        DIYRepository.addAutomation(newAutomation)
        AppFunctionResult(true, "Created automation '${params.title}' successfully")
    }
}

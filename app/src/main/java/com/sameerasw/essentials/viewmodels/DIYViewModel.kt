package com.sameerasw.essentials.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sameerasw.essentials.domain.diy.Automation
import com.sameerasw.essentials.domain.diy.DIYRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


sealed class GenAIState {
    object Idle : GenAIState()
    object Loading : GenAIState()
    data class Success(val suggestion: com.sameerasw.essentials.domain.genai.AutomationSuggestion) : GenAIState()
    data class Error(val message: String) : GenAIState()
}

class DIYViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DIYRepository

    private val _genAIState = kotlinx.coroutines.flow.MutableStateFlow<GenAIState>(GenAIState.Idle)
    val genAIState: StateFlow<GenAIState> = _genAIState.asStateFlow()

    init {
        repository.init(application)
    }

    val automations: StateFlow<List<Automation>> = repository.automations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteAutomation(id: String) {
        repository.removeAutomation(id)
    }

    fun toggleAutomation(id: String) {
        repository.getAutomation(id)?.let { automation ->
            repository.updateAutomation(automation.copy(isEnabled = !automation.isEnabled))
        }
    }

    fun requestGenAISuggestion(description: String) {
        viewModelScope.launch {
            _genAIState.value = GenAIState.Loading
            val result = com.sameerasw.essentials.domain.genai.GenAIAutomationService.suggestAutomation(description)
            _genAIState.value = result.fold(
                onSuccess = { GenAIState.Success(it) },
                onFailure = { GenAIState.Error(it.message ?: "Failed to generate suggestion") }
            )
        }
    }

    fun dismissGenAISuggestion() {
        _genAIState.value = GenAIState.Idle
    }

    fun confirmGenAISuggestion(suggestion: com.sameerasw.essentials.domain.genai.AutomationSuggestion) {
        val automation = mapSuggestionToAutomation(suggestion)
        repository.addAutomation(automation)
        _genAIState.value = GenAIState.Idle
    }

    private fun mapSuggestionToAutomation(suggestion: com.sameerasw.essentials.domain.genai.AutomationSuggestion): Automation {
        val id = java.util.UUID.randomUUID().toString()
        val type = when (suggestion.type.uppercase()) {
            "STATE" -> Automation.Type.STATE
            "APP" -> Automation.Type.APP
            else -> Automation.Type.TRIGGER
        }

        val trigger = when (suggestion.triggerType) {
            "ScreenOff" -> com.sameerasw.essentials.domain.diy.Trigger.ScreenOff
            "ScreenOn" -> com.sameerasw.essentials.domain.diy.Trigger.ScreenOn
            "DeviceUnlock" -> com.sameerasw.essentials.domain.diy.Trigger.DeviceUnlock
            "ChargerConnected" -> com.sameerasw.essentials.domain.diy.Trigger.ChargerConnected
            "ChargerDisconnected" -> com.sameerasw.essentials.domain.diy.Trigger.ChargerDisconnected
            "Schedule" -> com.sameerasw.essentials.domain.diy.Trigger.Schedule()
            "BluetoothConnected" -> com.sameerasw.essentials.domain.diy.Trigger.BluetoothConnected()
            "BluetoothDisconnected" -> com.sameerasw.essentials.domain.diy.Trigger.BluetoothDisconnected()
            "WifiConnected" -> com.sameerasw.essentials.domain.diy.Trigger.WifiConnected()
            "WifiDisconnected" -> com.sameerasw.essentials.domain.diy.Trigger.WifiDisconnected()
            else -> if (type == Automation.Type.TRIGGER) com.sameerasw.essentials.domain.diy.Trigger.ScreenOff else null
        }

        val state = when (suggestion.stateType) {
            "Charging" -> com.sameerasw.essentials.domain.diy.State.Charging
            "ScreenOn" -> com.sameerasw.essentials.domain.diy.State.ScreenOn
            "TimePeriod" -> com.sameerasw.essentials.domain.diy.State.TimePeriod()
            else -> if (type == Automation.Type.STATE) com.sameerasw.essentials.domain.diy.State.Charging else null
        }

        val actions = suggestion.actionTypes.mapNotNull { actionName ->
            when (actionName) {
                "HapticVibration" -> com.sameerasw.essentials.domain.diy.Action.HapticVibration
                "ShowNotification" -> com.sameerasw.essentials.domain.diy.Action.ShowNotification
                "RemoveNotification" -> com.sameerasw.essentials.domain.diy.Action.RemoveNotification
                "TurnOnFlashlight" -> com.sameerasw.essentials.domain.diy.Action.TurnOnFlashlight
                "TurnOffFlashlight" -> com.sameerasw.essentials.domain.diy.Action.TurnOffFlashlight
                "ToggleFlashlight" -> com.sameerasw.essentials.domain.diy.Action.ToggleFlashlight
                "DimWallpaper" -> com.sameerasw.essentials.domain.diy.Action.DimWallpaper()
                "DeviceEffects" -> com.sameerasw.essentials.domain.diy.Action.DeviceEffects()
                "SoundMode" -> com.sameerasw.essentials.domain.diy.Action.SoundMode()
                "TurnOnLowPower" -> com.sameerasw.essentials.domain.diy.Action.TurnOnLowPower
                "TurnOffLowPower" -> com.sameerasw.essentials.domain.diy.Action.TurnOffLowPower
                "ScreenOff" -> com.sameerasw.essentials.domain.diy.Action.ScreenOff()
                "MediaPlayPause" -> com.sameerasw.essentials.domain.diy.Action.MediaPlayPause
                "MediaNext" -> com.sameerasw.essentials.domain.diy.Action.MediaNext
                "MediaPrevious" -> com.sameerasw.essentials.domain.diy.Action.MediaPrevious
                "AIAssistant" -> com.sameerasw.essentials.domain.diy.Action.AIAssistant
                "TakeScreenshot" -> com.sameerasw.essentials.domain.diy.Action.TakeScreenshot
                "ToggleMediaVolume" -> com.sameerasw.essentials.domain.diy.Action.ToggleMediaVolume
                "LikeCurrentSong" -> com.sameerasw.essentials.domain.diy.Action.LikeCurrentSong
                "CircleToSearch" -> com.sameerasw.essentials.domain.diy.Action.CircleToSearch
                "PinApp" -> com.sameerasw.essentials.domain.diy.Action.PinApp
                "SometimesEssentials" -> com.sameerasw.essentials.domain.diy.Action.SometimesEssentials()
                "FreezeTag" -> com.sameerasw.essentials.domain.diy.Action.FreezeTag()
                else -> null
            }
        }

        return Automation(
            id = id,
            type = type,
            trigger = trigger,
            state = state,
            actions = if (type == Automation.Type.TRIGGER) actions else emptyList(),
            entryAction = if (type == Automation.Type.STATE || type == Automation.Type.APP) actions.firstOrNull() else null,
            exitAction = if ((type == Automation.Type.STATE || type == Automation.Type.APP) && actions.size > 1) actions[1] else null,
            isEnabled = true
        )
    }
}


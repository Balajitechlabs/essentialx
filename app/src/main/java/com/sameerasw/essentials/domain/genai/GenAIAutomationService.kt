package com.sameerasw.essentials.domain.genai

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.prompt.Generation
import com.google.mlkit.genai.prompt.TextPart
import com.google.mlkit.genai.prompt.generateContentRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GenAIAutomationService {
    private const val TAG = "GenAIAutomationService"
    private val gson = Gson()

    suspend fun isSupported(): Boolean = withContext(Dispatchers.IO) {
        try {
            val generativeModel = Generation.getClient()
            val status = generativeModel.checkStatus()
            status == FeatureStatus.AVAILABLE || status == FeatureStatus.DOWNLOADABLE || status == FeatureStatus.DOWNLOADING
        } catch (e: Exception) {
            Log.e(TAG, "Failed to check GenAI status", e)
            false
        }
    }

    suspend fun suggestAutomation(userPrompt: String): Result<AutomationSuggestion> = withContext(Dispatchers.IO) {
        try {
            val generativeModel = Generation.getClient()
            when (generativeModel.checkStatus()) {

                FeatureStatus.UNAVAILABLE -> {
                    return@withContext Result.failure(IllegalStateException("GenAI feature is UNAVAILABLE on this device"))
                }
                FeatureStatus.DOWNLOADABLE -> {
                    Log.d(TAG, "Gemini Nano model is downloadable. Triggering download...")
                    var downloadFailedReason: String? = null
                    generativeModel.download().collect { downloadStatus ->
                        when (downloadStatus) {
                            is com.google.mlkit.genai.common.DownloadStatus.DownloadFailed -> {
                                downloadFailedReason = downloadStatus.e.message
                            }
                            else -> {}
                        }
                    }
                    if (downloadFailedReason != null) {
                        return@withContext Result.failure(IllegalStateException("Gemini Nano download failed: $downloadFailedReason"))
                    }
                }
                FeatureStatus.DOWNLOADING -> {
                    Log.d(TAG, "Gemini Nano model is currently downloading...")
                    generativeModel.download().collect {}
                }
                FeatureStatus.AVAILABLE -> {
                    // Ready to proceed
                }
            }


            val systemInstructionText = """
                You are an automation assistant for the Essentials Android app.
                Map the user's description into a single JSON object matching the following structure:
                {
                  "type": "TRIGGER" or "STATE" or "APP",
                  "title": "Short descriptive title",
                  "triggerType": "ScreenOff" or "ScreenOn" or "DeviceUnlock" or "ChargerConnected" or "ChargerDisconnected" or "Schedule" or "BluetoothConnected" or "BluetoothDisconnected" or "WifiConnected" or "WifiDisconnected",
                  "stateType": "Charging" or "ScreenOn" or "TimePeriod",
                  "actionTypes": ["HapticVibration", "ShowNotification", "RemoveNotification", "TurnOnFlashlight", "TurnOffFlashlight", "ToggleFlashlight", "DimWallpaper", "DeviceEffects", "SoundMode", "TurnOnLowPower", "TurnOffLowPower", "ScreenOff", "MediaPlayPause", "MediaNext", "MediaPrevious", "AIAssistant", "TakeScreenshot", "ToggleMediaVolume", "LikeCurrentSong", "CircleToSearch", "PinApp", "SometimesEssentials", "FreezeTag"],
                  "explanation": "Brief 1-2 sentence description of what this automation does"
                }

                Respond ONLY with valid raw JSON without any markdown formatting or code blocks.
            """.trimIndent()

            val contextPrompt = """
                $systemInstructionText

                User request: "$userPrompt"
            """.trimIndent()

            val request = generateContentRequest(TextPart(contextPrompt)) {
                temperature = 0.2f
            }

            val response = generativeModel.generateContent(request)
            val candidateText = response.candidates.firstOrNull()?.text ?: ""

            // Clean up possible markdown code fences from the output
            val jsonText = candidateText
                .replace("```json", "")
                .replace("```", "")
                .trim()

            if (jsonText.isNotEmpty()) {
                val suggestion = gson.fromJson(jsonText, AutomationSuggestion::class.java)
                if (suggestion != null) {
                    Result.success(suggestion)
                } else {
                    Result.failure(Exception("Failed to parse JSON response into AutomationSuggestion"))
                }
            } else {
                val finishReason = response.candidates.firstOrNull()?.finishReason
                Result.failure(Exception("AI response was empty. Finish reason: $finishReason"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error generating automation suggestion", e)
            Result.failure(e)
        }
    }
}


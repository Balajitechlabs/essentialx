package com.sameerasw.essentials.appfunctions.dto

import androidx.annotation.Keep
import androidx.appfunctions.AppFunctionSerializable

/**
 * Summary of a DIY automation rule.
 *
 * @param id Unique ID of the automation.
 * @param title Descriptive title.
 * @param type Automation type (TRIGGER, STATE, APP).
 * @param isEnabled True if currently enabled.
 * @param triggerOrStateDescription Human readable trigger or state description.
 * @param actionsCount Number of actions in this automation.
 */
@Keep
@AppFunctionSerializable(isDescribedByKDoc = true)
data class AutomationSummary(
    val id: String,
    val title: String,
    val type: String,
    val isEnabled: Boolean,
    val triggerOrStateDescription: String,
    val actionsCount: Int
)

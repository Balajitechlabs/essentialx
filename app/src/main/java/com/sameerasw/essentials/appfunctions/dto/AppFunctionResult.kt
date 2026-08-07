package com.sameerasw.essentials.appfunctions.dto

import androidx.annotation.Keep
import androidx.appfunctions.AppFunctionSerializable

/**
 * Result of executing an AppFunction operation.
 *
 * @param success True if operation succeeded, false otherwise.
 * @param message Human-readable detail or status message.
 */
@Keep
@AppFunctionSerializable(isDescribedByKDoc = true)
data class AppFunctionResult(
    val success: Boolean,
    val message: String
)

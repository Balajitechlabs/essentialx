package com.sameerasw.essentials.appfunctions.dto

import androidx.annotation.Keep
import androidx.appfunctions.AppFunctionSerializable

/**
 * Summary of an app freeze tag.
 *
 * @param id Unique tag identifier.
 * @param name Tag display name.
 * @param appCount Number of apps associated with this tag.
 * @param isFrozen True if apps in this tag are currently frozen.
 */
@Keep
@AppFunctionSerializable(isDescribedByKDoc = true)
data class FreezeTagSummary(
    val id: String,
    val name: String,
    val appCount: Int,
    val isFrozen: Boolean
)

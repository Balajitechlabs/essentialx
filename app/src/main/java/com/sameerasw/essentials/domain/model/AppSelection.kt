/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Domain Layer Models & Registries
 * File: AppSelection.kt
 * Description: Domain model and business logic entry for AppSelection.kt.
 */

package com.sameerasw.essentials.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AppSelection(
    @SerializedName("packageName") val packageName: String,
    @SerializedName("isEnabled") val isEnabled: Boolean
)

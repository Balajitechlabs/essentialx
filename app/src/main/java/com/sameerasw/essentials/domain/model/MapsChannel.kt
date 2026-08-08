/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Domain Layer Models & Registries
 * File: MapsChannel.kt
 * Description: Domain model and business logic entry for MapsChannel.kt.
 */

package com.sameerasw.essentials.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MapsChannel(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("isEnabled") val isEnabled: Boolean = false
)

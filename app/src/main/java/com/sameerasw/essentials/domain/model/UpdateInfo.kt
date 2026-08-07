/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Domain Layer Models & Registries
 * File: UpdateInfo.kt
 * Description: Domain model and business logic entry for UpdateInfo.kt.
 */

package com.sameerasw.essentials.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateInfo(
    @SerializedName("versionName") val versionName: String,
    @SerializedName("releaseNotes") val releaseNotes: String,
    @SerializedName("downloadUrl") val downloadUrl: String,
    @SerializedName("releaseUrl") val releaseUrl: String = "",
    @SerializedName("isUpdateAvailable") val isUpdateAvailable: Boolean = false
)

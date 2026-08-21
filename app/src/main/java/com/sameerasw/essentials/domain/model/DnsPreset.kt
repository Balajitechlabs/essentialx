/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Domain Layer Models & Registries
 * File: DnsPreset.kt
 * Description: Domain model and business logic entry for DnsPreset.kt.
 */

package com.sameerasw.essentials.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DnsPreset(
    @SerializedName("id") val id: String =
        java.util.UUID
            .randomUUID()
            .toString(),
    @SerializedName("name") val name: String,
    @SerializedName("hostname") val hostname: String,
    @SerializedName("isDefault") val isDefault: Boolean = false,
)

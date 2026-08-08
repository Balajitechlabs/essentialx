/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Domain Layer Models & Registries
 * File: NotificationApp.kt
 * Description: Domain model and business logic entry for NotificationApp.kt.
 */

package com.sameerasw.essentials.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap

@Immutable
data class NotificationApp(
    val packageName: String,
    val appName: String,
    val isEnabled: Boolean,
    val icon: ImageBitmap,
    val isSystemApp: Boolean,
    val lastUpdated: Long
)

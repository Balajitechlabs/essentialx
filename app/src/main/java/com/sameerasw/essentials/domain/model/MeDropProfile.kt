/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Domain Layer
 * File: MeDropProfile.kt
 */

package com.sameerasw.essentials.domain.model

enum class MeDropProfileType {
    CONTACT,
    PROFESSIONAL,
    CUSTOM
}

data class MeDropProfile(
    val type: MeDropProfileType,
    val enabled: Boolean = true,
    val photoUri: String? = null,
    val selectedEntryIds: Set<String>? = null
)
